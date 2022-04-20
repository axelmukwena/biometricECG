# Author: Axel Mukwena
# ECG Biometric Authentication using CNN

import os

from scipy.signal import filtfilt
import pandas as pd
import numpy as np
import wfdb


def filters(array, n):
    # the larger n is, the smoother curve will be
    b = [1.0 / n] * n
    a = 1
    array = filtfilt(b, a, array, padlen=50)
    return array


# Sampling rate, sr for MIT-BIH is 360 Hz
# Get data, convert .dat to .csv files
def constructor(directory, filename, db):
    signals, fields = wfdb.rdsamp(os.path.join(directory, filename))
    # Read from online Physionet Dataset
    # signals, fields = wfdb.rdsamp(filename, pn_dir=db)
    a = [m[0] for m in signals]  # get filtered signals

    df = pd.DataFrame(a)
    df.to_csv(os.path.join(directory, filename + '.' 'csv'), index=False)


class GetSignals:
    def __init__(self):
        self.mit_dir = os.path.expanduser("data/raw/mit/")
        self.bmd_dir = os.path.expanduser("data/raw/bmd101/")
        self.ecg_id = os.path.expanduser("data/raw/ecgid/")
        self.mitdb = 'mitdb'
        self.ecgiddb = 'ecgiddb'

    def mit(self, people):
        # crawls into every folder and sends .dat file to constructor
        print('Converting to .dat to .csv...')
        files = sorted(os.listdir(self.mit_dir))
        print(len(files), " files found.\n")
        for file in files:
            if file.endswith('.dat') and file.replace(".dat", "") in people:
                basename = file.split('.')[0]
                constructor(self.mit_dir, basename, self.mitdb)
                print('Person ' + basename)

    def bmd(self, people):
        folders = sorted(os.listdir(self.bmd_dir + "/raw/"))
        for folder in folders:
            if not folder.startswith('.') and folder in people:
                files = sorted(os.listdir(os.path.join(self.bmd_dir + "/raw/", folder)))
                print(len(files), " files found.\n")
                for file in files:
                    if file.startswith('ECGLog'):
                        name = self.bmd_dir + "/raw/" + folder + "/" + file
                        count = 0
                        array = []
                        with open(name, 'r') as f:
                            for line in f:
                                count += 1
                                if count == 1:
                                    continue
                                value = int(line.strip().split()[1])
                                array.append(value)

                        array = np.array(array, dtype="float32")
                        array = np.interp(array, (array.min(), array.max()), (-1, +1))
                        array = np.array(array, dtype="float32")
                        unfiltered = array[:]

                        df = pd.DataFrame()
                        df["0"] = array[:]
                        df.to_csv(self.bmd_dir + "csv/" + folder + '.' 'csv', index=False)
                        print("Person:", str(folder))

    def ecgid(self, people):

        # crawls into every folder and sends .dat file to constructor
        print('Converting to .dat to .csv...')
        folders = sorted(os.listdir(self.ecg_id))
        count = 0
        for folder in folders:
            if folder.startswith('Person_') and folder.replace("Person_", "") in people:
                records = sorted(os.listdir(os.path.join(self.ecg_id, folder)))
                print(len(records), " records found.\n")
                for record in records:
                    # only get the first 2 records for all people to have equal weights
                    if record.startswith('rec_1.dat') or record.startswith('rec_2.dat'):
                        basename = record.split('.', 1)[0]
                        constructor(self.ecg_id + folder, basename, self.ecgiddb)
                count += 1
                print('Person ' + str(count))
