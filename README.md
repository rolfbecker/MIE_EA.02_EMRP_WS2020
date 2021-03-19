Axelor Open Suite
================================

Axelor Open Suite reduces the complexity and improve responsiveness of business processes. Thanks to its modularity, you can start with few features and  then activate other modules when needed.

Axelor Open Suite includes the following modules :

* Customer Relationship Management
* Sales management
* Financial and cost management
* Human Resource Management
* Project Management
* Inventory and Supply Chain Management
* Production Management
* Multi-company, multi-currency and multi-lingual

Axelor Open Suite is built on top of [Axelor Open Platform](https://github.com/axelor/axelor-open-platform)

Download
-------------------------
```bash
$ git clone git@github.com:axelor/open-suite-webapp.git
$ cd open-suite-webapp
$ git checkout master
$ git submodule init
$ git submodule update
$ git submodule foreach git checkout master
$ git submodule foreach git pull origin master
```

EMRP Axelor project setup
================================
EMRP Axelor setup section going to show project direcotry structure, For that need to clone this branch shown below 
 * https://github.com/rolfbecker/MIE_EA.02_EMRP_WS2020/tree/mqtt_axelor_root
 * https://github.com/rolfbecker/MIE_EA.02_EMRP_WS2020/tree/mqtt_axelor

Following steps to run MQTT-Axelor project on localhost

Step 1: clone root project 
```bash
$ git clone https://github.com/rolfbecker/MIE_EA.02_EMRP_WS2020.git
$ cd MIE_EA.02_EMRP_WS2020/
$ git fetch origin mqtt_axelor_root
$ git pull origin mqtt_axelor_root
$ git co mqtt_axelor_root
```
From above command clone this "MIE_EA.02_EMRP_WS2020" folder and change branch with mqtt_axelor_root  

Step 2: 
1) Change The folder name "MIE_EA.02_EMRP_WS2020" to "axelor-erp"
2) Write password in file "axelor-erp/src/main/resources/application.properties" password is given on discord 
3) Run following command 
```bash
$ ./gradlew build
```
Step 3: Now set up sub modules for that go to location "axelor-erp/modules/abs"
clone sub modules and change the branch 
```bash
$ git clone https://github.com/rolfbecker/MIE_EA.02_EMRP_WS2020.git
$ cd MIE_EA.02_EMRP_WS2020/
$ git fetch origin mqtt_axelor
$ git pull origin
$ git co git co mqtt_axelor
```
Important : 

1) Cut folder "MIE_EA.02_EMRP_WS2020" from "axelor-erp/modules/abs/" and past on "axelor-erp/modules/"
2) Delete empty folder "abs"
3) Rename folder from "MIE_EA.02_EMRP_WS2020" to "abs" on location "axelor-erp/modules/"

All set up done ....next run command for build project

Setp 4:

Run command from location "axelor-erp/"
```bash
$ ./gradlew clean classes build -x test cleanEclipse eclipse
$ ./gradlew --no-daemon run
```
It takes some time to run project, you find link on terminal http://localhost:8080/axelor-erp
or past this link to browser 


EMRP Axelor project setup for Windows
=======================================

prerequisite for emrp data excess data
---------------------------------------
1. postgres SQL for data management https://www.postgresql.org/download/windows/
2. Git for version control https://desktop.github.com/
3. Python for API call https://www.python.org/downloads/
4. Insomaina for read Cookies https://insomnia.rest/download
5. Tomcat Server 8.5.0 for run project on localhost https://tomcat.apache.org/tomcat-8.5-doc/index.html
6. Eclipse for change data-model https://www.eclipse.org/downloads/
7. Java minimum version 8










  
