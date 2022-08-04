'''
Aplicaciòn en Flask que busca en la base de datos el còdigo de un punto
y lo despliega en una pagina Html para su lectura
Autores: Gabriel Barròn
Fecha: 11 Julio 2022
'''

import sqlite3
from flask import Flask, render_template
import json


app = Flask(__name__) #Levanta al servidor Flask

'''
Funcion que realiza la conexion a la BD en SQLITE
'''
def get_db_connection():
    conn = sqlite3.connect('capstone.db')
    conn.row_factory = sqlite3.Row
    return conn

'''
Funcion que realiza una consulta de todos los productos en existencua
'''
@app.route('/')
def index():
    conn = get_db_connection()
    products = conn.execute('SELECT * FROM producto').fetchall()
    conn.close()
    return 'Hola a todos'

'''
Funcion que extrae la informacion de un producto mediante su codigo de producto
'''
@app.route('/<code>', methods=('GET', 'POST'))
def get_product(code):
    conn = get_db_connection()
    query = "SELECT * FROM producto WHERE codigo = '" + code + "'" 
    print("Query ", query)
    product = conn.execute(query).fetchone()
    conn.close()
    print(product)
    return render_template('product.html', producto=product)