# Author: Axel Mukwena
# ECG Biometric Authentication using CNN

# import the necessary packages
import os
import pickle
import time
from datetime import datetime

import matplotlib.pyplot as plt
import numpy as np
from sklearn.metrics import classification_report
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelBinarizer
from keras.callbacks import EarlyStopping
from keras.callbacks import ModelCheckpoint
from keras.callbacks import TensorBoard
from keras.layers import Activation
from keras.layers import BatchNormalization
from keras.layers import Conv1D
from keras.layers import Dense
from keras.layers import Dropout
from keras.layers import Flatten
from keras.layers import MaxPool1D
from keras.layers import concatenate
from keras.models import Sequential

import utils

# initialize the number of epochs to train for, initial learning rate,
# batch size, and image dimensions
EPOCHS = 100
BS = 64
LR = 0.00001
decay = LR / EPOCHS


def splits(yy, xx, sig_dims):
    # binarize the labels
    lbb = LabelBinarizer()
    yy = lbb.fit_transform(yy)

    # train 70%, test is 30%
    xx_train, xx_test, yy_train, yy_test = train_test_split(
        xx, yy, test_size=0.4, shuffle=True, random_state=42)

    xx_valid, xx_test, yy_valid, yy_test = train_test_split(
        xx_test, yy_test, test_size=0.5, shuffle=True, random_state=42)

    print("X train shape:", xx_train.shape)
    xx_train = xx_train.reshape(xx_train.shape[0], sig_dims[0], sig_dims[1])
    xx_valid = xx_valid.reshape(xx_valid.shape[0], sig_dims[0], sig_dims[1])
    xx_test = xx_test.reshape(xx_test.shape[0], sig_dims[0], sig_dims[1])
    print("X train shape:", xx_train.shape)
    print("X valid shape:", xx_valid.shape)
    print("X test shape:", xx_test.shape, "\n")

    return xx_train, yy_train, xx_valid, yy_valid, xx_test, yy_test, lbb


def block(m, fs, ks, ps):
    m.add(Conv1D(filters=fs, kernel_size=ks, padding="same"))
    m.add(BatchNormalization())
    m.add(Activation('relu'))
    m.add(MaxPool1D(pool_size=ps, padding='same'))
    return m


def spp_layer(inp, spp_windows):
    p_poolings = []

    for pi in range(len(spp_windows)):
        p_poolings.append(Flatten()(MaxPool1D(pool_size=spp_windows[pi], padding='same')(inp)))
    out = concatenate(p_poolings, axis=-1)

    return out


def train(folder, sig_dims, data):
    x_train, y_train, x_valid, y_valid, x_test, y_test, lb = data
    if not os.path.exists(folder):
        os.makedirs(folder)

    # save the label binarizer to disk
    f = open(folder + "lb.pickle", "wb")
    f.write(pickle.dumps(lb))
    f.close()

    # Model
    m = Sequential()

    m.add(Conv1D(filters=32, kernel_size=1, padding="same", input_shape=sig_dims))
    m.add(BatchNormalization())
    m.add(Activation('relu'))

    # Blocks
    m = block(m, 32 * 2, 15, 2)
    m = block(m, 32 * 4, 15, 2)
    m = block(m, 32 * 8, 15, 2)
    m = block(m, 32 * 16, 15, 2)
    last = 1 + 2 + 4 + 8 + 16
    m = block(m, 32 * last, 15, 2)

    # Fully Connected Layer
    m.add(Flatten())
    m.add(Dense(32 * last))
    m.add(BatchNormalization())
    m.add(Activation('relu'))
    m.add(Dropout(0.25))

    # softmax classifier
    m.add(Dense(len(lb.classes_)))
    m.add(Activation("softmax"))

    print(m.summary())

    m.compile(optimizer="adam", loss="categorical_crossentropy", metrics=["accuracy"])

    STEPS_PER_EPOCH = len(x_train) // BS
    VAL_STEPS_PER_EPOCH = len(x_valid) // BS

    best_model = folder + "debbis"
    check_pointer = ModelCheckpoint(filepath=best_model, verbose=1, save_best_only=True)
    early_stopping = EarlyStopping(monitor='val_loss', patience=5)

    # Define the Keras TensorBoard callback.
    log_dir = folder + "logs/fit/" + datetime.now().strftime("%Y%m%d-%H%M%S")
    tensorboard_callback = TensorBoard(log_dir=log_dir)

    # fit network
    t = time.time()

    h = m.fit(x_train, y_train, batch_size=BS,
              validation_data=(x_valid, y_valid),
              steps_per_epoch=STEPS_PER_EPOCH,
              validation_steps=VAL_STEPS_PER_EPOCH,
              epochs=EPOCHS, verbose=1,
              callbacks=[tensorboard_callback, check_pointer, early_stopping])

    print('\nTraining time: ', time.time() - t)

    # save the model to disk
    m.save(best_model)

    return m, h


