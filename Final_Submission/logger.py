import logging

# creating a logger object with name MY_DIVISION'
logger = logging.getLogger("__app__")

# Setting the logging level to 'WARNING'
logger.setLevel(logging.WARNING)

# add the file handler to the logger object
logger.addHandler(fh)