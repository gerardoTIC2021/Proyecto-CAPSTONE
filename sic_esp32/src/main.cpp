/*
 * por: El Profe Tacho
 * Fecha: 10 de julio de 2022
 * 
 * Aplicación de la ESP32 que controla dispositivos fisicos, display de Matriz de 8x64, atención de clientes, fecha y hora en tiempo real, temperatura 
 * y humedad con registros en tiempo real.
 * (Algunas referencias y comentarios estan en ingles debido a que se comenzo con ese idioma se unificaran en actualizaciones) 
 * Componentes de Hardware
 *  - Modulo Matriz Led Max7219 Para Arduino 4 Módulos Rojo - 2 unidades
 *  - Nodemcu Esp32 Devkit V1 D0wdq6 Wifi Bluetooth 240mhz Ch9102x - 2 unidades
 *  - Cables Jumpers Dupont H-h, M-m, H-m 20cm 30 Pzas. Arduino - 1 unidad
 *  - Fuente Para Protoboard Mb102 5 Y 3.3v - 1 unidad
 *  - Base Shield Esp32 Esp32s Wifi Con Regulador 5v 2a - 1 unidad
 *  - Sensor Temperatura Y Humedad Dht11
 *  - Cargadores 5V - 2 Unidades
 *  - Cables Microusb Android Datos - 3 Unidades
 * DEPENDENCIAS ESP32: 
 *  - esp32-led-matrix-master
 *  - beegee-tokyo/DHT sensor library for ESPx@^1.18
 *  - mobizt/Firebase Arduino Client Library for ESP8266 and ESP32@^4.0.0
 *  - gypsyrobot/MusicBuzzer@^1.0.0
 *  - lbernstone/Tone32@^1.0.0
 *  - bblanchon/ArduinoJson@^6.19.4
 * 
 */

//Libraries WiFi
#if defined(ESP32)
  #include <WiFi.h>
#elif defined(ESP8266)
  #include <ESP8266WiFi.h>
#endif

//Arduino
#include <Arduino.h>

//JSON Data
#include <ArduinoJson.h>

//Time
#include "time.h"
#include "sntp.h"


//Libraries WebServer
#include <WebServer.h>
#include <SPI.h> 

//Libraries LED Matrix
#include "LedMatrix.h"
#include <MusicBuzzer.h>

//Constants
#define BUZZER_PIN 5

//Library firebase client esp32
#include <Firebase_ESP_Client.h>

//Library DHT for ESP32 
#include "DHTesp.h"
const int DHT_PIN = 4;
DHTesp dhtSensor;

//Provide the token generation process info.
#include "addons/TokenHelper.h"
//Provide the RTDB payload printing info and other helper functions.
#include "addons/RTDBHelper.h"

// Insert your network credentials
#define WIFI_SSID "!secret wifi_ssid"
#define WIFI_PASSWORD "!secret wifi_password"

//Time constants
const char* ntpServer1 = "pool.ntp.org";
const char* ntpServer2 = "time.nist.gov";
const long  gmtOffset_sec = 3600;
const int   daylightOffset_sec = 3600;
const char* time_zone = "CST6CDT,M4.1.0,M10.5.0";  // TimeZone rule for Mexico


// Insert Firebase project API Key
#define API_KEY "!secret api key"

// Insert RTDB URLefine the RTDB URL */
#define DATABASE_URL "URL to database" 

//Define Firebase Data object
FirebaseData fbdo;

FirebaseAuth auth;
FirebaseConfig config;

unsigned long sendDataPrevMillis = 0;
int count = 0;
bool signupOK = false;

//Constants LedMatrix connection
#define NUMBER_OF_DEVICES 8 //number of led matrix connect in series
#define CS_PIN 15
#define CLK_PIN 18
#define MISO_PIN 19 //we do not use this pin just fill to match constructor
#define MOSI_PIN 23

//Object LEDMATRIX
LedMatrix ledMatrix = LedMatrix(NUMBER_OF_DEVICES, CLK_PIN, MISO_PIN, MOSI_PIN, CS_PIN);


//Constants Electronic Components
const int ENTER_PIN = 32;
const int EXIT_PIN = 33;
const int HALLWAY_PIN = 25;
const int WC_PIN = 27;
const int PAYMENT_PIN = 26;
const int STORE_PIN = 14;
const int AIR_PIN = 12;
const int LAMP_PIN = 13;

//Start webserver object
WebServer server(80);

//Variables changing status
int status = 0;
int intValue;


//Initial values
const char* user = "Anonimo";
const char* sensor = "No data";
char date[80];

//Object to manages json
DynamicJsonDocument doc(1024);

//Función para imprimir el horario y fechas actuales 
const char* printLocalTime()
{
  struct tm timeinfo;
  if(!getLocalTime(&timeinfo)){
    Serial.println("No time available (yet)");
    return "";
  }

  strftime(date, sizeof(date), "%A, %B %d %Y %H:%M", &timeinfo);  //

  //Datetime to display in LedMatrix
  //sprintf(str, "%A, %B %d %Y %H:%M:%S", &timeinfo);
  Serial.println(&timeinfo, "%A, %B %d %Y %H:%M");

  return (date);

}

