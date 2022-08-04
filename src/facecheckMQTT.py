'''
Aplicacion que verifica el analisis de un cliente 

Autores: Gabriel Barròn
Fecha: 11 Julio 2022
'''
import sys, getopt
from deepface import DeepFace
import paho.mqtt.client as mqtt
import time
import pandas as pd

#Mètodo que inicializa mosquitto
def on_connect(client, userdata, flags, rc):
    print(f"Connected with result code {rc}")

'''
   Mètodo que inicia la aplicacion
'''
def main(argv):
   img_src = '' #Parametro de la imagen a analizar
   db_src = ''  #Parametro de la ruta de la base de datos de imagenes
   try:
      opts, args = getopt.getopt(argv,"hi:j:",["imgsrc=","dbsrc="])
   except getopt.GetoptError:
      print ('test.py -i <img_src> -j <db_src>')
      sys.exit(2)
   for opt, arg in opts:
      if opt == '-h':
         print ('test.py -i <img_src> -j <db_src>')
         sys.exit()
      elif opt in ("-i", "--imgsrc"):
         img_src = arg
      elif opt in ("-j", "--dbsrc"):
         db_src = arg
   
   #Modulo Deepface que se analiza
   df = DeepFace.find(img_path = img_src,db_path= db_src,model_name='VGG-Face',enforce_detection=False)

   #Se almacena resultado de analisis
   result = df.to_json(orient="index")

   #Se envia resultado a traves de mosquitto
   client = mqtt.Client()
   client.on_connect = on_connect
   client.connect("localhost", 1883, 60)
   client.publish('capstone/facial', payload=result, qos=0, retain=False)
   return df

if __name__ == "__main__":
   main(sys.argv[1:])
   
