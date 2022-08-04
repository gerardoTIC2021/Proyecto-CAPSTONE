'''
Aplicaciòn en Python que realiza capturas de rostro del cliente

Autores: Gabriel Barròn
Fecha: 11 Julio 2022
'''
from importlib.resources import path 
from tkinter import* #librerias tkinter
from tkinter import ttk
from tkinter import messagebox
from pathlib import Path
from turtle import title
from xmlrpc.client import boolean
from PIL import Image, ImageTk
import paho.mqtt.client as mqtt #librerias mosquitto
import cv2 
import urllib.request #hacer peticiones
import numpy as np
import matplotlib as plt
import os
import json
import re 

url= 'http://192.168.1.69:81/stream' # URL de la camara
winName = 'Proyecto CapStone' #titulo de la aplicacion 
cv2.namedWindow(winName, cv2.WINDOW_AUTOSIZE)
escala = 80
contador = 0
isNewClient = True
servermqtt = "127.0.0.1" #url de mosquitto
port = 1883 #puerto donde escucha mosquitto
topic = "capstone/utng/camara" #Tema a donde publicar
directory = "/home/gbarron/capstone/src/my_db/" #ruta donde se guardan las imagenes

cliente = mqtt.Client("camara1")
cliente.connect(servermqtt, port) #establece conexion

'''
    Metodo que permite cerrar la aplicaciòn.
'''
def cerrar():
    global root
    root.destroy()


'''
    Metodo que inicializa la camara
'''
def iniciarCamara():
    #Manipula variables en toda la aplicacion
    global camara, img, isNewClient, lblImagen
    global btnGuardar, btnCapturar

    txtNombre.focus()
    contador = 0 #Inicia cuenta de imagen
    isNewClient = True
    camara = cv2.VideoCapture(url) #Inicializa camara
    capturaImagen()

'''
    Metodo que guarda imagen de la camara
'''
def capturaImagen():
    global camara, img
    if camara is not None: #Es correcta la inicializacion
        ret, imagen = camara.read()
        if ret == True:
            imagen = cv2.cvtColor(imagen, cv2.COLOR_BGR2RGB)
            img = Image.fromarray(imagen)
            img = img.resize((640,480)) #resolucion de la imagen
            imgTk = ImageTk.PhotoImage(image=img)
            lblImagen.configure(image=imgTk) #Almacena la imagen en control Label
            lblImagen.image = imgTk
            lblImagen.after(10, capturaImagen)
        else:
            lblImagen.image = ""
            camara.release()
'''
    Metodo que guarda las imagenes que se capturan mediante la camara
'''
def save_data():
    global isNewClient
    mensaje = ""  
    #Validacion de datos que se capturan 
    if len(txtNombre.get().strip()) == 0: 
        mensaje += "Se requiere un nombre\n"
        txtNombre.focus()
    if len(txtUsuario.get().strip()) == 0: 
        mensaje += "Se requiere un usuario\n"
        txtUsuario.focus()
    if len(txtEmail.get().strip()) == 0:
        mensaje += "Se requiere un Email\n"
        txtEmail.focus()
    if len(txtCelular.get().strip()) == 0:
         mensaje += "Se requiere un Celular\n"
         txtCelular.focus()

    if len(mensaje.strip()) != 0:
        messagebox.showerror(message=mensaje, title="Validando datos")
        return
    
    dir = directory + txtUsuario.get() #ruta donde se almacenan
    print("Directorio " + dir)
    try:
        if isNewClient:
            os.mkdir(dir)
            #Se elimina archivo para que se reconozca a la nueva persona
            os.remove(directory + "representations_vgg_face.pkl")
    except:
        print(dir)

    saveImagen()
    if isNewClient: #Guarda en un archivo JSON datos del usuario
        unmensaje = "{\"nombre\":\""+txtNombre.get() + "\",\"email\":\""  + txtEmail.get() + "\",\"celular\":\"" + txtCelular.get() +"\"}"
        ret = cliente.publish(topic,unmensaje) #publica datos a traves de mosquitto
        isNewClient = False
        ruta = directory + txtUsuario.get().strip() + "/info.json"
        with open(ruta,'w') as f:
            f.write(unmensaje)

'''
    Metodo que se dedica a guardar las imagenes en formato PNG
'''
def saveImagen():
        global contador
        contador = contador + 1 #Contador de imagenes
        dir = directory + txtUsuario.get()
        fileName = dir + "/" + str(contador) +'.png'
        print(fileName)
        path = Path(fileName)
        if not path.is_file():
            img.save(fileName, format="png")
        else:
            messagebox.showerror(message='El archivo existe!!', title="agregando datos")

'''
    Metodo cierra la aplicacion y libera la camara
'''
def cerrarCamara():
    global camara
    contador = 0  # se inicializa contador
    isNewClient = True  #se inicializa un nuevo cliente
    lblImagen.image = ""
    camara.release()
    contador = 0


