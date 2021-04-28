# Dash and Streamlit Dashboard  

Here, we are working on interactive data map application using two differents methods : Dash and streamlit. But after first usage of both, we decide to drop streamlit and invest more on dash. 

[<img src="https://cdn.rawgit.com/plotly/dash-docs/b1178b4e/images/dash-logo-stripe.svg" align="right" width="250">](https://plot.ly/products/dash/)


A curated list of awesome Dash (plotly) resources

> [Dash](https://plot.ly/products/dash/) is a productive Python framework for building web applications.
> Written on top of Flask, Plotly.js, and React.js, Dash is ideal for building data visualization apps with highly custom user interfaces in pure Python. It's particularly suited for anyone who works with data in Python.

## Contents
- [Application] (#Application)
- [Data](#Data)
- [Installation](#Installation)
- [More information about dash](#More information about dash)

## Applications
we have two different apps basically with same features: 
### Trashbin Dashboard
In this application, we have developed a dashboard for trashbin visulization in Moers. It displays waste bin location with different colors representing the fill-level of each one.

### Kamp-Lintfort old Park
This one is also a dashboard for trees monitoring in old park of Kamp-Lintfort. We affect ramdomly group of trees to list of sensors which are implemented in different places.

## Data
The data we are using in the park application is comming basically from sensors already implemented sending real time data. We associate each sensor to a list of trees(for demo until every tree has its own sensor).
For the coordinates, our Professor has provide us a shape file containing trees location. We develop a small script "trees.ipynb" to transform the CRS used and then insert in our database using Postgis plugin.
In the other app, coordinates was already provided but we simulate trashbin's sensor "mock_sensor.py" because there is no one already running.


## Installation
To run the app locally:

1. (optional) create and activate new virtualenv or conda env:

```
pip install virtualenv
virtualenv venv
source venv/bin/activate
```

or, with conda:
```
conda create --yes -n dash_docs
source activate dash_docs
```

2. `pip install -r requirements.txt`
3. `python azam_azam_dash_park.py / azam_dash_app.py (path of dash python file) `
4. open http://127.0.0.1:8050 in your browser 

## More information about dash
For more information and details about dash, do not hesitate to visit the official documentation website.  
- [Official Documentation](https://dash.plotly.com/)