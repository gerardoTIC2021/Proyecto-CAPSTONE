# Notas para instalación por el Profe Tacho (Anastacio Rodríguez García)

- Instalar Home Assistant y configurarlo, debe de estar instalado python3 y pip, recuerda que también puedes usar un contenedor docker o una maquina virtual.

![image](https://user-images.githubusercontent.com/36056832/183044879-a578a53c-d882-44cf-9149-6f3c9011bd4f.png)
	
- Instalar ESPHOME (Puedes buscar un tutorial para el caso windows, recomiendo hacerlo en la RASPBERRY PI)
	### pip install esphome	
- Crear archivo yml para tu placa y configura la red y el switch que te permitirá encender y apagar el foco puedes utilizar el comando esphome wizard nombre_archivo.yml y ejecutalo utilizando el comando esphome run y el nombre de tu archivo.
https://github.com/gerardoTIC2021/Proyecto-CAPSTONE/blob/main/ESPHOME_ASSISTANT/esp32.yml
	
- Añade a home assistant una integración colocando la ip del dispositivo a que obtienes al ejecutar esphome run archivo.yml, el puerto es el mismo por defecto.

![image](https://user-images.githubusercontent.com/36056832/183045836-adca45bb-ebc5-40c0-a1cf-35efd0ff06ce.png)

![image](https://user-images.githubusercontent.com/36056832/183046341-9dfa69b3-7671-43bf-b007-28152417f205.png)


	
- Esta explicación es a alto nivel, por lo que de acuerdo a la opción que decidan implementar puede tener detalles que tendríamos que cambiar para el correcto funcionamiento.

![image](https://user-images.githubusercontent.com/36056832/183046684-c6c1ae08-6273-4c0c-87bc-b0eef16bffc3.png)
	
