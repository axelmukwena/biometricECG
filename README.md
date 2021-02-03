# Biometric ECG
---
##### 01 Nov, 2020
An ECG based biometric model for specialised authentication systems
- Confusion matrix [[1][1]]
- PhysioNet Tutorials [[2][2]] | Just dicovered this dataset: http://thew-project.org/database/e-hol-03-0202-003.html
- How to exctract ECG Features [[3][3]]
- ECG **R-peak segmentation** algorithm, Christov Segmenter [[4][4]] and [[5][5]]
- A Comparison of 1-D and 2-D Deep Convolutional Neural Networks
in ECG Classification [[6][6]]
    - ![Comparison of ECG beat classification methods on MIT-BIH arrhythmia database](media/2d_1d_comparison.png)
- Convert ECG signal to a 2D image. However, there are certain **drawbacks and limitations** of using such deep CNNs. [[7][7]]
    - To start with it is known that they pose a **high computational complexity** that requires special hardware especially for training. 
    - Hence, 2D CNNs are **not suitable for real-time applications** on mobile and low-power/low-memory devices.
	- Moreover, proper training of deep CNNs **requires a very large training dataset** in order to achieve a reasonable generalization capability. 
	- This may not be a viable option for many practical 1D signal applications where the labeled data is scarce.
-   2D time-frequency spectrograms using short-time Fourier transform (STFT)?
- Apply cropping??
- Do I select equal number ecg record files per person?
- Image size?? https://arxiv.org/abs/2005.06902

### Notable sources & Readings
1. https://github.com/hiredd/DeepECG-1
1. https://github.com/SoufianeDataFan/ECG-authentificate
1. https://leejunhyun.github.io/project/biomedical/2018/09/25/ECG-biometrics/
1. https://github.com/ankur219/ECG-Arrhythmia-classification
	1. https://medium.com/datadriveninvestor/ecg-arrhythmia-classification-using-a-2-d-convolutional-neural-network-33aa586bad67
	2. https://arxiv.org/pdf/1804.06812.pdf
1. https://github.com/lorenzobrusco/ECGNeuralNetwork
1. Great source for fourier transformations:  https://github.com/gogowenzhang/ECG_Detector 

---
##### 18 Nov, 2020

1. Quality > Quantity of data: https://news.voyage.auto/active-learning-and-why-not-all-data-is-created-equal-8a43a758c6f9
1. Imbalance in datasets: https://towardsdatascience.com/handling-imbalanced-datasets-in-machine-learning-7a0e84220f28
    - https://towardsdatascience.com/deep-learning-unbalanced-training-data-solve-it-like-this-6c528e9efea6
    - When out of all the classes which you want to predict if for one or more classes there are extremely low number of samples you may be facing a problem of unbalanced classes in your data.
1. Transforming an inertial sensor signal into images with large dimensions will allow a CNN to infer many correlations among dimensions, allowing it to extract detailed features and rich information from the original signal.
    - https://www.ncbi.nlm.nih.gov/pmc/articles/PMC6263516/


---
##### 18 Dec, 2020
1. How to augment signal images for cnn https://link.springer.com/chapter/10.1007/978-3-319-73600-6_8
1. What is the [minimum sample size](https://www.researchgate.net/post/What_is_the_minimum_sample_size_required_to_train_a_Deep_Learning_model-CNN) required to train a Deep Learning model - CNN?
1. Why it's necessary to have [equal classes](https://www.cs.cmu.edu/afs/cs/project/jair/pub/volume16/chawla02a-html/chawla2002.html)
1. How to [augment sound](https://medium.com/@keur.plkar/audio-data-augmentation-in-python-a91600613e47) + [code](https://gist.github.com/keyurparalkar/5a49f696ed36ddce6526ab50e29e04ce)

---
##### 29 Dec, 2020
1. Setup Dataset [scikit-learn tutorial](https://kapernikov.com/tutorial-image-classification-with-scikit-learn/)
1. Setup and [Keras generator](https://machinelearningmastery.com/how-to-load-large-datasets-from-directories-for-deep-learning-with-keras/)
1. How to divide data for training, validation and testing [here](https://glassboxmedicine.com/2019/09/15/best-use-of-train-val-test-splits-with-tips-for-medical-data/)

##### 02 Jan, 2021
1. [How][8] to add a new class to a deep learning model?
1. [Discussion][9] on incremental learning for the model?? Check [this][10] paper

[1]: <https://www.dataschool.io/simple-guide-to-confusion-matrix-terminology/#:~:text=A%20confusion%20matrix%20is%20a,related%20terminology%20can%20be%20confusing.>
[2]: <https://archive.physionet.org/tutorials/creating-records.shtml>
[3]: <https://ieeexplore.ieee.org/document/6950168>
[4]: <https://biosppy.readthedocs.io/en/stable/biosppy.signals.html>
[5]: <https://www.semanticscholar.org/paper/Review-and-Comparison-of-Real-Time-Segmentation-for-Canento-Louren%C3%A7o/788a62ac3567e7793cfdf14aae65296101c43042>
[6]: <https://arxiv.org/pdf/1810.07088.pdf>
[7]: <https://ieeexplore.ieee.org/document/8682194>
[8]: <https://datascience.stackexchange.com/questions/15656/how-to-add-a-new-category-to-a-deep-learning-model/57189#57189>
[9]: <https://www.researchgate.net/post/What-are-the-techniques-for-incremental-training-of-Convolutional-Neural-Networks-without-doing-full-training-as-new-classes-are-added-to-data>
[10]: <https://arxiv.org/abs/1807.09536>
