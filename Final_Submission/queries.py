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

def get_last_50(schema, table):

    query = "SELECT * FROM {}.{} ORDER BY ts DESC LIMIT 20;".format(str(schema), str(table))


    print(query)

    # Load the locations of trashbins
    df = pd.read_sql(query, conn)

    cursor.close()

    print(df.shape)
    return df

cursor.close()