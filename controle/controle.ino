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
  atual = lerBtns();
  if (!anterior || atual != anterior) {
    Serial.println(atual);
    anterior = atual;
  }
  delay(del);
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

