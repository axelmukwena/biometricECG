# Author: Axel Mukwena
# ECG Biometric Authentication using CNN

# Data Processing: Converts .dat files
# to .csv and generates annotated dataset
from biosppy.signals import ecg
import matplotlib.pyplot as plt
import pandas as pd
import numpy as np
import wfdb
import os


# Sampling rate for ECG-ID is 500 Hz

# ************************************ Get data ************************************

# Get data, convert .dat to .csv files
class GetData:
    def __init__(self):
        self.dir = os.path.join(os.getcwd(), 'data')
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
        self.dir = os.path.join(os.getcwd(), 'data')
        self.id = []
        self.person_labels = []
        self.age_labels = []
        self.gender_labels = []
        self.date_labels = []
        self.processedData = {}
        self.totalRecords = 0

        print('Setting up data labels...')
        self.extract_labels()
        ecglabels = [list(i) for i in zip(self.id, self.person_labels, self.age_labels,
                                          self.gender_labels, self.date_labels)]

        print('Exporting labels to csv...')
        ecglabelsDF = pd.DataFrame(ecglabels)
        ecglabelsDF.to_csv(os.path.join('processedData', 'ecglabels.csv'), index=False)
        print('Export complete.')

        print('\nSetting up and exporting processed data...')
        self.extract_features()
        self.processedDataDF = pd.DataFrame(self.processedData)
        self.processedDataDF = self.processedDataDF.T
        self.processedDataDF.to_csv(os.path.join('processedData', 'processedData.csv'), index=False)

    # Extracts labels and features from rec_1.hea of each person
    def extract_labels(self):
        p = 0
        folders = sorted(os.listdir(self.dir))
        for folder in folders:
            if folder.startswith('Person_'):
                p += 1
                self.id.append(p)
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
        p = 0  # person counter
        folders = sorted(os.listdir(self.dir))
        for folder in folders:
            if folder.startswith('Person_'):
                p = p + 1
                files = sorted(os.listdir(os.path.join(self.dir, folder)))
                f = 0  # file counter
                for file in files:
                    if (file.startswith('rec_1.') or file.startswith('rec_2.')) and file.endswith('csv'):
                        with open(os.path.join(self.dir, folder, file), 'r') as x:
                            f = f + 1
                            # read csv data for each person from col=1 (filtered sig)
                            features = pd.read_csv(x)
                            filteredData = []
                            for row in range(len(features)):
                                filteredData.append(features.iat[row, 1])

                            self.segment(np.asarray(filteredData), p, f)
                print('Person ' + str(p))

    # ECG R-peak segmentation
    def segment(self, array, p, f):
        count = 1
        signals = []
        peaks = ecg.christov_segmenter(signal=array, sampling_rate=500)[0]
        for i in (peaks[1:-1]):
            diff1 = abs(peaks[count - 1] - i)
            diff2 = abs(peaks[count + 1] - i)
            x = peaks[count - 1] + diff1 // 2
            y = peaks[count + 1] - diff2 // 2
            sig = array[x:y]
            self.sigToImage(sig, p, f, count)
            signals.append(sig)
            count += 1

    # Convert segmented signals into grayscale images, nparray
    def sigToImage(self, array, person, record, rPeak):
        fig = plt.figure(frameon=False)  # plt.figure(figsize=(20, 4))
        plt.plot(array, color='gray')
        plt.xticks([]), plt.yticks([])
        for spine in plt.gca().spines.values():
            spine.set_visible(False)

        folder = 'processedData/images/' + f'{person:03}' + '/'
        if not os.path.exists(folder):
            os.makedirs(folder)
        filename = folder + f'{person:03}' + '_' + f'{record:03}' + '_' + f'{rPeak:03}' + '.png'
        fig.savefig(filename)
        plt.cla()
        plt.clf()
        plt.close('all')

        # Add segment details to global record dictionary
        self.totalRecords += 1
        self.processedData[self.totalRecords] = person, filename


# Data Augmentation
class Augmentation:
    def __init__(self):
        pass

# ---------------- Driver ---------------- #
# GetData()
# ProcessData()