root = Tk()
ancho_ventana = 960
alto_ventana = 540
coordX = 80
coordY = 80
isCorrecto = True

x_ventana = root.winfo_screenwidth() // 2 - ancho_ventana // 2
y_ventana = root.winfo_screenheight() // 2 - alto_ventana // 2

posicion = str(ancho_ventana) + "x" + str(alto_ventana) + "+" + str(x_ventana) + "+" + str(y_ventana)
root.geometry(posicion)
root.title("Registro de Cliente")


lblTitulo = Label(root, text="Registro de cliente",width=20,font=("bold", 20))
lblTitulo.place(x=90,y=20)

lblNombre = Label(root, text="Nombre Completo",width=20,font=("bold", 10))
lblNombre.place(x=80,y=coordY)

'''
    Metodo valida nombre cuando pierde el foco
'''
def lost_focus_nombre(event):
    print('Se invoca focus nombre')
    lstNombre = txtNombre.get().split()
    if len(lstNombre) < 2:
        messagebox.showerror(message="Se require un nombre con sus apellidos")
        txtNombre.focus()
        return

    txtUsuario.delete(0, END)
    nameDir = str(lstNombre[0]) + str(lstNombre[1])
    txtUsuario.insert(END, nameDir.lower())
    
#crear componente de GUI para captura de nombre
txtNombre = Entry(root)
txtNombre.place(x=240,y=80)
txtNombre.bind("<FocusOut>", lost_focus_nombre)

#crear componente Label de GUI para usuario
lblUsuario = Label(root, text="Usuario",width=20,font=("bold", 10))
lblUsuario.place(x=80,y= coordY +50)

'''
    Metodo valida usuario cuando pierde el foco
'''
def lost_focus_usuario(event):
    dirs = os.listdir(directory)
    for d in dirs:
        if d == txtUsuario.get().split():
            messagebox.showerror(message="Usuario ya existe!!!")
            txtUsuario.focus()
            return

#crear componente de GUI para captura de usuario
txtUsuario = Entry(root)
txtUsuario.place(x=240,y=coordY+50)
txtUsuario.bind("<FocusOut>", lost_focus_usuario)


lblEmail = Label(root, text="Email",width=20,font=("bold", 10))
lblEmail.place(x=68,y=coordY + 100)

#crear componente de GUI para captura de nombre
txtEmail = Entry(root)
isCorrecto = False
txtEmail.place(x=240,y=coordY + 100)

'''
    Metodo valida email cuando pierde el foco
'''
def lost_focus_email( event):
    lstEmail = txtEmail.get().split()
    
    if len(lstEmail) < 1:
        messagebox.showerror(message="El Email debe tener un valor")
        txtEmail.focus()
        isCorrecto = False
        return

    regex = r'\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}\b'
    if(not(re.fullmatch(regex, lstEmail[0]))):
        messagebox.showerror(message="El Email es incorrecto")
        txtEmail.focus()
        isCorrecto = False
        return
        
    isCorrecto = True

txtEmail.bind("<FocusOut>", lost_focus_email) #liga el evento con el mètodo

lblCelular = Label(root, text="Celular:",width=20,font=("bold", 10))
lblCelular.place(x=80,y=coordY + 150)

'''
    Metodo valida celular cuando pierde el foco
'''
def lost_focus_celular( event):
    lstCelular = txtCelular.get().split()
    print(len(lstCelular))
    if isCorrecto == False:
        txtEmail.focus()
        return 
    if len(lstCelular) < 1:
        messagebox.showerror(message="Ingresar un telèono correcto")
        txtCelular.focus()
        return

    regex = r'\b[0-9]{10}'
    if(not(re.fullmatch(regex, lstCelular[0]))):
        messagebox.showerror(message="El Telefono es incorrecto")
        txtCelular.focus()
        return

#crear componente de GUI para captura de nombre
txtCelular = Entry(root)
txtCelular.place(x=240,y=coordY + 150)
txtCelular.bind("<FocusOut>", lost_focus_celular) #liga el evento con el mètodo

lblImagen = Label(root,text="ello", width= 400)
lblImagen.place(x=500, y=10)

btnGuardar = Button(root, text='Guardar',width=20,bg='brown',fg='white', command=save_data)
btnGuardar.place(x=20,y=500)
btnCapturar = Button(root, text='Capturar',width=20,bg='brown',fg='white', command=iniciarCamara)
btnCapturar.place(x=170,y=500)
btnCapturar.place_forget()
Button(root, text='Salir',width=20,bg='brown',fg='white', command=cerrar).place(x=700,y=500)

iniciarCamara()

root.mainloop()

cv2.destroyAllWindows()
