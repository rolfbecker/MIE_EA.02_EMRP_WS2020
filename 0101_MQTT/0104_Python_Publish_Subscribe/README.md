
# Installation of MQTT Related Software Packages 

## Change to new remote repo location

Clone the repo from `github.com`:
```
git clone https://github.com/rolfbecker/MIE_EA.02_EMRP_WS2020.git
```

In case you have cloned from `github.com` already pull the repo (update local from remote):
```
cd MIE_EA.02_EMRP_WS2020
git pull
```

## Install the Paho MQTT Module for Python

Login as studi and open a terminal. Execute:
```
conda install -c conda-forge paho-mqtt
```

Open `Jupyter-Lab` (e.g. via `Anaconda Navigator`) and change to the directory of the MQTT Python notebooks `publisher_V004.ipynb` and `subscriber_V004.ipynb`.
```
Open the notebooks in Jupyter. Read the code and understand.

1. Execute the **subscriber first**. It is an infinite loop. The code does not stop on its own. You have to stop the execution of the cell or interrupt the kernel.
1. Execute the **publisher second**. The cell executions publish a simple text message. Look at the subscriber. It should have received it.

Many publishers and subscribers can use the same topic.




