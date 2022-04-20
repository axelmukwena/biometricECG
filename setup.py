# Author: Axel Mukwena
# ECG Biometric Authentication using CNN

import os
import pickle


# Set up data for training and testing
class Setup:
    def __init__(self):
        self.dir = os.path.expanduser("data/ready/")
        self.y, self.x = [], []
        self.name = ''  # train or enroll, if enroll, specify node

    # Load the signals
    def load_signals(self, limit, name, people, trim):
        self.y, self.x = [], []
        self.name = name
        directory = self.dir + "signals/"

        pickles = sorted(os.listdir(directory))
        how = []
        who = []
        for p in pickles:
            if p.endswith('.pickle') and p.replace(".pickle", "") in people:
                count = 0
                basename = p.split('.')[0]

                if int(basename) < 100:
                    limit = 250

                pickleIn = open(os.path.join(directory, p), 'rb')
                waves = pickle.load(pickleIn)
                if trim:
                    waves = waves[trim:]
                for wave in waves:
                    if count >= limit:
                        break
                    count += 1
                    self.y.append(basename)
                    self.x.append(wave)
                how.append(count)
                who.append(basename)
                print("Person:", basename, "|", "x wave length:", len(self.x))

        if who:
            # print(who)
            # print(how)
            print('Saving to pickle... Complete.')
            pickle_out = open('data/ready/pickles/' + str(self.name) + '.pickle', 'wb')
            pickle.dump((people, self.y, self.x), pickle_out)
            pickle_out.close()
        else:
            print("Please make sure feature extraction was performed correctly.")
