# Proyecto CapStone UTNG
[![Contribuidores][contribuidores-shield]][contributors-url]

## contenido
<details>
  <summary>Tabla contenidos</summary>
  <ol>
    <li>
      <a href="#acerca-del-proyecto">Acerca Del Proyecto</a>
      <ul>
        <li><a href="#construido-con">Construido Con</a></li>
      </ul>
    </li>
    <li>
      <a href="#iniciando">Iniciando</a>
      <ul>
        <li><a href="#requisitos">Prerrequisitos</a></li>
        <li><a href="#instalacion">Instalación</a></li>
      </ul>
    </li>
    <li><a href="#uso">Uso</a></li>
    <li><a href="#guias">Guias</a></li>
    <li><a href="#contribucion">Contribución</a></li>
    <li><a href="#licencia">licencia</a></li>
    <li><a href="#contacto">Contacto</a></li>
    <li><a href="#participantes">Participantes</a></li>
  </ol>
</details>

<!-- Acerca del proyecto -->
## Acerca del proyecto

### Nombre del proyecto
**Smart Craft Sales Dolores Hidalgo**

### Justificación del proyecto

> El mejor servicio al cliente que podemos tener, es no tener la necesidad de usar el servicio al cliente.

> El servicio al cliente bajo la óptica del Internet de las Cosas se vuelve una actividad esencial, automática, proactiva y constante. El mejor servicio al cliente es aquel que logramos visualizar antes que los clientes noten el problema; y para ello, necesitas modificar tus operaciones y flujos de trabajo.

> Dolores Hidalgo se encuentra en la zona norte del estado de Guanajuato, pocas ciudades del país pueden decir que viven completamente de la artesanía. El centro de  Dolores Hidalgo, Guanajuato, encontrarás calles repletas de todo tipo de objetos artesanales cerámicos. Además de ser cuna de la Independencia Nacional, Dolores Hidalgo es una población fantástica donde innumerables alfareros y ceramistas han hecho de la cerámica de talavera su forma de vida al trabajarla en diversas formas  y tonos multicolores que engalanan al México popular país con un hondo espíritu folclórico.
> El principal turista de ocio que llega a la Cuna de la Independencia, es de tipo familiar por el potencial que tenemos en el personaje de José Alfredo Jiménez y la cerámica tipo Talavera, pero una de los grandes incovenientes es que el turista dura en la ciudad de uno a dos días y después se van a otras ciudades como Guanajuato, San Miguel de Allende, San Luis de la Paz. Generalmente, los pequeños negocios de artesanías que comercializan los productos de Talavera contratan a personal femenino para el servicio de atención al cliente, pero una de las grandes inquietudes que tienen los empresarios de los estos comercios es usar la tecnología para atrapar al turista e incrementar sus ventas. 

### Objetivo del proyecto: 

> Impulsar el incremento de ventas de los pequeños comercios de comercializan Talavera y Cerámica mediante el uso de la tecnología del Internet de las Cosas que ayuden a eficientar la atención al cliente y una experiencia agradable en el comercio.

### Descripción general del proyecto

> Este proyecto consiste generar un prototipo que propone incrementar ventas mediante experiencia de usuario agradables en los comercios de artesanías en la ciudad de Dolores Hidalgo CIN,  a través de un sistema IoT. Consta de sensores que capturan datos del entorno como el rostro del cliente, lectura de QR y realidad aumentada para conocer la descripción y detalles del producto; así como actuadores que responderán para dar la bienvenida al usuario, cuando un usuario solicite atención enviará notificaciones para recibir atención del Usuario.
> El dueño del establecimiento podrá observar estadísticas mediante análisis de datos sobre el número de clientes que ingresan al establecimiento, los clientes que lograron realizar la venta y la salida de satisfacción del cliente. 