# plot the training loss and accuracy
def plot_history(h):
    acc = h.history['accuracy']
    val_acc = h.history['val_accuracy']

    loss = h.history['loss']
    val_loss = h.history['val_loss']

    plt.figure(figsize=(8, 8))
    plt.subplot(2, 1, 1)
    plt.plot(acc, label='Training Accuracy')
    plt.plot(val_acc, label='Validation Accuracy')
    plt.legend(loc='lower right')
    plt.ylabel('Accuracy')
    plt.ylim([min(plt.ylim()), 1])
    plt.title('Training and Validation Accuracy')

    plt.subplot(2, 1, 2)
    plt.plot(loss, label='Training Loss')
    plt.plot(val_loss, label='Validation Loss')
    plt.legend(loc='upper right')
    plt.ylabel('Cross Entropy')
    plt.ylim([0, 1.0])
    plt.title('Training and Validation Loss')
    plt.xlabel('epoch')

    name = "media/plots/cnn_history_" + datetime.now().strftime("%Y%m%d-%H%M%S")
    plt.savefig(name, dpi=300, bbox_inches='tight')

    plt.show()


def report(m, x_test, y_test):
    lbb = LabelBinarizer()
    predictions = m.predict(x_test, batch_size=BS, verbose=1)
    y_pred_bool = np.argmax(predictions, axis=1)
    y_pred_bool = lbb.fit_transform(y_pred_bool)
    print(classification_report(y_test, y_pred_bool), "\n")
    return predictions


def plot_predictions(predictions, x_test, y_test, lb):
    up, down = [], []
    for i in predictions:
        pred = max(i)
        if pred >= 0.99:
            up.append(pred)
        else:
            down.append(pred)

    print("Number of predicted Positive Pairs:", len(up))
    print(up, "\n")
    print("Number of predicted Negative Pairs:", len(down))
    print(down, "\n")

    fig = plt.figure(figsize=(64, 54))
    for i, idx in enumerate(np.random.choice(x_test.shape[0], size=225, replace=False)):
        pred_idx = np.argmax(predictions[idx])
        true_idx = np.argmax(y_test[idx])
        prob = max(predictions[pred_idx])
        ax = fig.add_subplot(15, 15, i + 1, xticks=[], yticks=[])
        ax.plot(x_test[idx])
        ax.set_title("T: {} P: {} {:.6f}".format(lb.classes_[true_idx], lb.classes_[pred_idx], prob),
                     color=("green" if pred_idx == true_idx else "red"))

        name = "media/plots/cnn_predictions_" + datetime.now().strftime("%Y%m%d-%H%M%S")
        fig.savefig(name, dpi=300, bbox_inches='tight')
        plt.show()


def main():
    path = 'data/ready/pickles/cnn.pickle'
    y, x, people = utils.load_data(path)
    y, x = utils.shuffle(y, x)

    SIG_DIMS = (x.shape[1], 1)
    print("Input Shape:", SIG_DIMS, "\n")

    x_train, y_train, x_valid, y_valid, x_test, y_test, lb = splits(y, x, SIG_DIMS)
    data = x_train, y_train, x_valid, y_valid, x_test, y_test, lb

    model_path = "models/cnn/"
    model, H = train(model_path, SIG_DIMS, data)

    # evaluate model
    _, accuracy = model.evaluate(x_test, y_test, batch_size=BS, verbose=1)
    print('\n', 'Test accuracy:', accuracy, '\n')

    plot_history(H)
    preds = report(model, x_test, y_test)
    plot_predictions(preds, x_test, y_test, lb)
