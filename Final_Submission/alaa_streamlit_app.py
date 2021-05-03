import streamlit as st
import sys, os
import numpy as np
import pandas as pd
#from bokeh.plotting import figure
from queries import *


def main():
    #side bar section
    st.sidebar.header("User Input Features")
    # get list of sensors_id 
    sensor_list_df = get_list("sensor_id","geo","emrp_lse01_test","sensor_id")
    sensors_choice = st.sidebar.multiselect('Sensor',sensor_list_df)

    st.title('Dashboard: Interactive map')
    st.subheader('View trashbins map')
    trash_loc_df = get_table("geo","trashbin")
    st.map(trash_loc_df)


    st.subheader("Temperature information")
    sensor_df = get_table("geo","emrp_lse01_test")
    selected_sensor_df = sensor_df[ sensor_df['sensor_id'].isin(sensors_choice)]
    st.write(selected_sensor_df)

    ''' Plot soil measurements execpt 'ec' '''

    st.vega_lite_chart(sensor_df,
         {
        "width" : 800,
        "height" : 500,
        "mark": "line",   
        'encoding': {
        "x": {"field": "ts", "type": "temporal"},
        "y": {"field": "val",  "type": "quantitative"},
        "color": {"field": "variable_type", "type": "nominal"}
        },
        })

if __name__ == "__main__":
    main()
    