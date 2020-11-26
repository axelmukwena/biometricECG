import os
import pandas as pd
import numpy as np
import matplotlib.pyplot as plt


class CountRecords:
    def __init__(self):
        self.dir = os.path.join(os.getcwd(), os.path.expanduser('~/drive/projects/ml/biometricECG/processedData/images'))
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
            if folder.startswith('0'):
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


CountRecords()
