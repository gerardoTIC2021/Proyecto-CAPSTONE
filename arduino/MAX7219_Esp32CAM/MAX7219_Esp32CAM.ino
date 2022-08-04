 /*
 * Conexión MAX7219 con ESP32CAM
 * por: Anastasio Rodrìguez Garcia, Gabriel Barròn Rodrìguez, Gerardo Reyna Ibarra
 * Fecha: 11 de julio de 2022
 * 
 * Este programa se conecta a una red Wifi mediante las credenciales de la propia red
 * El programa recibe datos a través del protocolo MQTT. 
 * Es necesario usar NodeRed para visualzar la información se está recibiendo correctamente.
 * Este programa no requiere componentes adicionales.
 * 
 * MAX7219     PinESP32CAM
 * Vcc------------ 3.3V
 * GND------------ GND
 * DIN ------------ 13
 * CS ------------- 15
 * CLK--------------14
 */
 
// Librerias requeridas:
#include "esp_camera.h"  //Es necesario para la activacion y detecciòn de la camara
#include <MD_Parola.h>   //Es necesario para el envìo de mensajes a LEDMATRIX
#include <MD_MAX72xx.h>  //Necesaria para el actuador de despliegue de mensajes
#include <SPI.h> 
#include <WiFi.h>  // Biblioteca para el control de WiFi
#include <PubSubClient.h> //Biblioteca para conexion MQTT

//Modelo de càmara que se activarà en la ESP32CAM
#define CAMERA_MODEL_AI_THINKER // Has PSRAM
#include "camera_pins.h"  //Archivo que define los pins de la ESP32CAM

// Define tipo hardware, tamaño, y pines de salida:
#define HARDWARE_TYPE MD_MAX72XX::FC16_HW
#define MAX_DEVICES 4 //Son cuatro led matrix

// Setup para software SPI en configuracion de LED MATRIX en la ESP32CAM:
#define CS_PIN 15
#define DATA_PIN 13
#define CLK_PIN 14

// Crea una nueva instancia de la clase MD_Parola con conexion hardware SPI:
MD_Parola myDisplay = MD_Parola(HARDWARE_TYPE, DATA_PIN, CLK_PIN, CS_PIN, MAX_DEVICES);
#define BUF_SIZE 50

char mensaje[BUF_SIZE] = {"Bienvenido "}; //mensaje inicial que se despliega en la LED MATRIX
char bienvenido[BUF_SIZE] = {"Bienvenido"};

//Datos de Conexion WiFi
const char* ssid = "UTNG_Docencia";
const char* password = "UtNg2020@#$";
const char* tema = "capstone/utng/ledmatrix";  //tema para desplegar mensaje de la LED MATRIX
long lastMsg = 0;

int value = 0;

//Datos del broker MQTT
const char* mqtt_server = "172.16.2.189"; // Si estas en una red local, coloca la IP asignada, en caso contrario, coloca la IP publica
IPAddress server(172,16,2,189);

// Objetos
WiFiClient espClient; // Este objeto maneja los datos de conexion WiFi
PubSubClient client(espClient); // Este objeto maneja los datos de conexion al broker

// Variables
long timeNow, timeLast; // Variables de control de tiempo no bloqueante
int data = 0; // Contador
int wait = 5000;  // Indica la espera cada 5 segundos para envío de mensajes MQTT

void startCameraServer();  //funcion que inicia la camara

void setup() {
  // Iniciar comunicación serial
  Serial.begin (115200);

  //Configuracion de la ESP32
  camera_config_t config;
  config.ledc_channel = LEDC_CHANNEL_0;
  config.ledc_timer = LEDC_TIMER_0;
  config.pin_d0 = Y2_GPIO_NUM;
  config.pin_d1 = Y3_GPIO_NUM;
  config.pin_d2 = Y4_GPIO_NUM;
  config.pin_d3 = Y5_GPIO_NUM;
  config.pin_d4 = Y6_GPIO_NUM;
  config.pin_d5 = Y7_GPIO_NUM;
  config.pin_d6 = Y8_GPIO_NUM;
  config.pin_d7 = Y9_GPIO_NUM;
  config.pin_xclk = XCLK_GPIO_NUM;
  config.pin_pclk = PCLK_GPIO_NUM;
  config.pin_vsync = VSYNC_GPIO_NUM;
  config.pin_href = HREF_GPIO_NUM;
  config.pin_sscb_sda = SIOD_GPIO_NUM;
  config.pin_sscb_scl = SIOC_GPIO_NUM;
  config.pin_pwdn = PWDN_GPIO_NUM;
  config.pin_reset = RESET_GPIO_NUM;
  config.xclk_freq_hz = 20000000;
  config.pixel_format = PIXFORMAT_JPEG;

  // si PSRAM IC present, 
  if(psramFound()){
    config.frame_size = FRAMESIZE_UXGA;
    config.jpeg_quality = 10;
    config.fb_count = 2;
  } else {
    config.frame_size = FRAMESIZE_SVGA;
    config.jpeg_quality = 12;
    config.fb_count = 1;
  }

  #if defined(CAMERA_MODEL_ESP_EYE)
    pinMode(13, INPUT_PULLUP);
    pinMode(14, INPUT_PULLUP);
  #endif

  // inicia camara
  esp_err_t err = esp_camera_init(&config);
  if (err != ESP_OK) {
    Serial.printf("Inicializacion fallo con codigo de error 0x%x", err);
    return;
  }

  sensor_t * s = esp_camera_sensor_get();
  // initial sensors are flipped vertically and colors are a bit saturated
  if (s->id.PID == OV3660_PID) {
    s->set_vflip(s, 1); // flip it back
    s->set_brightness(s, 1); // up the brightness just a bit
    s->set_saturation(s, -2); // lower the saturation
  }
  // drop down frame size for higher initial frame rate
  s->set_framesize(s, FRAMESIZE_QVGA);

  #if defined(CAMERA_MODEL_M5STACK_WIDE) || defined(CAMERA_MODEL_M5STACK_ESP32CAM)
    s->set_vflip(s, 1);
    s->set_hmirror(s, 1);
  #endif

  setup_wifi(); //funcion que configura el wifi

  // Conexión con el broker MQTT
  client.setServer(server, 1883); // Conectarse a la IP del broker en el puerto indicado
  client.setCallback(callback); // Activar función de CallBack, permite recibir mensajes MQTT y ejecutar funciones a partir de ellos

  timeLast = millis (); // Inicia el control de tiempo
      
  myDisplay.begin();   // Inicializa el objeto Display
  myDisplay.setIntensity(0); // Establece brillo (0-15)
  myDisplay.displayClear(); // Limpia el Display
  myDisplay.displayText(mensaje, PA_CENTER, 100, 0, PA_SCROLL_LEFT, PA_SCROLL_LEFT);
}


