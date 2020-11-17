# Node-RED



The [node-red-dashboard palette](https://flows.nodered.org/node/node-red-dashboard) provides simple dashboard components embeddeded Node-RED. 

Install node-red-dashboard palette from within Node-RED. Select `menu -> manage palette -> (tab) install`

Create a UI tab and name it `EMRP2020`. Add a chart block to your flow and name it `time series`. Add the chart to a new group you name `group 1`.

Use a Jupyter notebook to generate a sine sequentially and publish the data via MQTT. 




## Exercise

Plot multiple lines in a chart component. The dashboard palette is well described on the source git repo:
https://github.com/node-red/node-red-dashboard/blob/master/Charts.md

Write a Python program which simulates three sine curves y1, y2, y3 with different frequencies (e.g. 0.1, 0.07, 0.03 Hz). 
Let it publish the data every second via MQTT. The JSON string should have a format like 
```
{"t" : "2020-11-09T22:23:22", "y1" : 0.456, "y2": 5.34, "y3": -2.77}
```

Receive the data with a Node-RED MQTT input block. You have to write a function block to create a message for the chart block. 
According to the documentation linked above the message should look like (no warrenty!):

```
[{
"series": ["y1", "y2", "y3"],
"data": [
    [{ "x": "2020-11-09T22:23:22", "y": 0.456}],
    [{ "x": "2020-11-09T22:23:22", "y": 5.34}],
    [{ "x": "2020-11-09T22:23:22", "y": -2.77}]
],
"labels": [""]
}]
```

I am not sure if this is true. I have not tested it, yet.



### Tutorials

Several excellent tutorials on Node-RED exist, e.g. on the official Node-RED website or the excellent [Youtube channel](https://www.youtube.com/user/stevecope) or [website](http://www.steves-internet-guide.com/)  of **Steve Cope**.

To understand the **concept of messages** better it is advisable to read the tutorial on the [Node-Red Programming Model](http://noderedguide.com/node-red-lecture-5-the-node-red-programming-model/).


