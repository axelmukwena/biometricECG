# Author: Axel Mukwena
# ECG Biometric Authentication using CNN

# Data Processing: Converts .dat files 
# to .csv and generates annotated dataset

# from sklearn.model_selection import train_test_split
from itertools import combinations, islice
# import matplotlib.pyplot as plt
from scipy import signal
from numpy import Inf
import pandas as pd
import numpy as np
import wfdb
import math
# import itertools
import os
import sys


# .dat to .csv converter
class CsvConverter:
    def __init__(self):
        self.dir = os.path.join(os.getcwd(), 'data')
        self.database = 'ecgiddb'

    def constructor(self, folder, filename):
        signals, fields = wfdb.rdsamp(filename,
                                      sampfrom=0,
                                      pn_dir=os.path.join(self.database, folder))
        df = pd.DataFrame(signals)
        df.to_csv(os.path.join(self.dir, folder, filename + "." 'csv'), index=False)

    # crawls into every folder and sends .dat file to constructor
    def to_csv(self):
        for folders in os.listdir(self.dir):
            if folders.startswith('Person_'):
                for inPersonDir in os.listdir(os.path.join(self.dir, folders)):
                    if inPersonDir.endswith('dat'):
                        basename = inPersonDir.split(".", 1)[0]
                        self.constructor(folders, basename)


# Generate features and labels
class ProcessData:
    def __init__(self):
        self.dir = os.path.join(os.getcwd(), 'data')
        self.person_labels = []  # Who the person is
        self.age_labels = []
        self.gender_labels = []
        self.date_labels = []  # month.day.year of ecg record
        self.ecg_filtered_sig = pd.DataFrame()  # filtered ecg dataset
        self.ecg_signal = pd.DataFrame()  # unfiltered ecg dataset

    # extracts labels and features from header file of each person
    def extract_labels(self, filepath):
        for folders in os.listdir(filepath):
            if folders.startswith('Person_'):
                self.person_labels.append(folders)
                for inPersonDir in os.listdir(os.path.join(filepath, folders)):
                    if inPersonDir.startswith('rec_1.') and inPersonDir.endswith('hea'):
                        with open(os.path.join(filepath, folders, inPersonDir), "r") as f:
                            array2D = [[str(token) for token in line.split()] for line in f]
                            self.age_labels.append(array2D[4][2])
                            self.gender_labels.append(array2D[5][2])
                            self.date_labels.append(array2D[6][3])
                        f.close()

    # extract features from rec_1.csv of each person
    def extract_features(self, filepath):
        p = 0  # person counter
        fNum = 0  # file counter
        for folders in os.listdir(filepath):
            if folders.startswith('Person_'):
                p += 1
                for files in os.listdir(os.path.join(filepath, folders)):
                    if files.endswith('csv'):
                        with open(os.path.join(filepath, folders, files), "r") as x:
                            fNum += 1
                            features = pd.read_csv(x, header=[0, 1])
                            pdFeatures = pd.DataFrame(features)
                            pdFeatures = pdFeatures.apply(pd.to_numeric)
                            temp = [p]
                            temp1 = [p]
                            for rows in range(len(pdFeatures)):
                                temp.append(pdFeatures.at[rows, 1, True])
                                temp1.append(pdFeatures.at[rows, 0, True])
                                tempNP = np.asarray(temp, dtype=float)
                                if tempNP.shape == (9999,):
                                    tempNP = np.append(tempNP, tempNP[9998])
                                temp1NP = np.asarray(temp1, dtype=float)
                                if temp1NP.shape == (9999,):
                                    temp1NP = np.append(temp1NP, temp1NP[9998])
                                self.dump_features(tempNP, 1)
                                self.dump_features(temp1NP, 2)
                            x.close()

    # Appends to a bigger global array
    def dump_features(self, array, flag):
        filtered_df = pd.DataFrame(array)
        filtered_df = filtered_df.T
        unfiltered_df = pd.DataFrame(array)
        unfiltered_df = unfiltered_df.T
        if flag == 1:
            self.ecg_filtered_sig = self.ecg_filtered_sig.append(filtered_df, ignore_index=True)
        if flag == 2:
            self.ecg_signal = self.ecg_signal.append(unfiltered_df, ignore_index=True)

    def init(self):
        print("Setting up ECG Biometric data labels...")
        self.extract_labels(self.dir)
        ecglabels = [list(i) for i in zip(self.person_labels, self.age_labels,
                                          self.gender_labels, self.date_labels)]
        print("Exporting labels to csv...")
        df_ecglabels = pd.DataFrame(ecglabels)
        df_ecglabels.to_csv(os.path.join('processedAugData', 'ecgDBLabels.csv'), index=False)
        print("Export complete")

        print("Setting up ECG Biometric data features...")
        self.extract_features(self.dir)
        print("Exporting labels to csv...")
        self.ecg_filtered_sig.to_csv(os.path.join('processedAugData', 'filteredECG.csv'), index=False)
        self.ecg_signal.to_csv(os.path.join('processedAugData', 'unFilteredECG.csv'), index=False)
        print("Export complete")

        if os.path.isfile(os.path.join('processedAugData', 'filteredECG' + "." + 'csv')) \
                and os.path.isfile(os.path.join('processedAugData', 'unFilteredECG' + "." + 'csv')):
            print("Data in processedAugData/ folder is now ready for training.")


