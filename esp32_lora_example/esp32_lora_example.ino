/*********
  Rui Santos
  Complete project details at https://RandomNerdTutorials.com/ttgo-lora32-sx1276-arduino-ide/
*********/

//Libraries for LoRa
#include <SPI.h>
#include <LoRa.h>

//Libraries for OLED Display
#include <Wire.h>
#include <Adafruit_SSD1306.h>
#include "Adafruit_SHT31.h"

//define the pins used by the LoRa transceiver module
#define SCK 5
#define MISO 19
#define MOSI 27
#define SS 18
#define RST 14
#define DIO0 26

//433E6 for Asia
//866E6 for Europe
//915E6 for North America
#define BAND 866E6

//OLED pins
#define OLED_SDA 4
#define OLED_SCL 15 
#define OLED_RST 16
#define SCREEN_WIDTH 128 // OLED display width, in pixels
#define SCREEN_HEIGHT 64 // OLED display height, in pixels

// SHT31:
// SCL = 15, SDA = 4; same as with the OLED
// Default I2C address: 0x44

//packet counter
int counter = 0;

Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, OLED_RST);
Adafruit_SHT31 sht31 = Adafruit_SHT31();
String LoRaData;

#define LORA_SENDER 0
#define LORA_RECEIVER 1

#if (1 == LORA_SENDER) && (1 == LORA_RECEIVER)
#error board can't be both a sender and receiver
#endif

#if (LORA_SENDER)
void setup() {
    //initialize Serial Monitor
    Serial.begin(115200);
    Serial.println("LoRa Sender Test");
    
    //reset OLED display via software
    pinMode(OLED_RST, OUTPUT);
    digitalWrite(OLED_RST, LOW);
    delay(20);
    digitalWrite(OLED_RST, HIGH);

    //initialize OLED
    Wire.begin(OLED_SDA, OLED_SCL);
    if (!display.begin(SSD1306_SWITCHCAPVCC, 0x3c, false, false)) { // Address 0x3C for 128x32
        Serial.println(F("SSD1306 allocation failed"));
        for(;;); // Don't proceed, loop forever
    }

    display.clearDisplay();
    display.setTextColor(WHITE);
    display.setTextSize(1);
    display.setCursor(0,0);
    display.print("LORA SENDER ");
    display.display();

    //SPI LoRa pins
    SPI.begin(SCK, MISO, MOSI, SS);
    //setup LoRa transceiver module
    LoRa.setPins(SS, RST, DIO0);

    if (!LoRa.begin(BAND)) {
        Serial.println("Starting LoRa failed!");
        while (1);
    }
    Serial.println("LoRa Initializing OK!");
    display.setCursor(0,10);
    display.print("LoRa Initializing OK!");
    display.display();

    // Initialize SHT31 (SCL=21, SDA=22)
    if (! sht31.begin(0x44)) {
        Serial.println("Couldn't find SHT31");
        while(1);
    }
    else {
        Serial.println("Found SHT31");
    }

    delay(2000);
}

void loop() {
    // Get measurement
    float t = sht31.readTemperature();
    float h = sht31.readHumidity();
    // Output measurement to OLED
    Serial.print("Temp *C = "); Serial.println(t);
    Serial.print("Hum. % = "); Serial.println(h);
    display.setCursor(0, 20);
    display.printf("temperature: %.2f *C", t);
    display.setCursor(0, 30);
    display.printf("humidity: %.2f %%", h);
    display.setCursor(0, 40);
    display.print("hello there!");
    display.display();

    delay(1000);
    Serial.print("Sending packet: ");
    Serial.println(counter);

    // Send LoRa packet to receiver
    LoRa.beginPacket();
    LoRa.print("hello ");
    LoRa.println(counter);
    LoRa.printf("t=%.2f\n", t);
    LoRa.printf("h=%.2f\n", h);
    LoRa.endPacket();

    display.clearDisplay();
    display.setCursor(0,0);
    display.println("LORA SENDER");
    display.setCursor(0,20);
    display.setTextSize(1);
    display.print("LoRa packet sent.");
    display.setCursor(0,30);
    display.print("Counter:");
    display.setCursor(50,30);
    display.print(counter);      
    display.display();

    counter++;

    delay(10000);
}
#elif (LORA_RECEIVER)
void setup() {
    Serial.begin(115200);
    Serial.println("LoRa Receiver Test");

    // Reset OLED display via software
    pinMode(OLED_RST, OUTPUT);
    digitalWrite(OLED_RST, LOW);
    delay(20);
    digitalWrite(OLED_RST, HIGH);

    // Initialize I2C
    Wire.begin(OLED_SDA, OLED_SCL);

    // Initialize OLED
    if (!display.begin(SSD1306_SWITCHCAPVCC, 0x3c, false, false)) { // Address 0x3C for 128x32
        Serial.println(F("SSD1306 allocation failed"));
        for(;;); // Don't proceed, loop forever
    }

    display.clearDisplay();
    display.setTextColor(WHITE);
    display.setTextSize(1);
    display.setCursor(0,0);
    display.print("LORA RECEIVER ");
    display.display();

    // SPI LoRa pins
    SPI.begin(SCK, MISO, MOSI, SS);
    // Setup LoRa transceiver module
    LoRa.setPins(SS, RST, DIO0);

    if (!LoRa.begin(BAND)) {
        Serial.println("Starting LoRa failed!");
        while (1);
    }
    Serial.println("LoRa Initializing OK!");
    display.setCursor(0,10);
    display.println("LoRa Initializing OK!");
    display.display();  
}

void loop() {
    // Try to parse packet
    int packetSize = LoRa.parsePacket();
    if (packetSize) {
    //received a packet
    Serial.print("Received packet ");

    // Read packet
    while (LoRa.available()) {
        LoRaData = LoRa.readString();
        Serial.print(LoRaData);
    }

    // Print RSSI of packet
    int rssi = LoRa.packetRssi();
    Serial.print(" with RSSI ");    
    Serial.println(rssi);

    // Display information
    display.clearDisplay();
    display.setTextSize(1);
    display.setCursor(0,0);
    display.print("LORA RECEIVER");
    display.setCursor(0,10);
    display.print("Received packet:");
    display.setCursor(0,20);
    display.print(LoRaData);
    display.setCursor(0,50);
    display.print("RSSI:");
    display.setCursor(30,50);
    display.print(rssi);
    display.display();   
    }
}
#else
void setup() {
    #error Neither a sender nor a receiver
}

void loop() {

}
#endif
