## Notas para instalación 

- Instalar Home Assistant y configurarlo, debe de estar instalado python3 y pip, recuerda que también puedes usar un contenedor docker o una maquina virtual.
	
- Instalar ESPHOME (Puedes buscar un tutorial para el caso windows, recomiendo hacerlo en la RASPBERRY PI)
	
- Crear archivo yml para tu placa y configura la red y el switch que te permitirá encender y apagar el foco puedes utilizar el comando esphome wizard nombre_archivo.yml y ejecutalo utilizando el comando esphome run y el nombre de tu archivo.
	
- Añade a home assistant una integración colocando la ip del dispositivo a que obtienes al ejecutar esphome run archivo.yml, el puerto es el mismo por defecto.
	
- Esta explicación es a alto nivel, por lo que de acuerdo a la opción que decidan implementar puede tener detalles que tendríamos que cambiar para el correcto funcionamiento.
	
