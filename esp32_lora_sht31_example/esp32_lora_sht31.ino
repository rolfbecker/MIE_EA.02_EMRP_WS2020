/* References
 * https://primalcortex.wordpress.com/2017/11/24/the-esp32-oled-lora-ttgo-lora32-board-and-connecting-it-to-ttn/
 * https://randomnerdtutorials.com/esp32-ssd1306-oled-display-arduino-ide/
*/

// This file is built upon the esp32_lorawan_example/esp32_lorawan_example.ino file for basic lora example refer to that file

// ******************************************************
// IMPORANT:
// edit the defines in <C:\Users\YourName\Documents\Arduino\libraries\MCCI_LoRaWAN_LMIC_library\project_config\lmic_project_config.h> ..
// .. to choose CFG_eu868 as the band to be used
// ******************************************************

// ******************************************************
// Libraries used in this sketch (downloadable from the Library Manager):
// - Adafruit's SSD1306 display driver: https://github.com/adafruit/Adafruit_SSD1306
// - Arduino-LMIC library: https://github.com/mcci-catena/arduino-lmic
// Board name: "TTGO  LoRa32-OLED V1"
// ******************************************************

#include <Adafruit_SSD1306.h>
#include <splash.h>
#include <Wire.h>
#include "Adafruit_SHT31.h"
#include "lmic.h"
#include "hal/hal.h"
#include <CayenneLPP.h>



#define LEDPIN 2

#define SCK     5    // GPIO5  -- SX1278's SCK
#define MISO    19   // GPIO19 -- SX1278's MISnO
#define MOSI    27   // GPIO27 -- SX1278's MOSI
#define SS      18   // GPIO18 -- SX1278's CS
#define RST     14   // GPIO14 -- SX1278's RESET
#define DI0     26   // GPIO26 -- SX1278's IRQ(Interrupt Request)
#define BAND  868E6

//OLED pins
#define OLED_SDA 4
#define OLED_SCL 15 
#define OLED_RST 16
#define SCREEN_WIDTH 128 // OLED display width, in pixels
#define SCREEN_HEIGHT 64 // OLED display height, in pixels

// Pin mapping
const lmic_pinmap lmic_pins = {
    .nss = 18,
    .rxtx = LMIC_UNUSED_PIN,
    .rst = 14,
    .dio = {26, 33, 32}  // Pins for the Heltec ESP32 Lora board/ TTGO Lora32 with 3D metal antenna
};

#define TX_INTERVAL 120 // Schedule TX every this many seconds (might become longer due to duty // cycle limitations).
static uint16_t counter = 0;
static osjob_t sendjob;
static char TTN_response[30];

CayenneLPP lpp(51);

Adafruit_SSD1306 display(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, OLED_RST);

Adafruit_SHT31 sht31 = Adafruit_SHT31();


// This EUI must be in little-endian format, so least-significant-byte
// first. When copying an EUI from ttnctl output, this means to reverse
// the bytes. For TTN issued EUIs the last bytes should be 0xD5, 0xB3, 0x70.
static const u1_t PROGMEM APPEUI[8] = { 0xBF, 0x95, 0x03, 0xD0, 0x7E, 0xD5, 0xB3, 0x70 };
void os_getArtEui (u1_t* buf) {
  memcpy_P(buf, APPEUI, 8);
}
// This should also be in little endian format, see above.
static const u1_t PROGMEM DEVEUI[8] = { 0xF5, 0xCE, 0x02, 0x84, 0x3A, 0xD4, 0xA7, 0x00 };
void os_getDevEui (u1_t* buf) {
  memcpy_P(buf, DEVEUI, 8);
}
// This key should be in big endian format (or, since it is not really a
// number but a block of memory, endianness does not really apply). In
// practice, a key taken from ttnctl can be copied as-is.
// 
static const u1_t PROGMEM APPKEY[16] = { 0x54, 0x88, 0xD0, 0x82, 0xE7, 0xBC, 0x8D, 0xC8, 0x28, 0x60, 0x2E, 0x3D, 0x75, 0xE4, 0xCF, 0xE1 };
void os_getDevKey (u1_t* buf) {
  memcpy_P(buf, APPKEY, 16);
}

