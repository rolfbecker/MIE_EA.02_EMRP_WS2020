# Homework 1: node-red and MQTT

## 1. Install node-red on your local computer.

## 2. Write a MQTT publisher publishing a sine wave. 

Use Python (e.g. with Jupyter or another way) to publish a sine wave which is received by node-red.
Start with `publisher_V004.ipynb`. Create a copy, name it  `sine_publisher_V001.ipynb` and modify it. 

Choose your own meaningful topic string.

The publisher loops infinitely. Send one point per second by using the simple `time.sleep(1)` function. It would delay the execution of one loop by one second.

Use an approch such as `y = A0*sin(2*pi*f*t)`

Send the message as a string. It should have the format `{"sin" : y}`, like a Python dictionary or a JSON string.

Try to receive the string with the node-red MQTT Input block.