// Callback function (get's called when time adjusts via NTP)
void timeavailable(struct timeval *t)
{
  Serial.println("Got time adjustment from NTP!");
  printLocalTime();
}


//Get status of attendant method.
void getStatus(){
 String data = "{\"status\":\""+String(status)+"\", \"user\":\""+String(user)+"\"}";
 server.send(200, "application/json", data); 
}

//Init display of the LedMatrix method
void initDisplay(String text){
   ledMatrix.init();
   ledMatrix.setText(text);
}

//Changes attendand method
void powerOn(){
  status = 1;
  Serial.println(server.arg(0));
  String json = server.arg(0);
  DeserializationError error = deserializeJson(doc, json);
  if (error){
      return;
  }
  const char* user1 = doc["user"];
  String data = "{\"status\":\""+String(status)+"\", \"user\":\""+String(user1)+"\"}";
  server.send(200, "application/json", data);
  initDisplay("Atender a: "+String(user1) +" - "+ printLocalTime());


}

//Changes attention method
void powerOff(){
  status = 0;
  Serial.println(server.args());
  initDisplay("Atendido a: "+String(user));
  String data = "{\"status\":"+String(status)+"}";
  server.send(200, "application/json", data);
}


//Method of config and setup wifi
void setupWifi() {
  Serial.print("Start connect WIFi Access");
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

  while(WiFi.status() != WL_CONNECTED){
    delay(500);
    Serial.print("..");

  }
  Serial.print("IP: ");
  Serial.println(WiFi.localIP());

}

//Activate the dht sensor
void setupDHT(){
  /* Assign the api key (required) */
  config.api_key = API_KEY;

  /* Assign the RTDB URL (required) */
  config.database_url = DATABASE_URL;

  /* Sign up */
  if (Firebase.signUp(&config, &auth, "", "")){
    Serial.println("ok");
    signupOK = true;
  }
  else{
    Serial.printf("%s\n", config.signer.signupError.message.c_str());
  }

  /* Assign the callback function for the long running token generation task */
  config.token_status_callback = tokenStatusCallback; //see addons/TokenHelper.h

  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);

  /* DHT11 */
  dhtSensor.setup(DHT_PIN, DHTesp::DHT11);
}

//Método de configuración y puesta en marcha de la funcionalidad de hora y fecha actuales
void setupTime(){
// set notification call-back function
  sntp_set_time_sync_notification_cb( timeavailable );

  /**
   * NTP server address could be aquired via DHCP,
   *
   * NOTE: This call should be made BEFORE esp32 aquires IP address via DHCP,
   * otherwise SNTP option 42 would be rejected by default.
   * NOTE: configTime() function call if made AFTER DHCP-client run
   * will OVERRIDE aquired NTP server address
   */
  sntp_servermode_dhcp(1);    // (optional)

  /**
   * This will set configured ntp servers and constant TimeZone/daylightOffset
   * should be OK if your time zone does not need to adjust daylightOffset twice a year,
   * in such a case time adjustment wont be handled automagicaly.
   */

  configTime(gmtOffset_sec, daylightOffset_sec, ntpServer1, ntpServer2);

  /**
   * A more convenient approach to handle TimeZones with daylightOffset 
   * would be to specify a environmnet variable with TimeZone definition including daylight adjustmnet rules.
   * A list of rules for your zone could be obtained from https://github.com/esp8266/Arduino/blob/master/cores/esp8266/TZ.h
   */
      
  configTzTime(time_zone, ntpServer1, ntpServer2);

}

//Config and init the Webserver
void setupWebServer(){
  Serial.print("Start server setup");
  server.on("/", getStatus);
  server.on("/on", powerOn);
  server.on("/off", powerOff);
  server.on("/att-on", attendOn);
  server.on("/att-off", attendOff);

  server.begin();
  Serial.println("... started");
}


//Change display text into LedMatrix
void  display(){
  ledMatrix.clear();
  ledMatrix.scrollTextLeft();
  ledMatrix.drawText();
  ledMatrix.commit();
  //delay(50);
}

