# Author: Axel Mukwena
# ECG Biometric Authentication using CNN

# Data Processing: Converts .dat files
# to .csv and generates annotated dataset

import os
import numpy as np
import pandas as pd
import wfdb


# Get data, convert .dat to .csv files
class GetData:
    def __init__(self):
        self.dir = os.path.join(os.getcwd(), 'data')
        self.database = 'ecgiddb'

        # crawls into every folder and sends .dat file to constructor
        print('Converting to csv')
        for folders in os.listdir(self.dir):
            if folders.startswith('Person_'):
                for record in os.listdir(os.path.join(self.dir, folders)):
                    if record.endswith('dat'):
                        basename = record.split('.', 1)[0]
                        self.constructor(folders, basename)

    def constructor(self, folder, filename):
        signals, fields = wfdb.rdsamp(filename, sampfrom=0,
                                      pn_dir=os.path.join(self.database, folder))
        df = pd.DataFrame(signals)
        df.to_csv(os.path.join(self.dir, folder, filename + '.' 'csv'), index=False)


# Generates features and labels
class ProcessData:
    def __init__(self):
        self.dir = os.path.join(os.getcwd(), 'data')
        self.person_labels = []
        self.age_labels = []
        self.gender_labels = []
        self.date_labels = []
        self.filtered_signal = pd.DataFrame()
        self.unfiltered_signal = pd.DataFrame()

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
        self.unfiltered_signal.to_csv(os.path.join('processedData', 'unfiltereddata.csv'), index=False)
        print('\nExport complete.')

        if os.path.isfile(os.path.join('processedData', 'filtereddata' + '.' + 'csv')) \
                and os.path.isfile(os.path.join('processedData', 'unfiltereddata' + '.' + 'csv')):
            print('Data in processedData/ is now ready for training')

    # Extracts labels and features from rec_1.hea of each person
    def extract_labels(self, filepath):
        for folders in os.listdir(filepath):
            if folders.startswith('Person_'):
                self.person_labels.append(folders)
                for file in os.listdir(os.path.join(filepath, folders)):
                    if file.startswith('rec_1.') and file.endswith('hea'):
                        with open(os.path.join(filepath, folders, file), 'r') as f:
                            array2d = [[str(token) for token in line.split()] for line in f]
                            self.age_labels.append(array2d[4][2])
                            self.gender_labels.append(array2d[5][2])
                            self.date_labels.append(array2d[6][3])
                        f.close()

    # Extracts features from rec_1.csv of each person
    def extract_features(self, filepath):
        p = 0  # person counter
        f = 0  # file counter
        for folders in os.listdir(filepath):
            if folders.startswith('Person_'):
                p = p + 1
                for file in os.listdir(os.path.join(filepath, folders)):
                    if file.endswith('csv'):
                        with open(os.path.join(filepath, folders, file), 'r') as x:
                            f = f + 1
                            features = pd.read_csv(x, header=[0, 1])
                            featuresDF = pd.DataFrame(features)
                            featuresDF = featuresDF.apply(pd.to_numeric)
                            temp = [p]
                            temp1 = [p]

                            for row in range(len(featuresDF)):
                                temp.append(featuresDF.iat[row, 1])
                                temp1.append(featuresDF.iat[row, 0])
                            tempnp = np.asarray(temp, dtype=float)
                            if tempnp.shape == (9999,):
                                tempnp = np.append(tempnp, tempnp[9998])
                            temp1np = np.asarray(temp1, dtype=float)
                            if temp1np.shape == (9999,):
                                temp1np = np.append(temp1np, tempnp[9998])
                            self.dump_features(tempnp, 0)
                            self.dump_features(temp1np, 1)
                        x.close()
        print('Number of files features extracted: ', f)

    # Append to a bigger global array
    def dump_features(self, array, flag):
        if not flag:
            filteredDF = pd.DataFrame(array)
            filteredDF = filteredDF.T
            self.filtered_signal = self.filtered_signal.append(filteredDF, ignore_index=True)
        if flag:
            unfilteredDF = pd.DataFrame(array)
            unfilteredDF = unfilteredDF.T
            self.unfiltered_signal = self.unfiltered_signal.append(unfilteredDF, ignore_index=True)


# Aligns dataset by first max peak
class Augmentation:
    def __init__(self):
        self.maxs = []


# ---------------- Driver ---------------- #
GetData()
ProcessData()
