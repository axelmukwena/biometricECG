# Author: Axel Mukwena
# ECG Biometric Authentication using Siamese and CNN

# import the necessary packages
import os
import time
from datetime import datetime

import matplotlib.pyplot as plt
import numpy as np
import tensorflow as tf
from sklearn.model_selection import train_test_split
from keras import backend as K
from keras.callbacks import EarlyStopping
from keras.callbacks import ModelCheckpoint
from keras.callbacks import TensorBoard
from keras.layers import Activation
from keras.layers import BatchNormalization
from keras.layers import Conv1D
from keras.layers import Dense
from keras.layers import Dropout
from keras.layers import Input
from keras.layers import Lambda
from keras.layers import MaxPool1D
from keras.models import Model
from tqdm import tqdm

import utils

# initialize the number of epochs to train for, initial learning rate,
# batch size, and image dimensions
EPOCHS = 10
BS = 64
LR = 0.00001
decay = LR / EPOCHS


# ps=people
def create_pairs(y, x, ps):
    yy, xx = [], []

    indices = [np.where(y == i)[0] for i in ps]
    dic = {ps[j]: indices[j] for j in range(len(ps))}
    for i in tqdm(range(len(x))):
        current_image = x[i]
        label = y[i]
        ia = np.random.choice(dic[label], replace=False)
        positive_image = x[ia]
        xx.append([current_image, positive_image])
        yy.append(1)

        choices = np.where(y != label)[0]
        ib = np.random.choice(choices, replace=False)
        negative_image = x[ib]
        xx.append([current_image, negative_image])
        yy.append(0)

    xx = np.array(xx)
    yy = np.array(yy)

    print(len(yy), len(xx))
    print(xx.shape)
    print("First n Y values: ", yy[:5], "\n")
    return xx, yy


# Plot a sample of pairs
def plot_pairs(yy, xx):
    s = [i for i in range(len(yy))]
    fig = plt.figure(figsize=(20, 15))
    for i in range(6):
        plt.subplot(331 + i)
        w = np.random.choice(s)
        plt.title("Label", yy[w])
        plt.plot(xx[w][1])
        plt.plot(xx[w][0])
    fig.tight_layout(pad=2.0)

    name = "media/plots/siamese_pair_samples_" + datetime.now().strftime("%Y%m%d-%H%M%S")
    plt.savefig(name, dpi=300, bbox_inches='tight')

    plt.show()


def splits(yy, xx):
    # train 70%, test is 30%
    xx_train, xx_test, yy_train, yy_test = train_test_split(
        xx, yy, test_size=0.3, shuffle=True, random_state=42)

    xx_valid, xx_test, yy_valid, yy_test = train_test_split(
        xx_test, yy_test, test_size=0.333333, shuffle=True, random_state=42)

    print("X train shape:", xx_train.shape)
    print("X valid shape:", xx_valid.shape)
    print("X test shape:", xx_test.shape, "\n")

    return xx_train, yy_train, xx_valid, yy_valid, xx_test, yy_test,


def block(layer, fs, ks, ps):
    layer = Conv1D(filters=fs, kernel_size=ks, padding="same")(layer)
    layer = BatchNormalization()(layer)
    layer = Activation('relu')(layer)
    layer = MaxPool1D(pool_size=ps, padding='same')(layer)
    layer = Dropout(0.25)(layer)
    return layer


def siamese(input_shape):
    inputs = Input(input_shape)
    layer = block(inputs, 32 * 1, 15, 2)
    layer = block(layer, 32 * 2, 15, 2)
    layer = block(layer, 32 * 4, 15, 2)
    layer = block(layer, 32 * 8, 15, 2)
    last = 1 + 2 + 4 + 8
    layer = block(layer, 32 * last, 15, 2)

    outputs = Dense(32 * last)(layer)

    m = Model(inputs, outputs)
    print(m.summary())
    return m


def euclidean_distance(vectors):
    (featsA, featsB) = vectors
    sum_squared = K.sum(K.square(featsA - featsB), axis=1, keepdims=True)
    return K.sqrt(K.maximum(sum_squared, K.epsilon()))


def contrastive_loss(yy, preds, margin=1):
    yy = tf.cast(yy, preds.dtype)
    squared_preds = K.square(preds)
    squared_margin = K.square(K.maximum(margin - preds, 0))
    loss = 1 - K.mean(yy * squared_preds + (1 - yy) * squared_margin)
    return loss


