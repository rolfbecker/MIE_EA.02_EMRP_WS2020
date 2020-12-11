## Setting up TTN
### Setting up a new TTN Application
Register for an account at https://www.thethingsnetwork.org/ \
Login and navigate to https://console.thethingsnetwork.org/ \
Click on `APPLICATIONS` to go to the application panel.
![console](https://github.com/rolfbecker/MIE_EA.02_EMRP_WS2020/blob/master/pictures/sensor_node_ttn/ttn_console.jpg)

Click on the `add application` button to add a new application. 
![add application](https://github.com/rolfbecker/MIE_EA.02_EMRP_WS2020/blob/hung/pictures/sensor_node_ttn/ttn_add_app.jpg)

Give the application a **_unique application ID_** and choose the proper handler with respect to the region where the application is to be deployed.
![application id](https://github.com/rolfbecker/MIE_EA.02_EMRP_WS2020/blob/hung/pictures/sensor_node_ttn/ttn_add_app_2.jpg)

The website will redirect to the application's `overview` page right after it is created.
![app overview](https://github.com/rolfbecker/MIE_EA.02_EMRP_WS2020/blob/hung/pictures/sensor_node_ttn/ttn_add_app_3.jpg)
### Setting the Payload Format
Click on the `Payload Formats` tab on the upper right corner of the `Application Overview` page. 
Choose `CayenneLPP` from the drop-down menu and click `save`.
![payload formats](https://github.com/rolfbecker/MIE_EA.02_EMRP_WS2020/blob/hung/pictures/sensor_node_ttn/ttn_payload_formats.jpg)

### Registering a Device
From the the `Application Overview` page, click on the `Devices` tab.
Click on the `register device` button.
![register device](https://github.com/rolfbecker/MIE_EA.02_EMRP_WS2020/blob/hung/pictures/sensor_node_ttn/ttn_register_devices.jpg)

Give the device a *unique device name* (within the scope of the application, meaning there should be no other devices in the application with the same name).\
On the `Device EUI` section, click the `generate` button to automatically generate a Device EUI.\
Click the `Register` button to finish registering the device.
![register device panel](https://github.com/rolfbecker/MIE_EA.02_EMRP_WS2020/blob/hung/pictures/sensor_node_ttn/ttn_register_devices_2.jpg)

The website will redirect to the `Device Overview` page after registration is complete.
![device overview](https://github.com/rolfbecker/MIE_EA.02_EMRP_WS2020/blob/master/pictures/sensor_node_ttn/ttn_device_overview.jpg)
Note that:

 - The **Activation Method** must be `OTAA` (changable in the `Settings` tab).
 - **Device EUI**, **Application EUI** and **App Key** are essensital for the configuration within the embedded software which will be covered in [section 5.1.2](#512-device-keys)