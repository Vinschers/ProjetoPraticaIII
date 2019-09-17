#define del 150
#define btnOk 2
#define btnPot 3
#define btnCima 4
#define btnMenu 5
#define btnBaixo 6
#define btnDireita 7
#define btnEsquerda 8

String atual, anterior;
#include "WiFiEsp.h"
#include "SoftwareSerial.h"
SoftwareSerial serial(8, 9);
char ssid[] = "Scherer";
char pass[] = "12345678";
int status = WL_IDLE_STATUS;

WiFiEspServer server(80);
RingBuffer buf(8);


void setup() {
  Serial.begin(115200);  // porta de debug
  serial.begin(9600);
  while (!Serial) {
    ;
  }
  WiFi.init(&serial);
  // check for the presence of the shield
  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println("WiFi shield not present");
    while (true); // don't continue
  }

  Serial.print("Attempting to start AP ");
  Serial.println(ssid);

  // uncomment these two lines if you want to set the IP address of the AP
  //IPAddress localIp(192, 168, 111, 111);
  //WiFi.configAP(localIp);

  // start access point
  status = WiFi.beginAP(ssid, 10, pass, ENC_TYPE_WPA2_PSK);

  Serial.println("Access point started");
  //printWifiStatus();

  // start the web server on port 80
  server.begin();
  Serial.println("Server started");
  pinMode(btnOk, INPUT);
  pinMode(btnPot, INPUT);
  pinMode(btnCima, INPUT);
  pinMode(btnMenu, INPUT);
  pinMode(btnBaixo, INPUT);
  pinMode(btnDireita, INPUT);
  pinMode(btnEsquerda, INPUT);
}

void loop() {
  WiFiEspClient client = server.available();
  if (client) {
    Serial.println("Novo cliente");
    buf.init();
    while (client.connected()) {
      if (client.available()) {
        char c = client.read();
        buf.push(c);
        Serial.write(c);
        if (buf.endsWith("\r\n\r\n")) {
          Serial.println("Fechando");
          buf.reset();
          break;
        }
        atual = lerBtns();
        if (!anterior || atual != anterior) {
          Serial.println(atual);
          anterior = atual;
          client.println(atual);
        }
        delay(del);
      }
    } // while
    client.stop();
    Serial.println("Desconectado");
  }
}

String lerBtns() {
  String ret = "";
  ret += digitalRead(btnOk) + " ";
  ret += digitalRead(btnPot) + " ";
  ret += digitalRead(btnCima) + " ";
  ret += digitalRead(btnMenu) + " ";
  ret += digitalRead(btnBaixo) + " ";
  ret += digitalRead(btnDireita) + " ";
  ret += digitalRead(btnEsquerda);
  return ret;
}
