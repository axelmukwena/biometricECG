# biometricECG
An ECG based biometric model for specialised authentication systems
- [Confusion matrix][1]
- [PhysioNet Tutorials][2]
- [How to exctract ECG Features][3]
- [ECG R-peak segmentation algorithm, Christov Segmenter][4]
- - [A Comparison of 1-D and 2-D Deep Convolutional Neural Networks
in ECG Classification][6]
    - ![Comparison of ECG beat classification methods on MIT-BIH arrhythmia database](media/2d_1d_comparison.png)
- Use power- or log-spectrogram to convert each ECG beat to a 2D image. However, there are certain **drawbacks and limitations** of using such deep CNNs. 
    - To start with it is known that they pose a **high computational complexity** that requires special hardware especially for training. 
    - Hence, 2D CNNs are **not suitable for real-time applications** on mobile and low-power/low-memory devices. 
	- Moreover, proper training of deep CNNs **requires a very large training dataset** in order to achieve a reasonable generalization capability. 
	- This may not be a viable option for many practical 1D signal applications where the labeled data is scarce.[5][5]
- Apply cropping??
- Do I select equal number ecg record files per person?

### Notable sources & Readings
1. https://github.com/hiredd/DeepECG-1
1. https://github.com/SoufianeDataFan/ECG-authentificate
1. https://leejunhyun.github.io/project/biomedical/2018/09/25/ECG-biometrics/
1. https://github.com/ankur219/ECG-Arrhythmia-classification
	1. https://medium.com/datadriveninvestor/ecg-arrhythmia-classification-using-a-2-d-convolutional-neural-network-33aa586bad67
	2. https://arxiv.org/pdf/1804.06812.pdf
1. This >>> https://github.com/lorenzobrusco/ECGNeuralNetwork
1. https://github.com/gogowenzhang/ECG_Detector | Great source for fourier transformations

[1]: <https://www.dataschool.io/simple-guide-to-confusion-matrix-terminology/#:~:text=A%20confusion%20matrix%20is%20a,related%20terminology%20can%20be%20confusing.>
[2]: <https://archive.physionet.org/tutorials/creating-records.shtml>
[3]: <https://ieeexplore.ieee.org/document/6950168>
[4]: <https://biosppy.readthedocs.io/en/stable/biosppy.signals.html>
[5]: <https://ieeexplore.ieee.org/document/8682194>
[6]: <https://arxiv.org/pdf/1810.07088.pdf>