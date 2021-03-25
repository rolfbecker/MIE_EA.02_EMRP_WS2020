Environmental Monitoring Research Project
============================================

Abstraction
-------------------

Environmental Monitoring Research Project – In this project going to sense Temperature and Humidity Data using **TTGO- ESP32-Lora** Micro devices. The data collection from sensor such as **SHT31**. After that send to the database such as **Postgres-SQL**. In between the process also use the MQTT Platform for data manipulation. Last step of The process is That to analyse data and make a graphical representation using relation model with web application Prospect.

Introduction
-------------------

Environmental Monitoring is all about data, which has been sense by sensors.But, the list of questions that helps to more understand project like, How can collect the data from sensors? How can process the collected data? How to use the data for application? Where to store the sensor data? The answer will be An environmental monitoring device – which can connect to internet connection (Ethernet or WiFi depends on the mode of device can connect) A Potgres-SQL to store data, and help of environmental monitoring device collect the sensor data. this article will also explain how to use sensors, MQTT and Micro Device for Environmental Monitoring, will cover how to write a Arduino IDE code. Last, using web application display data and represent graphical view.

Steps Of EMRP Process
----------------------

* It is five step process of EMRP 
* Sensors (e.g. DTH31)
* Devices (e.g. ESP32)
* Getway  (e.g. Node-RED)
* Database or Cloud (e.g. Postgres-SQL) 
* Application (e.g. Web Application or Dashboard)

Software Requirements
----------------------

  * Arduino IDE
  * Node-Red MQTT
  * Python Jupiter
  * Application Platform
  * Postgres SQL
  * Axelor application platform

Data Model
------------
EMPR project data model describ on mqtt_axelor_root branh of this project
Link: https://github.com/rolfbecker/MIE_EA.02_EMRP_WS2020/tree/mqtt_axelor_root







 

