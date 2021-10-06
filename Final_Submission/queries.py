import psycopg2
import pandas.io.sql as psql
import pandas as pd
import credentials as creds
from queries import *
#from logger import logger

# Set up a connection to the postgres server.
conn_string = "host="+ creds.PGHOST +" port="+ "5432" +" dbname="+ creds.PGDATABASE +" user=" + creds.PGUSER \
+" password="+ creds.PGPASSWORD
conn=psycopg2.connect(conn_string)
print("Connected!")

# Create a cursor object
cursor = conn.cursor()

def sensors_data(schema,table,sensors):
    """"
    this function to get sensor parameter
    """
    list_tuple = tuple(sensors)
    query = "SELECT * FROM {}.{} WHERE sensor_id LIKE '{}' ;".format(str(schema),str(table),list_tuple)
    print(query)

    # Load the locations of trashbins
    df = pd.read_sql(query, conn)

    cursor.close()

    print(df.shape)
    return df


def get_table(schema,table):
    """
    get sql table to dataframe
    """
    query = "SELECT * FROM {}.{} ;".format(str(schema), str(table))
    print(query)

    # Load the locations of trashbins
    df = pd.read_sql(query, conn)

    cursor.close()
    print(df.shape)
    return df

def get_list(param,schema,table,choice):
    """
    get list of trash/sensor or defined param
    """
    query = "SELECT DISTINCT {} FROM {}.{} ORDER BY {} ;".format(str(param),str(schema), str(table), str(choice))
    print(query)

    # Load the locations of trashbins
    sensor_list = pd.read_sql(query, conn)

    cursor.close()

    return sensor_list


def get_var_type(schema,table,sensors_choice):
    """
    get types of data produced by a sensor
    """

    query = "SELECT DISTINCT variable_type FROM {}.{} ORDER BY variable_type ;".format(str(schema), str(table))
    print(query)

    # Load the locations of trashbins
    type_list = pd.read_sql(query, conn)

    cursor.close()

    return type_list

def get_last_100(schema, table):

    query = "SELECT * FROM {}.{} ORDER BY ts DESC, trash_id DESC LIMIT 1111;".format(str(schema), str(table))

    print(query)

    # Load the locations of trashbins
    df = pd.read_sql(query, conn)

    cursor.close()

    print(df.shape)
    return df


def get_last_val(schema, table,sensor_id):
    ''' Get 4 lines insert in the table which represent newest measurement '''
    
    query = "SELECT sensor_id,variable_type,val FROM {}.{} WHERE sensor_id = '{}' ORDER BY ts DESC LIMIT 4;".format(str(schema), str(table), str(sensor_id))


    print(query)

    # Load the locations of trashbins
    df = pd.read_sql(query, conn)

    cursor.close()

    print(df.shape)
    return df

def get_sensor_val(): 
    '''
    Get last values send by sensors
    '''
    df_sensor = get_list("sensor_id","geo","emrp_lse01_test","sensor_id")
    sensor_list = df_sensor['sensor_id'].tolist()
    df = pd.DataFrame(columns=['sensor_id','variable_type','val'])
    for i in sensor_list:
        df_query = get_last_val("geo","emrp_lse01_test",i) 
        df = df.append(df_query, ignore_index=True, sort=False)
    df = pd.pivot(df, index=['sensor_id'],columns=['variable_type'], values='val')    
    print(df)

    #print(df)
    cursor.close()
    return df


