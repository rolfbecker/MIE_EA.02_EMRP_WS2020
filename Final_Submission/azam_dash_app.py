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
        html.H4('Moers Trash Bin Application'),
        html.Div(id='live-update-text'),
        dcc.Graph(id='live-update-graph'),
        dcc.Interval(
            id='interval-component',
            interval=10*1000, # in milliseconds
            n_intervals=0
        )
    ])
)

@app.callback(Output('live-update-graph', 'figure'), Input('interval-component', 'n_intervals'))
def update_graph_live(n):

    df = get_table("geo","trashbin")

    fig = px.scatter_mapbox(df, lat="latitude", lon="longitude", hover_name="bin_id", hover_data=["name", "fill_level","temperature"],
                            color= 'fill_level', color_continuous_scale=px.colors.sequential.YlOrRd, zoom=12, height=800)
    fig.update_layout(mapbox_style="open-street-map")   
    fig.update_layout(margin={"r":0,"t":0,"l":0,"b":0})

    print('graph updated')

    return fig

if __name__ == '__main__':
    app.run_server(debug=True)