# plot the loss
def plot_history(h):
    loss = h.history['loss']
    val_loss = h.history['val_loss']

    plt.subplot(2, 1, 2)
    plt.plot(loss, label='Training Loss')
    plt.plot(val_loss, label='Validation Loss')
    plt.legend(loc='upper right')
    plt.ylabel('Cross Entropy')
    plt.ylim([0, 1.0])
    plt.title('Training and Validation Loss')
    plt.xlabel('epoch')

    name = "media/plots/siamese_history_" + datetime.now().strftime("%Y%m%d-%H%M%S")
    plt.savefig(name, dpi=300, bbox_inches='tight')

    plt.show()


def train(folder, sig_dims, data):
    x_train, y_train, x_valid, y_valid, x_test, y_test = data
    if not os.path.exists(folder):
        os.makedirs(folder)

    # Model
    wave_first = Input(shape=sig_dims)
    wave_second = Input(shape=sig_dims)
    feature_extractor = siamese(sig_dims)

    feats_first = feature_extractor(wave_first)
    feats_second = feature_extractor(wave_second)

    output = Lambda(euclidean_distance)([feats_first, feats_second])
    output = Dense(1)(output)
    output = Activation("sigmoid")(output)

    m = Model(inputs=[wave_first, wave_second], outputs=output)

    m.compile(loss=contrastive_loss, optimizer="adam")

    print("\nModel Summary")
    print(m.summary())

    STEPS_PER_EPOCH = len(x_train) // BS
    VAL_STEPS_PER_EPOCH = len(x_valid) // BS

    best_model = folder + "debbis"
    check_pointer = ModelCheckpoint(filepath=best_model, verbose=1,
                                    save_best_only=True)
    early_stopping = EarlyStopping(monitor='val_loss', patience=5)

    # Define the Keras TensorBoard callback.
    log_dir = folder + "logs/fit/" + datetime.now().strftime("%Y%m%d-%H%M%S")
    tensorboard_callback = TensorBoard(log_dir=log_dir)

    # fit network
    t = time.time()

    h = m.fit([x_train[:, 0], x_train[:, 1]], y_train[:], batch_size=BS,
              validation_data=([x_valid[:, 0], x_valid[:, 1]], y_valid[:]),
              steps_per_epoch=STEPS_PER_EPOCH, verbose=1,
              validation_steps=VAL_STEPS_PER_EPOCH, epochs=EPOCHS,
              callbacks=[check_pointer, early_stopping, tensorboard_callback])

    print('\nTraining time: ', time.time() - t, "\n")

    # save the model to disk
    m.save(best_model)

    return m, h


def plot_predictions(m, xx_test):
    predictions = m.predict([xx_test[:, 0], xx_test[:, 1]])
    print(len(predictions))

    up, down = [], []
    for i in predictions:
        pred = max(i)[0]
        if pred >= 0.00099999:
            up.append(pred)
        else:
            down.append(pred)

    print("Number of predicted Positive Pairs:", len(up))
    print(up, "\n")
    print("Number of predicted Negative Pairs:", len(down))
    print(down, "\n")

    fig = plt.figure(figsize=(64, 54))
    for i, idx in enumerate(np.random.choice(xx_test.shape[0], size=225, replace=False)):
        pred = max(predictions[idx])[0]
        ax = fig.add_subplot(15, 15, i + 1, xticks=[], yticks=[])
        ax.plot(xx_test[idx][0])
        ax.plot(xx_test[idx][1])
        ax.set_title("{:.6f}".format(pred), color=("green" if pred > 0.00099999 else "red"))

    name = "media/plots/siamese_predictions_" + datetime.now().strftime("%Y%m%d-%H%M%S")
    fig.savefig(name, dpi=300, bbox_inches='tight')
    plt.show()


def main():
    path = 'data/ready/pickles/snn.pickle'
    y, x, people = utils.load_data(path)
    y, x = utils.shuffle(y, x)

    limit = 50000
    y, x = create_pairs(y[:limit], x[:limit], people)
    plot_pairs(y, x)

    SIG_DIMS = (x.shape[2], 1)
    print("Input Shape:", SIG_DIMS, "\n")

    x_train, y_train, x_valid, y_valid, x_test, y_test = splits(y, x)
    data = x_train, y_train, x_valid, y_valid, x_test, y_test

    model_path = "models/snn/"
    model, H = train(model_path, SIG_DIMS, data)

    # evaluate model
    accuracy = model.evaluate([x_test[:, 0], x_test[:, 1]], y_test[:])
    print('\n', 'Test accuracy:', accuracy, '\n')

    plot_history(H)
    plot_predictions(model, x_test)
