import plotly.express as px
import dash
import dash_core_components as dcc
import dash_html_components as html
from dash.dependencies import Input, Output 
import plotly.graph_objs as go
import pandas as pd
from queries import *


#us_cities = pd.read_csv("https://raw.githubusercontent.com/plotly/datasets/master/us-cities-top-1k.csv")

external_stylesheets = ['https://codepen.io/chriddyp/pen/bWLwgP.css']

app = dash.Dash(__name__, external_stylesheets=external_stylesheets)

app.layout = html.Div(
    html.Div([
        html.H4('Kamp-Lintfort Park Application'),
        html.Div(id='live-update-text'),
        dcc.Graph(id='live-update-graph'),
        dcc.Interval(
            id='interval-component',
            interval=60*1000, # in milliseconds
            n_intervals=0
        )
    ])
)

@app.callback(Output('live-update-graph', 'figure'), Input('interval-component', 'n_intervals'))
def update_graph_live(n):

    df = get_table("geo","trees")
    df_data = get_last_50("geo", "emrp_lse01_test")

    
    df_final = pd.DataFrame()
    
    
    df_final['tree_number'] = df['tree_number']
    df_final['tree_species'] = df['tree_species']
    df_final['sensor_id'] = df['sensor_id']
    df_final['latitude'] = df['Longitude']
    df_final['longitude'] = df['Latitude']

    df_final['temp'] = df_data.loc[(df_data['variable_type'] == 'temp'),'val']
    df_final['ec'] = df_data.loc[(df_data['variable_type'] == 'ec'),'val']
    df_final['sm'] = df_data.loc[(df_data['variable_type'] == 'sm'),'val']
    df_final['bat'] = df_data.loc[(df_data['variable_type'] == 'bat'),'val']
        
    print(df_final)    

    fig = px.scatter_mapbox(df_final, lat="latitude", lon="longitude", hover_name="tree_number", hover_data=["sensor_id", "temp", "ec", "sm", "bat"],
                            color= 'sensor_id', color_continuous_scale=px.colors.sequential.YlOrRd, zoom=15, height=800, size='latitude', size_max=5)
    
    fig.update_layout(mapbox_style="open-street-map")   
    fig.update_layout(margin={"r":0,"t":0,"l":0,"b":0})

    print('graph updated')

    return fig

if __name__ == '__main__':
    app.run_server(debug=True)