void setup_wifi() {
  delay(10);
  // Se inicia conectando a la red WiFi
  Serial.println();
  Serial.print("Conectando a ");
  Serial.println(ssid);


  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  startCameraServer(); //Funciona inicia nuevamente la camara
  
  Serial.print("Camara Lista! Use 'http://");
  Serial.print(WiFi.localIP());
  Serial.println("' a conectar");
}


void loop() {

    if (myDisplay.displayAnimate()) {
        myDisplay.displayReset();
    }

  //Verificar siempre que haya conexión al broker
  if (!client.connected()) {
    reconnect();  // En caso de que no haya conexión, ejecutar la función de reconexión, definida despues del void setup ()
  }// fin del if (!client.connected())

  client.loop(); // Esta función es muy importante, ejecuta de manera no bloqueante las funciones necesarias para la comunicación con el broker

}

// Esta función permite tomar acciones en caso de que se reciba un mensaje correspondiente a un tema al cual se hará una suscripción
void callback(char* topic, byte* message, unsigned int length) {

  Serial.print("Message arrived on topic: ");
  Serial.print(topic);
  Serial.print(". Message: ");
  String messageTemp;
  
  for (int i = 0; i < length; i++) {
    Serial.print((char)message[i]);
    messageTemp += (char)message[i];
  }
  Serial.println();


  // Si una mensaje es recibido al tema capstone/utng/ledmatrix y si no esta vacio
  // le concatena a Bienvenido el texto que le sigue. Por ejemplo, Bienvenido Luis Juarez
  if (String(topic) == "capstone/utng/ledmatrix") {
    if(messageTemp != ""){
      char newMensaje[BUF_SIZE] = {""};      
      static char *cp = newMensaje;

     for (int i = 0; i < strlen(bienvenido); i++) { //Itera sobre todos los caracteres
       *cp = bienvenido[i];
       cp++;
     }
     *cp = ' '; 
     cp++;
     *cp = ' ';
     cp++;
     //Se recorre mediante apuntadores los caracteres de la cadena
      for (int i = 0; i < length; i++) {
        *cp = messageTemp[i];
        cp++;
      }
      *cp = '\0'; //Asigna salto de lìnea
      cp = newMensaje;
      strcpy(mensaje, newMensaje);
      
      Serial.print("Nuevo mensaje: ");
      Serial.println(messageTemp);
      Serial.print("Longitud: ");
      Serial.println(strlen(mensaje));

      myDisplay.displayClear(); // Limpia el Display
      myDisplay.displayReset();
    }
  }
        
}// fin del void callback

// Función para reconectarse
void reconnect() {
  // Bucle hasta lograr conexión
  while (!client.connected()) { // Pregunta si hay conexión
    Serial.print("Tratando de conectarse...");
    // Intentar reconexión
    if (client.connect("ESP32_MAXLED")) { //Pregunta por el resultado del intento de conexión
      Serial.println("Conectado");
      client.subscribe("capstone/utng/ledmatrix"); // Esta función realiza la suscripción al tema
    }
    else {  //en caso de que la conexión no se logre
      Serial.print("Conexion fallida, Error rc=");
      Serial.print(client.state()); // Muestra el codigo de error
      Serial.println(" Volviendo a intentar en 5 segundos");
      // Espera de 5 segundos bloqueante
      delay(5000);
      Serial.println (client.connected ()); // Muestra estatus de conexión
    }// fin del else
  }// fin del bucle while (!client.connected())
}// fin de void reconnect(
