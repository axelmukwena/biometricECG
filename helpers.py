import os
import pandas as pd
import matplotlib.pyplot as plt
import numpy as np


class CountRecords:
    def __init__(self):
        self.dir = os.path.join(os.getcwd(),
                                os.path.expanduser('~/drive/projects/ml/biometricECG/processedData/images'))
        self.records = {}
        self.recordsArray = []

        self.count()
        print(self.records)
        print(self.recordsArray)
        self.graph()
        self.records = pd.DataFrame(self.records, index=[0])
        self.records = self.records.T
        self.records.to_csv(os.path.join('processedData', 'recordCount.csv'))

    def count(self):
        p = 0
        folders = sorted(os.listdir(self.dir))
        for folder in folders:
            p += 1
            r = 0
            records = sorted(os.listdir(os.path.join(self.dir, folder)))
            for record in records:
                if record.endswith('png'):
                    r += 1
            self.records[p] = r
            self.recordsArray.append(r)

    def graph(self):
        unique, counts = np.unique(np.asarray(self.recordsArray), return_counts=True)
        fig = plt.figure(frameon=True)  # frameon = background
        plt.title = 'image data per person'
        circle = plt.Circle((0, 0), 0.7, color='white')
        plt.pie(counts, labels=unique, autopct='%1.1f%%')
        p = plt.gcf()
        p.gca().add_artist(circle)
        folder = 'media/'
        if not os.path.exists(folder):
            os.makedirs(folder)
        filename = folder + 'processedDataDistribution.png'
        fig.savefig(filename)


# calculate counts per type and sort, to ensure their order
def count(y):
    unique, counts = np.unique(y, return_counts=True)
    sorted_index = np.argsort(unique)
    counts = counts[sorted_index]
    return counts


# Plot bar to show distribution of data
def plotBar(y, a, b, c):
    labels = np.unique(y)
    width = 0.20  # the width of the bars

    aCounts = count(a)
    bCounts = count(b)
    cCounts = count(c)

    x = np.arange(len(labels))  # the label locations

    # Set position of bar on X axis
    aPos = x
    bPos = [i + width for i in aPos]
    cPos = [i + width for i in bPos]

    fig, ax = plt.subplots()
    ax.bar(aPos, aCounts, width, edgecolor='white', label='train')
    ax.bar(bPos, bCounts, width, edgecolor='white', label='validate')
    ax.bar(cPos, cCounts, width, edgecolor='white', label='test')

    # Add some text for labels, title and custom x-axis tick labels, etc.
    ax.set_ylabel('counts')
    ax.set_title('relative amount of photos per class')
    ax.set_xticks(x)
    ax.set_xticklabels(labels)
    ax.legend(['train ({0} photos)'.format(len(a)),
               'val ({0} photos)'.format(len(b)),
               'test ({0} photos)'.format(len(c))])

    fig.tight_layout()
    plt.show()

    folder = 'media/'
    if not os.path.exists(folder):
        os.makedirs(folder)
    filename = folder + 'DataSplitDistribution.png'
    fig.savefig(filename)


# CountRecords()
