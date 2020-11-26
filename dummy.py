import matplotlib.colors as colors
import matplotlib.pyplot as plt
from biosppy.signals import ecg
from scipy import signal
from PIL import Image
import pandas as pd
import numpy as np
import cv2
import os


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
        sigToImage(sig, p, count)
        signals.append(sig)
        count += 1
    return signals


def sigToImage(array, person, record):

    fig = plt.figure(frameon=False)
    plt.plot(array, color='gray')
    plt.xticks([]), plt.yticks([])
    for spine in plt.gca().spines.values():
        spine.set_visible(False)
    pp = 'processedData/dummy/' + str(person) + '/peak2/'
    if not os.path.exists(pp):
        os.makedirs(pp)
    # filename = pp + str(record) + 'unseg' + '.png'
    filename = pp + str(record) + '.png'
    fig.savefig(filename)

    # img = cv2.imread(filename, cv2.IMREAD_GRAYSCALE)
    # img = cv2.resize(img, (128, 128), interpolation=cv2.INTER_LANCZOS4)
    # cv2.imwrite(filename, gray)
    plt.cla()
    plt.clf()
    plt.close('all')


# To spectrogram
def toSpectrogram(array):
    f, t, Sxx = signal.spectrogram(array, fs=500, nperseg=64)
    return Sxx.T


def fourierSpectrogram(array, person, record):
    f, t, Sxx = signal.spectrogram(array)

    fig = plt.figure(frameon=False)
    plt.pcolormesh(t, f, Sxx, shading='gouraud', cmap='Greys')

    pp = 'processedData/dummy/spec/' + str(person) + '/'
    if not os.path.exists(pp):
        os.makedirs(pp)
    filename = pp + str(record) + 'unseg' + '.png'
    # filename = pp + str(record) + '.png'

    fig.savefig(filename)
    plt.cla()
    plt.clf()
    plt.close('all')


p = 2
fileCount = 1
with open('data/Person_02/rec_2.csv', 'r') as file:
    # read csv data for each person from col=1 (filtered sig)
    features = pd.read_csv(file)
    temp = [p]  # person ID index
    filteredData = []
    segmentedData = []

    for row in range(len(features)):
        filteredData.append(features.iat[row, 1])

    segmented = segment(filteredData)

    for lst in segmented:
        for e in lst:
            segmentedData.append(e)

    # sigToImage(np.array(filteredData), p, fileCount)
    # fourierSpectrogram(np.array(filteredData), p, fileCount)
    '''
    unSeg = pd.DataFrame(np.array(filteredData))
    unSeg.to_csv(os.path.join('processedData/dummy', 'unSegData.csv'), index=False)

    seg = pd.DataFrame(np.array(segmentedData))
    seg.to_csv(os.path.join('processedData/dummy', 'segData.csv'), index=False)
    '''
    tempNP = np.asarray(temp, dtype=float)
    if tempNP.shape == (9999,):
        tempNP = np.append(tempNP, tempNP[9998])
