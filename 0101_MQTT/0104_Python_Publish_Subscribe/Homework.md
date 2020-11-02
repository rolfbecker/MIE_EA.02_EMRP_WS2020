# Homework 1: node-red and MQTT

## 1. Install node-red on your local computer.

## 2. Write a MQTT publisher publishing a sine wave. 

Use Python (e.g. with Jupyter or another way) to publish a sine wave which is received by node-red.
Start with `publisher_V004.ipynb`. Create a copy, name it  `sine_publisher_V001.ipynb` and modify it. 

The publisher loops infinitely. Send one point per second by using the simple `time.sleep(1)` function. It would delay the execution of one loop by one second.

Send a message in the format `{"sin" : y}`

With $y$

