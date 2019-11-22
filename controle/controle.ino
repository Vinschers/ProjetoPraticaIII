#define del 150
#define btnOk 2 
#define btnMenu 5 
#define btnCancelar 3 
#define pinoX A0
#define pinoY A1

int pushButton = 2;
String atual, anterior;
void setup() {
  Serial.begin(9600);
  Serial.println("ComunicaÃ§Ã£o iniciada!");
  pinMode(btnOk, INPUT);
  pinMode(btnMenu, INPUT);
  pinMode(btnCancelar, INPUT);

}

void loop() {
    atual = lerBtns();
    if (!anterior || atual != anterior) {
      Serial.println(atual);
      anterior = atual;
    }
    delay(del);
}

String lerBtns() {
  int x = analogRead(pinoX);
  int y = analogRead(pinoY);
  
  int esquerda = y > 800 ? 1 : 0,
      direita  = y < 200 ? 1 : 0,
      cima     = x < 200 ? 1 : 0,
      baixo    = x > 800 ? 1 : 0;
      
  String ret = digitalRead(btnOk) + String(" ") + cima + " " + digitalRead(btnMenu) + " " + baixo + " " + direita + " " + digitalRead(btnCancelar) + " " + esquerda;
  
  return ret;
}