//Change Firebase realtime database of data sensors. 
void dhtDataFirebase(){
  TempAndHumidity  data = dhtSensor.getTempAndHumidity();
  Serial.println("Temp: " + String(data.temperature, 2) + "°C");
  Serial.println("Humidity: " + String(data.humidity, 1) + "%");
  Serial.println("---");


  //Actualización de los datos para firebase 
  if (Firebase.ready() && signupOK && (millis() - sendDataPrevMillis > 15000 || sendDataPrevMillis == 0)){
    sendDataPrevMillis = millis();
    // Write an Int number on the database path test/int
    if (Firebase.RTDB.setInt(&fbdo, "temperature", round(data.temperature))){
      Serial.println("PASSED");
      Serial.println("PATH: " + fbdo.dataPath());
      Serial.println("TYPE: " + fbdo.dataType());
    }
    else {
      Serial.println("FAILED");
      Serial.println("REASON: " + fbdo.errorReason());
    }
    count++;

    // Write an Float number on the database path test/float
    if (Firebase.RTDB.setInt(&fbdo, "humidity", round(data.humidity))){
      Serial.println("PASSED");
      Serial.println("PATH: " + fbdo.dataPath());
      Serial.println("TYPE: " + fbdo.dataType());
    }
    else {
      Serial.println("FAILED");
      Serial.println("REASON: " + fbdo.errorReason());
    }



    if (Firebase.RTDB.getInt(&fbdo, "ventilador")) {
      if (fbdo.dataType() == "int") {
        intValue = fbdo.intData();
        Serial.println("Ventilador:"+String(intValue));
        digitalWrite(AIR_PIN, intValue);
      }
    }
    else {
      Serial.println(fbdo.errorReason());
    }

    if (Firebase.RTDB.getInt(&fbdo, "entrada")) {
      if (fbdo.dataType() == "int") {
        intValue = fbdo.intData();
        Serial.println("Entrada:"+String(intValue));
        digitalWrite(ENTER_PIN, intValue);
      }
    }
    else {
      Serial.println(fbdo.errorReason());
    }


    if (Firebase.RTDB.getInt(&fbdo, "salida")) {
      if (fbdo.dataType() == "int") {
        intValue = fbdo.intData();
        Serial.println("Salida:"+String(intValue));
        digitalWrite(EXIT_PIN, intValue);
      }
    }
    else {
      Serial.println(fbdo.errorReason());
    }


    if (Firebase.RTDB.getInt(&fbdo, "pasillo")) {
      if (fbdo.dataType() == "int") {
        intValue = fbdo.intData();
        Serial.println("Pasillo:"+String(intValue));
        digitalWrite(HALLWAY_PIN, intValue);
      }
    }
    else {
      Serial.println(fbdo.errorReason());
    }

     if (Firebase.RTDB.getInt(&fbdo, "cajas")) {
      if (fbdo.dataType() == "int") {
        intValue = fbdo.intData();
        Serial.println("Cajas:"+String(intValue));
        digitalWrite(PAYMENT_PIN, intValue);
      }
    }
    else {
      Serial.println(fbdo.errorReason());
    }

     if (Firebase.RTDB.getInt(&fbdo, "banio")) {
      if (fbdo.dataType() == "int") {
        intValue = fbdo.intData();
        Serial.println("Baños:"+String(intValue));
        digitalWrite(WC_PIN, intValue);
      }
    }
    else {
      Serial.println(fbdo.errorReason());
    }

     if (Firebase.RTDB.getInt(&fbdo, "almacen")) {
      if (fbdo.dataType() == "int") {
        intValue = fbdo.intData();
        Serial.println("Almacen:"+String(intValue));
        digitalWrite(STORE_PIN, intValue);
      }
    }
    else {
      Serial.println(fbdo.errorReason());
    }

     if (Firebase.RTDB.getInt(&fbdo, "lampara")) {
      if (fbdo.dataType() == "int") {
        intValue = fbdo.intData();
        Serial.println("Lampara:"+String(intValue));
        digitalWrite(LAMP_PIN, intValue);
      }
    }
    else {
      Serial.println(fbdo.errorReason());
    }





  }
}

//Begin configurations
void setup() {

  // Configuration serial
  Serial.begin(115200);
  //Setup WiFi
  setupWifi();
  delay(100);

  //Setup web server
  setupWebServer();
  delay(100);

  //Setup DHT11
  setupDHT();
  delay(100);

  //setup Time
  setupTime();
  delay(100);

  Serial.println("Setup done");

  //pinMode(LED_PIN, OUTPUT);
  pinMode(ENTER_PIN, OUTPUT);
  pinMode(EXIT_PIN, OUTPUT);
  pinMode(HALLWAY_PIN, OUTPUT);
  pinMode(WC_PIN, OUTPUT);
  pinMode(PAYMENT_PIN, OUTPUT);
  pinMode(STORE_PIN, OUTPUT);
  pinMode(AIR_PIN, OUTPUT);
  pinMode(LAMP_PIN, OUTPUT);


  music.init(BUZZER_PIN);

  //Display
  initDisplay(printLocalTime());

}


//Método que se ejecuta repetidamente.
void loop() {

  //Relay working
  if(status==1){
    //digitalWrite(LED_PIN, HIGH);
    printLocalTime();
    display();

  }else if(status == 0){
      //digitalWrite(LED_PIN, LOW);
      display();
  }



  //Server
  server.handleClient();
  //delay(100);

  //DHT
  dhtDataFirebase();

  printLocalTime();

  //Wait...
  //delay(1000);
}