# Aligns dataset by first max peak
class Augmentation:
    def __init__(self):
        self.maxs = []
        self.mins = []
        self.aligned_data = pd.DataFrame()
        self.peak_rsampled_data = pd.DataFrame()
        self.slice_2500_data = pd.DataFrame()
        self.person_tab = []

    # Helper funtions
    def peak_maxhelper(self, array):
        self.peak_detect(array, 0.5, flag='max')

    # Returns list of max positions
    @staticmethod
    def peaks_per_person(ndnp, row, maxarr):
        return maxarr[row][:, np.r_[0:1]].ravel()

    # Peak detector
    def peak_detect(self, v, delta, flag, x=None):
        maxtab = []
        mintab = []

        if x is None:
            x = np.arange(len(v))
        v = np.asarray(v)
        if len(v) != len(x):
            sys.exit('Input vectors v and x must have the same length')
        if not np.isscalar(delta):
            sys.exit('Input argument delta must be a scalar')
        if delta <= 0:
            sys.exit('Input argument delta must be a positive')

        mn, mx = Inf, -Inf
        mnpos, mxpos = np.NaN, np.NaN

        lookformax = True

        for i in np.arange(len(v)):
            this = v[i]
            if this > mx:
                mx = this
                mxpos = x[i]
            if this < mn:
                mn = this
                mnpos = x[i]
            if lookformax:
                if this < mx - delta:
                    maxtab.append((mxpos, mx))
                    mn = this
                    mnpos = x[i]
                    lookformax = False
            else:
                if this > mn + delta:
                    mintab.append((mnpos, mn))
                    mx = this
                    mxpos = x[i]
                    lookformax = True
            if flag == 'max':
                npmaxtab = np.asarray(maxtab)
                self.maxs.append(npmaxtab)
            if flag == 'min':
                npmintab = np.asarray(mintab)
                self.mins.append(npmintab)

    # Calls slice signal on consequitive pairs of max peaks
    def helper_slice(self, ndnp, personid, row, maxdist, maxarr, maxpos):
        # print row
        for curr_pos, next_pos in zip(maxpos, islice(maxpos, 1, None)):
            self.slice_signal(ndnp, personid, row, maxdist, curr_pos, next_pos)

    # Splits data into peak to peak chunks
    def slice_signal(self, ndnp, personid, row, maxdist, start, stop):
        mv = ndnp[row][:]
        step_back = stop = stop - 1
        chunk = mv[start:step_back]

        # Normal heart rate: 50 to 140 beats per minute
        # Choose 70 as our typical beats per minute
        # 60/70 * 500 = 428.57 ~ 430
        rchunk = signal.resample(chunk, 430)  # Resample chunk
        temp = np.asarray([personid])
        chunktab = np.concatenate((temp, rchunk), axis=0)
        self.gen_dataset(chunk, personid, 'resampled')

    # Aligns all signals by first peak
    def align_data(self, ndnp, personid, row, first_peak):
        mv = ndnp[row][:]
        chunk = mv[first_peak:]
        self.gen_dataset(chunk, personid, 'align')

    # Appends chunk to a global nd array
    def gen_dataset(self, array, personid, flag):
        chunk_df = pd.DataFrame(array)
        chunk_df = chunk_df.T

        if flag == 'align':
            self.aligned_data = self.aligned_data.append(chunk_df, ignore_index=True)
            self.person_tab.append(personid)

        if flag == 'new':
            self.slice_2500_data = self.slice_2500_data.append(chunk_df, ignore_index=True)

        if flag == 'resampled':
            self.peak_rsampled_data = self.peak_rsampled_data.append(chunk_df, ignore_index=True)

    # Distance between two points
    @staticmethod
    def dist(p1, p2):
        (x1, y1), (x2, y2) = p1, p2
        return math.sqrt((x2 - x1) ** 2 + (y2 - y1) ** 2)

    def init(self):
        data = pd.read_csv(os.path.join('processedAugData', 'filteredECG.csv'))
        npdata = np.asarray(data)

        personid = npdata[:, 0]
        signals = npdata[:, np.r_[1:10000]]

        # 1. Detect signal peaks
        np.apply_along_axis(self.peak_maxhelper, 1, signals)
        maxnp = np.asarray(self.maxs)

        # 2. Find distance between max peaks
        # Resample signals by maxdist after slicing
        dfmaxs = pd.DataFrame(maxnp)
        dists = None
        for row in range(len(dfmaxs)):
            dists = [self.dist(p1, p2) for p1, p2 in combinations(dfmaxs.get_value(row, 0, True), 2)]
        npdist = np.asarray(dists)  # List of distances
        maxdist = np.amax(npdist)  # Max distance between two peaks

        # 3. Align dataset by first peak
        print("Generating aligned dataset...")
        for i, row in enumerate(signals):
            amax = np.amax(row)
            if amax < 0.5:
                continue
            max_pos = self.peaks_per_person(signals, i, maxnp)
            first_max = max_pos[0]
            self.align_data(signals, personid[i], i, int(first_max))

        np_aligned = np.asarray(self.aligned_data)

        # 4. Slice the data peak to peak and resample to maintain width/num.of.samples
        slice_resample = True  # Slice peak to peak and resize data
        if slice_resample:
            print("Slicing and resampling data from peak to peak...")
            for i, row in enumerate(signals):
                amax = np.amax(row)  # Get max of signal
                if amax < 0.5:
                    continue
                max_pos1 = self.peaks_per_person(signals, i, maxnp)
                self.helper_slice(signals, personid[i], i, int(maxdist), maxnp, max_pos1.astype(np.int64))

            print("Exporting dataset...")
            path = os.path.join('processedAugData', 'peak_rsampled_data.csv')
            self.peak_rsampled_data.to_csv(path, index=False)
            print("Export complete!")

        # Alternative
        # 4. Slice every 2500 seconds instead of peak to peak. No need to resample
        slice_resample_2500 = False
        if slice_resample_2500:
            print("Slicing data for every 2500 seconds...")
            for i, row in enumerate(np_aligned):
                split_arr = np.array_split(row, 4)
                for j in np.arange(4):
                    this = split_arr[j]
                    if np.isnan(this).any() or np.less(len(this), 2500):
                        continue
                    with_label = np.insert(split_arr[j], 0, self.person_tab[i])  # Prepend personid
                    self.gen_dataset(with_label, self.person_tab[i], 'new')

            print("Exporting dataset...")
            path = os.path.join('processedAugData', 'slice_2500_data.csv')
            self.slice_2500_data.to_csv(path, index=False)
            print("Export complete!")


# Start
# Call methods unless already called
if os.path.isfile(os.path.join('processedAugData', 'filteredECG' + "." + 'csv')) \
        and os.path.isfile(os.path.join('processedAugData', 'unFilteredECG' + "." + 'csv')) \
        and os.path.isfile(os.path.join('processedAugData', 'ecgDBLabels' + "." + 'csv')) \
        and os.path.isfile(os.path.join('processedAugData', 'peak_rsampled_data' + "." + 'csv')) \
        and os.path.isfile(os.path.join('processedAugData', 'slice_2500_data' + "." + 'csv')):
    pass
else:
    print("Started...")
    
    # Convert all .dat files to .csv
    generate = CsvConverter()
    generate.to_csv()

    # Create an aggregated dataset
    processing = ProcessData()
    processing.init()

    # Align data by first max peak
    aligndata = Augmentation()
    aligndata.init()

    print("Ended...")
