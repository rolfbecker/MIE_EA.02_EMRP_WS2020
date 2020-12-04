# Node-RED - PostgreSQL

Node-RED can be used to insert or select data from a PostgreSQL database.

PostgreSQL is the most sophisticated open source object relational database management system (ODBMRS). For example it provides stored procedures, user defined abstract data types (own objects) and other powerful features. PostGIS is a spatial database extender to PostgreSQL to store and process geographic objects such as polygons. This allows to store vector type objects together with their attribute tables in one database.

Assume a sensor sends the following data:
```
{"sensor_id": 22, "ts": '2020-09-09T12:13:14UTC', "val": 3.14159}
```

You need a function node to extract the data from the payload, arrange it in a list and assign the list to **`msg.params`**. This message object is then sent to the SQL node.

```
let sensor_id = msg.payload.sensor_id;
let ts = msg.payload.ts;
let val = msg.payload.val;

msg.params = [sensor_id, ts, val];
return msg;
```

The SQL node contains:

```
INSERT INTO TEST ("sensor_id", "ts", "val") VALUES ($1, $2, $3);
```

For more information see the Jupyter notebook of this activity.

