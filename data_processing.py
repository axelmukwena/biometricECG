# Author: Axel Mukwena
# ECG Biometric Authentication using CNN

# Data Processing: Converts .dat files
# to .csv and generates annotated dataset
from biosppy.signals import ecg
import matplotlib.pyplot as plt
import pyrubberband as pyrb
import pandas as pd
import numpy as np
# import helpers
import pickle
import wfdb
import cv2
import os


# Sampling rate, sr for ECG-ID is 500 Hz

# ************************************ Get data ************************************

# Get data, convert .dat to .csv files
class GetData:
    def __init__(self):
        self.dir = os.path.join(os.getcwd(), 'rawdata')
        self.database = 'ecgiddb'

        # crawls into every folder and sends .dat file to constructor
        print('Converting to .dat to .csv...')
        folders = sorted(os.listdir(self.dir))
        for folder in folders:
            if folder.startswith('Person_'):
                records = sorted(os.listdir(os.path.join(self.dir, folder)))
                for record in records:
                    # only get the first 2 records for all people to have equal weights
                    if record.startswith('rec_1') or record.startswith('rec_2') and record.endswith('dat'):
                        basename = record.split('.', 1)[0]
                        self.constructor(folder, basename)

    def constructor(self, folder, filename):
        signals, fields = wfdb.rdsamp(filename, sampfrom=0,
                                      pn_dir=os.path.join(self.database, folder))
        df = pd.DataFrame(signals)
        df.to_csv(os.path.join(self.dir, folder, filename + '.' 'csv'), index=False)


# ************************************ Process data ************************************

