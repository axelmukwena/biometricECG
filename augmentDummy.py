import os

import librosa
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from biosppy.signals import ecg


# ECG R-peak segmentation
def segment(array, p, f, augmentType):
    count = 1
    signals = []
    peaks = ecg.christov_segmenter(signal=array, sampling_rate=500)[0]
    for i in (peaks[1:-1]):
        diff1 = abs(peaks[count - 1] - i)
        diff2 = abs(peaks[count + 1] - i)
        x = peaks[count - 1] + diff1 // 2
        y = peaks[count + 1] - diff2 // 2
        sig = array[x:y]
        sigToImage(sig, p, f, augmentType, count)
        signals.append(sig)
        count += 1


# Convert segmented signals into grayscale images, nparray
def sigToImage(array, p, f, augmentType, peakCount):
    fig = plt.figure(frameon=False)  # plt.figure(figsize=(20, 4))
    plt.plot(array, color='gray')
    plt.xticks([]), plt.yticks([])
    for spine in plt.gca().spines.values():
        spine.set_visible(False)

    folder = 'processedData/augmentDummy/' + f'{p:02}' + '/'
    if not os.path.exists(folder):
        os.makedirs(folder)
    filename = folder + f'{p:02}' + '_' + f'{f:02}' + '_' + f'{augmentType:02}' + '_' + f'{peakCount:02}' + '.png'
    fig.savefig(filename)
    plt.cla()
    plt.clf()
    plt.close('all')


def augment(original, p, f):
    segment(original, p, f, 1)

    # Noise addition using normal distribution with mean = 0 and std =1
    # Permissible noise factor value = x > 0.004
    noiseAdding = original + 0.009 * np.random.normal(0, 1, len(original))
    segment(noiseAdding, p, f, 2)

    # Permissible factor values = samplingRate / 10
    timeShifting = np.roll(original, int(500 / 10))
    segment(timeShifting, p, f, 3)

    # Permissible factor values = -5 <= x <= 5
    pitchShifting = librosa.effects.pitch_shift(original, 500, n_steps=-5.0)
    segment(pitchShifting, p, f, 4)

    # Permissible factor values = 0 < x < 1.0
    factor = 0.99999  # Yields the best reults without losing ecg wave shape
    timeStretching = librosa.effects.time_stretch(original, factor)
    segment(timeStretching, p, f, 5)

    signals = [list(i) for i in zip(original, noiseAdding, timeShifting, pitchShifting,
                                    timeStretching)]

    print('Exporting original + augmented signals to csv...')
    signalsDF = pd.DataFrame(signals)
    signalsDF.to_csv(os.path.join('processedData/augmentDummy', 'signals.csv'), index=False)
    print('Export complete.')


person = 10
fileNum = 2
with open('data/Person_10/rec_2.csv', 'r') as file:
    # read csv data for each person from col=1 (filtered sig)
    features = pd.read_csv(file)
    filteredData = []

    for row in range(len(features)):
        filteredData.append(features.iat[row, 1])

    augment(np.asarray(filteredData), person, fileNum)
