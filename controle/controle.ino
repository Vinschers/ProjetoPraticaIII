#define del 150
#define btnOk 2
#define btnPot 3
#define btnCima 4
#define btnMenu 5
#define btnBaixo 6
#define btnDireita 7
#define btnEsquerda 8

String atual, anterior;


void setup() {
  Serial.begin(9600);
  pinMode(btnOk, INPUT);
  pinMode(btnPot, INPUT);
  pinMode(btnCima, INPUT);
  pinMode(btnMenu, INPUT);
  pinMode(btnBaixo, INPUT);
  pinMode(btnDireita, INPUT);
  pinMode(btnEsquerda, INPUT);
}

void loop() {
  atual = digitalRead(btnOk) + "" + digitalRead(btnPot) + "" + digitalRead(btnCima) + "" + digitalRead(btnMenu) + "" + digitalRead(btnBaixo) + "" + digitalRead(btnDireita) + "" + digitalRead(btnEsquerda);
  if (!anterior || atual != anterior) {
    Serial.println(atual);
    anterior = atual;
  }
  delay(del);
}
