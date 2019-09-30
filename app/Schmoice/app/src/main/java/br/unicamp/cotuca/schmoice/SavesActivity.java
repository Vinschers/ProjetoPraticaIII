package br.unicamp.cotuca.schmoice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class SavesActivity extends AppCompatActivity {
    Arvore arvore1, arvore2, arvore3;
    Button btnJogo1, btnJogo2, btnJogo3;
    Controle controle;
    int atual = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saves);
        getArvores();
        btnJogo1 = (Button)findViewById(R.id.btnJogo1);
        btnJogo2 = (Button)findViewById(R.id.btnJogo2);
        btnJogo3 = (Button)findViewById(R.id.btnJogo3);
        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        controle = (Controle)params.getSerializable("controle");
        controle.setEventos(new Eventos(){
            @Override
            public void onPraBaixo() {
                desselecionar();
                if (atual < 3) {
                    atual++;
                } else {
                    atual = 0;
                }
                selecionar();
            }

            @Override
            public void onPraCima() {
                desselecionar();
                if (atual > -1) {
                    atual--;
                } else {
                    atual = 2;
                }
                selecionar();
            }
        });
        if (arvore1 == null) {
            btnJogo1.setText("Novo Jogo");
        }
    }
    public void getArvores() {
        //
    }
    public void desselecionar() {
        Button btns[] = {btnJogo1, btnJogo2, btnJogo3};
        btns[atual].clearFocus();
    }
    public void selecionar() {
        Button btns[] = {btnJogo1, btnJogo2, btnJogo3};
        btns[atual].findFocus();
    }
}
