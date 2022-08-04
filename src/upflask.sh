#!/bin/bash
cd /home/gbarron/capstone/src
export FLASK_APP=/home/gbarron/capstone/src/app.py

#Se ejecuta servidor
flask run --host=0.0.0.0
exit
