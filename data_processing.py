# Author: Axel Mukwena
# ECG Biometric Authentication using CNN

# Data Processing: Converts .dat files
# to .csv and generates annotated dataset
from sklearn.model_selection import train_test_split
from biosppy.signals import ecg
import matplotlib.pyplot as plt
from shutil import copyfile
import pandas as pd
import numpy as np
import warnings
# import helpers
import librosa
import wfdb
import os


# Sampling rate for ECG-ID is 500 Hz

# ************************************ Get data ************************************

# Get data, convert .dat to .csv files
class GetData:
    def __init__(self):
        self.dir = os.path.join(os.getcwd(), 'dataset')
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
        self.dir = os.path.join(os.getcwd(), 'dataset')
        self.id = []
        self.person_labels = []
        self.age_labels = []
        self.gender_labels = []
        self.date_labels = []
        self.processedData = {}
        self.totalImages = 0
        self.person = 0
        self.record = 0

        print('Setting up data labels...')
        self.extract_labels()
        ecglabels = [list(i) for i in zip(self.id, self.person_labels, self.age_labels,
                                          self.gender_labels, self.date_labels)]

        print('\nExporting labels to csv...')
        ecglabelsDF = pd.DataFrame(ecglabels)
        ecglabelsDF.to_csv(os.path.join('processedData', 'ecglabels.csv'), index=False)
        print('Export complete.')

        print('\nSetting up data features...')
        self.extract_features()
        print('\nExporting processed data...')
        self.processedDataDF = pd.DataFrame(self.processedData)
        self.processedDataDF = self.processedDataDF.T
        self.processedDataDF.to_csv(os.path.join('processedData', 'processedData.csv'), index=False)
        print('Export complete.')

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
        # Permissible noise factor value = x > 0.004
        noiseAdding = array + 0.009 * np.random.normal(0, 1, len(array))
        self.sigToImage(noiseAdding, 2, count)

        # Permissible factor values = samplingRate / 10
        timeShifting = np.roll(array, int(500 / 10))
        self.sigToImage(timeShifting, 3, count)

        # Disable warnings such as below from librosa
        # UserWarning: n_fft=2048 is too small for input signal of length=371
        # See https://github.com/librosa/librosa/issues/1194
        warnings.filterwarnings('ignore', category=UserWarning)

        # Permissible factor values = -5 <= x <= 5
        pitchShifting = librosa.effects.pitch_shift(array, 500, n_steps=-5.0)
        self.sigToImage(pitchShifting, 4, count)

        # Permissible factor values = 0 < x < 1.0
        factor = 0.99  # Yields the best reults without losing ecg wave shape
        timeStretching = librosa.effects.time_stretch(array, factor)
        self.sigToImage(timeStretching, 5, count)

    # Convert segmented signals into grayscale images, nparray
    def sigToImage(self, array, augmentType, count):
        fig = plt.figure(frameon=False)  # plt.figure(figsize=(20, 4))
        plt.plot(array, color='gray')
        plt.xticks([]), plt.yticks([])
        for spine in plt.gca().spines.values():
            spine.set_visible(False)

        folder = 'processedData/images/' + f'{self.person:02}' + '/'
        if not os.path.exists(folder):
            os.makedirs(folder)
        # Naming convention only accomodates tens place numbers 0-99
        filename = folder + f'{self.person:02}' + '_' + f'{self.record:02}' \
                   + '_' + f'{augmentType:02}' + '_' + f'{count:02}' + '.png'
        fig.savefig(filename)
        plt.cla()
        plt.clf()
        plt.close('all')

        # Add segment details to global record dictionary
        self.totalImages += 1
        self.processedData[self.totalImages] = self.person, filename


# Set up data for training and testing
class Setup:
    def __init__(self):
        self.x_train, self.x_val, self.x_test = [], [], []
        self.y_train, self.y_val, self.y_test = [], [], []

        self.split()
        self.move()

    def split(self):
        data = pd.read_csv(os.path.join('processedData', 'processedData.csv'))

        x = data.loc[:, '1'].values
        y = data.loc[:, '0'].values

        train = 0.7
        validation = 0.15
        test = 0.15
        # train 70% since test is 30%
        self.x_train, self.x_test, self.y_train, self.y_test = train_test_split(
            x, y, test_size=1 - train, shuffle=True, random_state=42)

        # test 15%, validation 15%
        self.x_val, self.x_test, self.y_val, self.y_test = train_test_split(
            self.x_test, self.y_test, test_size=test / (test + validation), random_state=3)

        # Plot bar to show distribution of data
        # helpers.plotBar(y, y_train, y_val, y_test)

    # Relocate & restructure data to prepapre training, val & testing
    def move(self):
        # Training data
        for index, address in enumerate(self.x_train):
            folder = 'data/train/' + f'{self.y_train[index]:02}' + '/'
            if not os.path.exists(folder):
                os.makedirs(folder)
            filename = folder + os.path.basename(address)
            copyfile(address, filename)

        # Validation data
        for index, address in enumerate(self.x_val):
            folder = 'data/validation/' + f'{self.y_val[index]:02}' + '/'
            if not os.path.exists(folder):
                os.makedirs(folder)
            filename = folder + os.path.basename(address)
            copyfile(address, filename)

        # Testing data
        for index, address in enumerate(self.x_test):
            folder = 'data/test/' + f'{self.y_test[index]:02}' + '/'
            if not os.path.exists(folder):
                os.makedirs(folder)
            filename = folder + os.path.basename(address)
            copyfile(address, filename)


# ---------------- Driver ---------------- #
# GetData()
# ProcessData()
Setup()