void do_send(osjob_t* j){

    float t = sht31.readTemperature();
    float h = sht31.readHumidity();
    
    // Encode using CayenneLPP
    lpp.reset();
    lpp.addTemperature(1, t);
    lpp.addRelativeHumidity(2, h);

    // Check if there is not a current TX/RX job running
    if (LMIC.opmode & OP_TXRXPEND) {
        Serial.println(F("OP_TXRXPEND, not sending"));
    } else {
        
        // Prepare upstream data transmission at the next possible time.
        LMIC_setTxData2(1, lpp.getBuffer(), lpp.getSize(), 0);
        Serial.println(F("Sending uplink packet..."));
        digitalWrite(LEDPIN, HIGH);
        display.clearDisplay();
        display.println("Sending uplink packet...");
        display.println( String (++counter));
        display.display ();
    }
    // Next TX is scheduled after TX_COMPLETE event.
}

void onEvent (ev_t ev) {
    Serial.print(os_getTime());
    Serial.print(": ");
    switch(ev) {
        case EV_TXCOMPLETE:
            Serial.println(F("EV_TXCOMPLETE (includes waiting for RX windows)"));
            display.clearDisplay();
            display.println("EV_TXCOMPLETE event!");

            if (LMIC.txrxFlags & TXRX_ACK) {
              Serial.println(F("Received ack"));
              display.println("Received ACK.");
            }

            if (LMIC.dataLen) {
              int i = 0;
              // data received in rx slot after tx
              Serial.print(F("Data Received: "));
              Serial.write(LMIC.frame+LMIC.dataBeg, LMIC.dataLen);
              Serial.println();
              Serial.println(LMIC.rssi);

              display.println ("Received DATA.");
              for ( i = 0 ; i < LMIC.dataLen ; i++ )
                TTN_response[i] = LMIC.frame[LMIC.dataBeg+i];
              TTN_response[i] = 0;
              display.println (String(TTN_response));
              display.println (String(LMIC.rssi));
              display.println (String(LMIC.snr));
            }

            // Schedule next transmission
            os_setTimedCallback(&sendjob, os_getTime()+sec2osticks(TX_INTERVAL), do_send);
            digitalWrite(LEDPIN, LOW);
            display.println (String (counter));
            display.display ();
            // Schedule next transmission
            os_setTimedCallback(&sendjob, os_getTime()+sec2osticks(TX_INTERVAL), do_send);
            break;
        case EV_JOINING:
            Serial.println(F("EV_JOINING: -> Joining..."));
            display.println("OTAA joining....");
            display.display();
            break;
        case EV_JOINED: {
              Serial.println(F("EV_JOINED"));
              display.clearDisplay();
              display.println("Joined!");
              display.display();
              // Disable link check validation (automatically enabled
              // during join, but not supported by TTN at this time).
              LMIC_setLinkCheckMode(0);
            }
            break;
        case EV_RXCOMPLETE:
            // data received in ping slot
            Serial.println(F("EV_RXCOMPLETE"));
            break;
        case EV_LINK_DEAD:
            Serial.println(F("EV_LINK_DEAD"));
            break;
        case EV_LINK_ALIVE:
            Serial.println(F("EV_LINK_ALIVE"));
            break;
         default:
            Serial.print(F("Unknown event: "));
            Serial.println(ev);
            break;
    }

}

void setup() {
    // put your setup code here, to run once:
    Serial.begin(115200);
    Wire.begin(4, 15);
    //reset OLED display via software
    pinMode(OLED_RST, OUTPUT);
    digitalWrite(OLED_RST, LOW);
    delay(20);
    digitalWrite(OLED_RST, HIGH);

    Wire.begin(OLED_SDA, OLED_SCL);
    if(!display.begin(SSD1306_SWITCHCAPVCC, 0x3c, false, false)) { // Address 0x3C for 128x32
        Serial.println(F("SSD1306 allocation failed"));
        for(;;); // Don't proceed, loop forever
    }
  
    display.clearDisplay();
    display.setTextColor(WHITE);
    display.setTextSize(1);
    display.setCursor(0,0);
    display.println("hello world!");
    display.display();  

    Serial.println("SHT31 test");
    if (! sht31.begin(0x44)) {   // Set to 0x45 for alternate i2c addr
      Serial.println("Couldn't find SHT31");
    while (1) delay(1);
    }
    
    // LMIC init
    os_init();
    // Reset the MAC state. Session and pending data transfers will be discarded.
    LMIC_reset();

    LMIC_setLinkCheckMode(0);
 
    // Start job (sending automatically starts OTAA too)
    do_send(&sendjob);
}

void loop() {
    // put your main code here, to run repeatedly:
    os_runloop_once();
}
