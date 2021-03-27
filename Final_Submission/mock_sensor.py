import time
from datetime import datetime
import random
import paho.mqtt.client as mqtt
from time import sleep
import math
import json


broker = "hsrw.space"
port=1883

username = "user"
password = "mqtt"

myname = "alaa"
#myname = "mickey_mouse"

# this should be a unique ID.
sensor_id = 22

topic = "/emrp2020/"+myname+"/mock_level_sensor"
print("Topic: ", topic)

def on_publish(client,userdata,result):             #create function for callback
    print("data published")
    pass

#client1= mqtt.Client(client_id = "Prof. B.'s Publisher")           #create client object
client1= mqtt.Client()           #create client object
client1.on_publish = on_publish                          #assign function to callback

client1.username_pw_set(username = username, password = password)
client1.connect(broker,port) #establish connection


a = 5
b = 30
fill = [0] * 1111


# Increase randommly the fill level and publish to mqtt server
while(True):
    for i in range(1,1111):
    
        level = random.uniform(a, b) + fill[i]
        temp = random.uniform(0, 20)
        
        if level > 100:
            level = 0
            fill[i] = 0
            
    
        ts = datetime.now().isoformat()+"CET"
        #    ms = int(datetime.now().timestamp()*1000) # milliseconds!
    
        data = {"trash_id": i, "ts" : ts, "level" : level, "temp" : temp}
    
        fill[i] = level
        
        msg = json.dumps(data)
        print("msg: ", msg)
        ret = client1.publish(topic,msg) 
        print("ret: ", ret)
        time.sleep(0.001)
    
    

