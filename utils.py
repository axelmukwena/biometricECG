# Author: Axel Mukwena
# ECG Biometric Authentication using Siamese and CNN

# import the necessary packages
import pickle
import random

import numpy as np


def load_data(where):
    pickleIn = open(where, 'rb')
    ps, yy, xx = pickle.load(pickleIn)
    yy = np.array(yy)
    xx = np.array(xx)

    # Normalize data
    print('Before Normalizing: Min:', xx.min(), 'Max:', xx.max(), "\n")
    xx = (xx - xx.min()) / (xx.max() - xx.min())
    print('After Normalizing: Min:', xx.min(), 'Max:', xx.max(), "\n")

    return yy, xx, ps


# Shuffle data
def shuffle(yy, xx):
    length = len(yy)
    data = []
    for i in range(length):
        data.append([yy[i], xx[i]])

    num = random.randint(0, length)
    random.seed(num)
    random.shuffle(data)

    yy, xx = [], []
    for k in range(length):
        yy.append(data[k][0])
        xx.append(data[k][1])

    xx = np.array(xx)
    yy = np.array(yy)

    return yy, xx
