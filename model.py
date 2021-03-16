# Author: Axel Mukwena
# ECG Biometric Authentication using CNN
# Model: Load and construct model to train data

from sklearn.model_selection import train_test_split
from tensorflow.keras.layers import Conv2D, MaxPooling2D
from tensorflow.keras.layers import Dense, Flatten
from tensorflow.keras.models import Sequential
import tensorflow as tf
import numpy as np
import pickle

np.random.seed(123) 

# load dataset
pickleIn = open('data480x480.pickle', 'rb')
categories, y, x = pickle.load(pickleIn)

x = np.array(x).reshape([-1, 480, 480, 1])
y = np.array(y)

# split data
train, validation, test = 0.7, 0.15, 0.15

# train 70% since test is 30%
x_train, x_test, y_train, y_test = train_test_split(
    x, y, test_size=1 - train, shuffle=True, random_state=42)

# test 15%, validation 15%
x_val, x_test, y_val, y_test = train_test_split(
    x_test, y_test, test_size=test / (test + validation), random_state=3)

# normalize data
x_train = tf.keras.utils.normalize(x_train, axis=1)
x_val = tf.keras.utils.normalize(x_val, axis=1)
x_test = tf.keras.utils.normalize(x_test, axis=1)
print('Min:', x.min(), 'Max:', x.max())

# building the model
model = Sequential()

model.add(Conv2D(256, (3, 3), activation='relu', input_shape=x_train.shape[1:]))
model.add(MaxPooling2D(pool_size=(2, 2)))

model.add(Conv2D(256, (3, 3), activation='relu'))
model.add(MaxPooling2D(pool_size=(2, 2)))

model.add(Flatten())
model.add(Dense(64, activation='relu'))
model.add(Dense(11, activation='softmax'))

# training the model
model.compile(loss='sparse_categorical_crossentropy', optimizer='adam', metrics=['accuracy'])

# fitting the model
model.fit(x_train, y_train, batch_size=32, epochs=5,
          verbose=1, validation_data=(x_val, y_val),)

val_loss, val_acc = model.evaluate(x_val, y_val)
print('Loss:', val_loss, 'Acc:', val_acc)
