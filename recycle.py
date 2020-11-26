# Author: Axel Mukwena
# ECG Biometric Authentication using CNN

# Data Processing: Converts .dat files
# to .csv and generates annotated dataset
from biosppy.signals import ecg
import matplotlib.pyplot as plt
from scipy import signal
import pandas as pd
import numpy as np
import wfdb
import cv2
import os


# Get data, convert .dat to .csv files
class GetData:
    def __init__(self):
        self.dir = os.path.join(os.getcwd(), 'data')
        self.database = 'ecgiddb'

        # crawls into every folder and sends .dat file to constructor
        print('Converting to csv...')
        folders = sorted(os.listdir(self.dir))
        for folder in folders:
            if folder.startswith('Person_'):
                records = sorted(os.listdir(os.path.join(self.dir, folder)))
                for record in records:
                    if record.startswith('rec_') and record.endswith('dat'):
                        basename = record.split('.', 1)[0]
                        self.constructor(folder, basename)

    def constructor(self, folder, filename):
        signals, fields = wfdb.rdsamp(filename, sampfrom=0,
                                      pn_dir=os.path.join(self.database, folder))
        df = pd.DataFrame(signals)
        df.to_csv(os.path.join(self.dir, folder, filename + '.' 'csv'), index=False)


# Segment signals by R-peak using Christov segmenter
def segment(array):
    count = 1
    signals = []
    peaks = ecg.christov_segmenter(signal=array, sampling_rate=500)[0]
    for i in (peaks[1:-1]):
        diff1 = abs(peaks[count - 1] - i)
        diff2 = abs(peaks[count + 1] - i)
        x = peaks[count - 1] + diff1 // 2
        y = peaks[count + 1] - diff2 // 2
        sig = array[x:y]
        signals.append(sig)
        count += 1
    return signals


# Convert segmented signals into grayscale images
def sigToImage(array, person, record):
    fig = plt.figure(frameon=False)
    plt.plot(array)
    plt.xticks([]), plt.yticks([])
    for spine in plt.gca().spines.values():
        spine.set_visible(False)

    folder = 'processedData/images/' + str(person) + '/'
    if not os.path.exists(folder):
        os.makedirs(folder)
    filename = folder + str(record) + '.png'
    fig.savefig(filename)

    img_gray = cv2.imread(filename, cv2.IMREAD_GRAYSCALE)
    # img_gray = cv2.resize(img_gray, (128, 128), interpolation=cv2.INTER_LANCZOS4)
    cv2.imwrite(filename, img_gray)
    plt.cla()
    plt.clf()
    plt.close('all')


# Transform 1-D array signals into 2-D array as spectrogram.
# Take log and standardize spectrogram. (Not implemented)
def fourierSpectrogram(array, person, record):
    f, t, Sxx = signal.spectrogram(array)

    fig = plt.figure(frameon=False)
    plt.pcolormesh(t, f, Sxx, shading='gouraud', cmap='Greys')

    pp = 'processedData/spectrogram/' + str(person) + '/'
    if not os.path.exists(pp):
        os.makedirs(pp)
    filename = pp + str(record) + '.png'

    fig.savefig(filename)
    plt.cla()
    plt.clf()
    plt.close('all')


# Generates features and labels
class ProcessData:
    def __init__(self):
        self.dir = os.path.join(os.getcwd(), 'data')
        self.person_labels = []
        self.age_labels = []
        self.gender_labels = []
        self.date_labels = []
        self.filtered_signal = pd.DataFrame()

        print('Setting up data labels...')
        self.extract_labels(self.dir)
        ecglabels = [list(i) for i in zip(self.person_labels, self.age_labels,
                                          self.gender_labels, self.date_labels)]

        print('Exporting labels to csv...')
        ecglabelsDF = pd.DataFrame(ecglabels)
        ecglabelsDF.to_csv(os.path.join('processedData', 'ecglabels.csv'), index=False)
        print('Export complete.')

        print('\nSetting up data features...')
        self.extract_features(self.dir)
        print('Exporting feature set to csv...')
        self.filtered_signal.to_csv(os.path.join('processedData', 'filtereddata.csv'), index=False)
        print('\nExport complete.')

        if os.path.isfile(os.path.join('processedData', 'filtereddata' + '.' + 'csv')):
            print('Data in processedData/ is now ready for training')

    # Extracts labels and features from rec_1.hea of each person
    def extract_labels(self, filepath):
        folders = sorted(os.listdir(filepath))
        for folder in folders:
            if folder.startswith('Person_'):
                self.person_labels.append(folder)
                for file in os.listdir(os.path.join(filepath, folder)):
                    if file.startswith('rec_1.') and file.endswith('hea'):
                        with open(os.path.join(filepath, folder, file), 'r') as f:
                            array2d = [[str(token) for token in line.split()] for line in f]
                            self.age_labels.append(array2d[4][2])
                            self.gender_labels.append(array2d[5][2])
                            self.date_labels.append(array2d[6][3])
                        f.close()

    # Extracts features from csv file of each person
    def extract_features(self, filepath):
        p = 0  # person counter
        folders = sorted(os.listdir(filepath))
        for folder in folders:
            if folder.startswith('Person_'):
                p = p + 1
                files = sorted(os.listdir(os.path.join(filepath, folder)))
                f = 0  # file counter
                for file in files:
                    if file.endswith('csv'):
                        with open(os.path.join(filepath, folder, file), 'r') as x:
                            f = f + 1
                            # read csv data for each person from col=1 (filtered sig)
                            features = pd.read_csv(x)
                            temp = [p]  # person ID index
                            filteredData = []
                            segmentedData = []

                            for row in range(len(features)):
                                filteredData.append(features.iat[row, 1])

                            segmented = segment(filteredData)

                            for lst in segmented:
                                for e in lst:
                                    temp.append(e)
                                    segmentedData.append(e)

                            sigToImage(np.array(segmentedData), p, f)
                            fourierSpectrogram(np.array(segmentedData), p, f)

                            tempNP = np.asarray(temp, dtype=float)
                            if tempNP.shape == (9999,):
                                tempNP = np.append(tempNP, tempNP[9998])

                            self.dump_features(tempNP)

    # Append to a bigger global array
    def dump_features(self, array):
        filteredDF = pd.DataFrame(array)
        filteredDF = filteredDF.T
        self.filtered_signal = self.filtered_signal.append(filteredDF, ignore_index=True)


# ---------------- Driver ---------------- #
# GetData()
ProcessData()