# Generates features and labels
class ProcessData:
    def __init__(self):
        self.dir = os.path.join(os.getcwd(), 'rawdata')
        self.id = []
        self.person_labels = []
        self.age_labels = []
        self.gender_labels = []
        self.date_labels = []
        self.person = 0
        self.record = 0

        print('Setting up data labels...')
        self.extract_labels()
        ecglabels = [list(i) for i in zip(self.id, self.person_labels, self.age_labels,
                                          self.gender_labels, self.date_labels)]

        print('\nExporting labels to csv...')
        ecglabelsDF = pd.DataFrame(ecglabels)
        ecglabelsDF.to_csv('ecglabels.csv', index=False)
        print('Export complete.')

        print('\nExporting features to images...')
        self.extract_features()
        print('\nExport complete.')

    # Extracts labels and features from rec_1.hea of each person
    def extract_labels(self):
        folders = sorted(os.listdir(self.dir))
        for folder in folders:
            if folder.startswith('Person_'):
                self.person += 1
                self.id.append(self.person)
                self.person_labels.append(folder)
                for file in os.listdir(os.path.join(self.dir, folder)):
                    if file.startswith('rec_1.') and file.endswith('hea'):
                        with open(os.path.join(self.dir, folder, file), 'r') as f:
                            array2d = [[str(token) for token in line.split()] for line in f]
                            self.age_labels.append(array2d[4][2])
                            self.gender_labels.append(array2d[5][2])
                            self.date_labels.append(array2d[6][3])
                        f.close()

    # Extracts features from csv file of each person
    def extract_features(self):
        self.person = 0  # set default person counter
        folders = sorted(os.listdir(self.dir))
        for folder in folders:
            if folder.startswith('Person_'):
                self.person += 1
                files = sorted(os.listdir(os.path.join(self.dir, folder)))
                self.record = 0  # set default file counter
                for file in files:
                    if (file.startswith('rec_1.') or file.startswith('rec_2.')) and file.endswith('csv'):
                        with open(os.path.join(self.dir, folder, file), 'r') as x:
                            self.record += 1
                            # read csv data for each person from col=1 (filtered sig)
                            features = pd.read_csv(x)
                            filteredData = []
                            for row in range(len(features)):
                                filteredData.append(features.iat[row, 1])

                            self.segment(np.asarray(filteredData))
                print('Person ' + str(self.person))

    # ECG R-peak segmentation
    def segment(self, array):
        count = 1
        peaks = ecg.christov_segmenter(signal=array, sampling_rate=500)[0]
        for i in (peaks[1:-1]):
            diff1 = abs(peaks[count - 1] - i)
            diff2 = abs(peaks[count + 1] - i)
            x = peaks[count - 1] + diff1 // 2
            y = peaks[count + 1] - diff2 // 2
            peakWave = array[x:y]
            self.augment(peakWave, count)
            count += 1

    # Augment each signal and convert call fucntion to convert it to image
    def augment(self, array, count):
        # Original signal
        # Second argument determines type of aumentation applied to signal
        self.sigToImage(array, 1, count)

        # Noise addition using normal distribution with mean = 0 and std =1
        # Permissible noise factor value = x > 0.009
        noiseAdding = array + 0.009 * np.random.normal(0, 1, len(array))
        self.sigToImage(noiseAdding, 2, count)

        # Permissible factor values = samplingRate / 100
        timeShifting = np.roll(array, int(500 / 100))
        self.sigToImage(timeShifting, 3, count)

        # Permissible factor values = -5 <= x <= 5
        pitchShifting = pyrb.pitch_shift(array, 500, -3)
        self.sigToImage(pitchShifting, 4, count)

        # Permissible factor values = 0 < x < 1.0
        factor = 0.95  # Yields the best reults without losing ecg wave shape
        timeStretching = pyrb.time_stretch(array, 500, factor)
        self.sigToImage(timeStretching, 5, count)

    # Convert segmented signals into grayscale images, nparray
    def sigToImage(self, array, augmentType, count):
        # Somehow with bbox_inches='tight', figsize is not
        # accurate hence resize again with cv2
        fig = plt.figure(frameon=False, figsize=(4.8, 4.8))  # plt.figure(figsize=(20, 4))
        plt.plot(array)
        plt.xticks([]), plt.yticks([])
        for spine in plt.gca().spines.values():
            spine.set_visible(False)

        folder = 'data480x480/' + f'{self.person:02}' + '/'
        if not os.path.exists(folder):
            os.makedirs(folder)
        # Naming convention only accomodates tens place numbers 0-99
        filename = folder + f'{self.person:02}' + '_' + f'{self.record:02}' \
                   + '_' + f'{augmentType:02}' + '_' + f'{count:02}' + '.png'
        fig.savefig(filename, bbox_inches='tight')

        # resize
        img = cv2.imread(filename, cv2.IMREAD_GRAYSCALE)
        img = cv2.resize(img, (480, 480), interpolation=cv2.INTER_CUBIC)
        cv2.imwrite(filename, img)

        plt.cla()
        plt.clf()
        plt.close('all')


# Set up data for training and testing
class Setup:
    def __init__(self):
        self.dir = os.path.join(os.getcwd(), 'data480x480')
        self.identity = {}

        self.y, self.x = [], []
        self.x_train, self.x_val, self.x_test = [], [], []
        self.y_train, self.y_val, self.y_test = [], [], []

        self.load()

    # Load the data
    def load(self):
        # Classes
        self.identity = pd.read_csv('ecglabels.csv')
        self.identity = self.identity.set_index('0').T.to_dict('list')

        # Load images
        person = 0
        folders = sorted(os.listdir(self.dir))
        for folder in folders:
            if not folder.startswith('.'):
                person += 1
                images = sorted(os.listdir(os.path.join(self.dir, folder)))
                for image in images:
                    if image.endswith('png'):
                        self.y.append(person)
                        imageArray = cv2.imread(os.path.join(self.dir, folder, image), cv2.IMREAD_GRAYSCALE)
                        self.x.append(imageArray)

        print('Saving to pickle... Complete.')
        pickleOut = open('data480x480.pickle', 'wb')
        pickle.dump((self.identity, self.y, self.x), pickleOut)
        pickleOut.close()

        # Plot bar to show distribution of data
        # helpers.plotBar(y, y_train, y_val, y_test)


# ---------------- Driver ---------------- #
# GetData()
ProcessData()
Setup()
