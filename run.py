# Author: Axel Mukwena
# ECG Biometric Authentication using CNN

import argparse
from signals import GetSignals
from features import GetFeatures
from setup import Setup
import snn
import cnn

mit = ['100', '101', '102', '103', '104', '105', '106', '107', '108', '109',
       '111', '112', '113', '114', '115', '116', '117', '118', '119', '121',
       '122', '123', '124', '200', '201', '202', '203', '205', '207', '208',
       '209', '210', '212', '213', '214', '215', '217', '219', '220', '221',
       '222', '223', '228', '230', '231', '232', '233', '234']

# exclude person 74
ecgid = ['01', '02', '03', '04', '05', '06', '07', '08', '09', '10',
         '11', '12', '13', '14', '15', '16', '17', '18', '19', '20',
         '21', '22', '23', '24', '25', '26', '27', '28', '29', '30',
         '31', '32', '33', '34', '35', '36', '37', '38', '39', '40',
         '41', '42', '43', '44', '45', '46', '47', '48', '49', '50',
         '51', '52', '53', '54', '55', '56', '57', '58', '59', '60',
         '61', '62', '63', '64', '65', '66', '67', '68', '69', '70',
         '71', '72', '73', '75', '76', '77', '78', '79', '80',
         '81', '82', '83', '84', '85', '86', '87', '88', '89', '90']

bmd101 = ["1975", "1973"]


def main():
    if arg.signals_mit:
        try:
            gs = GetSignals()
            gs.mit(mit)
        except Exception as e:
            print(e)

    elif arg.signals_ecgid:
        try:
            gs = GetSignals()
            gs.ecgid(ecgid)
        except Exception as e:
            print(e)
    elif arg.signals_bmd:
        try:
            gs = GetSignals()
            gs.bmd(bmd101)
        except Exception as e:
            print(e)

    elif arg.features_mit:
        try:
            feats = GetFeatures()
            feats.features('mit', mit)
        except Exception as e:
            print(e)
    elif arg.features_ecgid:
        try:
            feats = GetFeatures()
            feats.features('ecgid', ecgid)
        except Exception as e:
            print(e)
    elif arg.features_bmd:
        try:
            feats = GetFeatures()
            feats.features('bmd', bmd101)
        except Exception as e:
            print(e)

    elif arg.setup:
        try:
            su = Setup()
            su.load_signals(2500, "cnn", mit[:40], 0)
            su.load_signals(2500, "snn", mit[:40], 0)
        except Exception as e:
            print(e)

    elif arg.snn:
        try:
            snn.main()
        except Exception as e:
            print(e)
    elif arg.cnn:
        try:
            cnn.main()
        except Exception as e:
            print(e)


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('-s-mit', '--signals_mit', nargs='?', const=True, default=False)
    parser.add_argument('-s-ecgid', '--signals_ecgid', nargs='?', const=True, default=False)
    parser.add_argument('-s-bmd', '--signals_bmd', nargs='?', const=True, default=False)

    parser.add_argument('-f-mit', '--features_mit', nargs='?', const=True, default=False)
    parser.add_argument('-f-ecgid', '--features_ecgid', nargs='?', const=True, default=False)
    parser.add_argument('-f-bmd', '--features_bmd', nargs='?', const=True, default=False)

    parser.add_argument('-setup', '--setup', nargs='?', const=True, default=False)

    parser.add_argument('-snn', '--snn', nargs='?', const=True, default=False)
    parser.add_argument('-cnn', '--cnn', nargs='?', const=True, default=False)

    arg = parser.parse_args()

    main()