### Material de uso:
<table> <tr> <th>Componente</th><th>Imagen</th><th>Descripción</th><th>Cantidad</th></tr>
  <tr>
    <td>Raspberry Pi 4</td>
    <td>
    <img src="https://user-images.githubusercontent.com/36056832/165995675-da53df0d-9c67-46af-94e3-22f30ad2aa4d.png" alt="Raspberry Pi 4" width="100"/>
    </td>
     <td><ul><li>Raspberry Pi 4 Modelo B 8GB RAM Original Uk.<li>Memoria microSD 32GB Clase 10 A1 con adaptador.<li>Cable microHDMI a HDMI 1.5m.<li>Carcasa con soporte para ventilador.<li>Ventilador 5VDC con tornillos y tuercas.<li>Fuente 5V 3A USB-C con Switch On/Off.<li>Kit de disipadores de calor cobre y aluminio con cinta térmica adherible.</ul></td>
    <td>1</td>
  </tr> 
  <tr>
    <td>ESP32 Cam</td>
    <td>
    <img src="https://user-images.githubusercontent.com/36056832/165996397-e9527b13-6ad4-4e73-a732-7721e0ffac59.png"  alt="ESP32 Cam" width="100"/>
    </td>
    <td><ul><li>Voltaje: 5 V<li>CPU de 32 bits de doble núcleo de baja potencia para procesadores de aplicaciones<li>Frecuencia principal de hasta 240 MHz<li>Potencia de cálculo de hasta 600 DMIPS<li>SRAM integrado de 520 KB<li>PSRAM externo de 4 M<li>Es ideal para interfaces como: UART. SPI. I2C. PWM. ADC. DAC<li>Soporta cámaras OV2640 y OV7670 (flash incorporado)<li>Apoyo imagen WiFi subir<li>Apoyo TF tarjeta</ul></td>
    <td>1</td>
  </tr> 
  <tr>
    <td>Display LCD 16x2</td>
    <td><img src="https://user-images.githubusercontent.com/8560750/166756741-813b8a4a-d952-4e20-aa89-b7699ffeb30d.jpg" alt="Lcd Display" width="100"/></td>
    <td>
      <ul>
        <li>Voltaje: 5 V</li>
        <li>Un pin de selección de registro (RS) que controla en qué parte de la memoria de la pantalla LCD está escribiendo datos. </li>
        <li>Un pin de lectura/escritura (R/W) que selecciona el modo de lectura o el modo de escritura</li>
        <li>Un pin Habilitar que permite escribir en los registros</li>
        <li>8 pines de datos (D0 -D7) . Los estados de estos pines (alto o bajo) son los bits que estás escribiendo en un registro cuando escribes, o los valores que estás leyendo cuando lees,</li>
      </ul>
    </td>
    <td>1</td>
  </tr> 
  <tr>
    <td>Lector QR </td>
    <td><img src="https://user-images.githubusercontent.com/8560750/166768412-b0cddffc-1a08-49cf-a9d5-4d46b3501678.jpg" width="100" alt="Lector de código QR"/> </td>
    <td>
      <ul>
        <li>Dispositivo móvil con sistema operativo Android 8.0 o superior.</li>
        <li>App para lectura de QR.</li>
        <li>Observar detalles y características de producto.</li>
      </ul>
      </td>
    <td>2</td>
  </tr>
  <tr>
    <td>App de comercio de AR </td>
    <td><img src="https://user-images.githubusercontent.com/8560750/166763820-c178c82a-4f1f-47fb-9e15-34011bad59f1.jpg" width="100" alt="App de realidad aumentada"/></td>
    <td>
      <ul>
        <li>Dispositivo móvil con sistema operativo Android 8.0 o superior.</li>
        <li>App de establecimiento.</li>
        <li>Interactuar con detalles y características de producto.</li>
      </ul>    
    </td>
    <td>2 </td>
  </tr>
  <tr>
    <td>ISD1820 </td>
    <td><img src="https://user-images.githubusercontent.com/8560750/166772317-71c55285-568d-478c-aa4a-080df56c719b.jpg" width="100" alt="ISD1820"/></td>
    <td>
      <ul>
        <li>Tipo: Módulo grabador de voz y parlante</li>
        <li>Dimensiones: 42.87mmx 38.55mm x 11.25mm</li>
        <li>Voltaje de funcionamiento: 3V a 5V DC</li>
        <li>Altavoz: 8Ω 0.5 W</li>
        <li>Bocina de 8ohms a 0.5W</li>
      </ul>
    </td>
    <td>1</td>
  </tr> 

</table>

#### Requerimientos
|No.  |Requerimiento  |
|---  |-------------  |
| 1   |El sistema deberá permitir dar la bienvenida al cliente o visitante a través de lectura de rostro, si la persona es un cliente mediante un Display y/o un repetidor de Voz dará la bienvenida. A su vez, el empleado recibirá una notificación a su dispositivo móvil para su atención.               |
| 2   |Mediante lectura de código QR a través de dispositivo móvil la persona podrá observar la descripción del producto. |
| 3   |Mediante realidad aumentada a través de dispositivo móvil el cliente o turista podrá interactuar con las características y uso del producto.|
| 4   |La persona podrá solicitar la atención del empleado a través de su dispositivo móvil ya sea para muestra del producto o venta del mismo.|
| 5   |La persona podrá responder la encuesta de salida si fue agradable el servicio al cliente, etc.|
| 6   |El gerente o dueño del negocio podrá ver estadísticas como: Número de visitantes que llegaron al establecimiento. Número de visitantes que realizaron compra. Encuesta de satisfacción de salida.  |

#### Diagrama inicial
![image](https://user-images.githubusercontent.com/8560750/166749650-89fba768-4bbc-45c4-93d3-df9e5d08ad4c.png)


### Prototipo


<!-- Construido con -->


<!-- Construido con -->
### Construido con
Construido con.

<!-- Iniciando -->
## Iniciando
Iniciando.

<!-- Requisitos -->
### Requisitos
Requisitos.

<!-- Instalación -->
### Instalacion
Instalación.

<!-- Uso -->
### Uso
Uso.

## Guias
Guias.

## contribucion
Contribucion.

## Licencia
Licencia.

## Contacto
Contacto.

## Participantes
* [Gerardo Reyna Ibarra]()
* [Anastasio Rodríguez García]()
* [Gabriel Barrón Rodríguez]()

[contribuidores-shield]: https://img.shields.io/github/contributors/github_username/repo_name.svg?style=for-the-badge
[contributors-url]: https://github.com/github_username/repo_name/graphs/contributors
