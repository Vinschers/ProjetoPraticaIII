package br.unicamp.cotuca.schmoice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SavesActivity extends AppCompatActivity {
    final int maxJogos = 3;
    Jogo jogos[] = new Jogo[maxJogos];
    Button[] btns = new Button[maxJogos];
    Controle controle;
    int atual = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saves);
        jogos = getJogos();
        btns[0] = (Button)findViewById(R.id.btnJogo1);
        btns[1] = (Button)findViewById(R.id.btnJogo2);
        btns[2] = (Button)findViewById(R.id.btnJogo3);
        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        controle = (Controle)params.getSerializable("controle");
        selecionar(false);
        controle.setEventos(new Eventos(){
            @Override
            public void onPraBaixo() {
                desselecionar(false);
                if (atual < 3) {
                    atual++;
                } else {
                    atual = 0;
                }
                selecionar(false);
            }

            @Override
            public void onPraCima() {
                desselecionar(false);
                if (atual > -1) {
                    atual--;
                } else {
                    atual = 2;
                }
                selecionar(false);
            }

            @Override
            public void onOK() {
                btns[atual].performClick();
            }
        });
        for (int i = 0; i < maxJogos; i++) {
            if (jogos[i] == null)
                btns[i].setText("Novo Jogo");
            final int index = i;
            btns[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    desselecionar(true);
                    atual = index;
                    selecionar(true);
                }
            });
            btns[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (jogos[index] != null)
                        carregarJogo(index);
                    else {
                        jogos[index] = new Jogo();
                        carregarJogo(index);
                    }
                }
            });
        }
    }
    public Jogo[] getJogos() {
        //access SQLite
        return new Jogo[maxJogos];
    }
    public void desselecionar(boolean fromFocusChange) {
        if (!fromFocusChange)
            btns[atual].clearFocus();
        btns[atual].setBackground(null);
    }
    public void selecionar(boolean fromFocusChange) {
        if (!fromFocusChange)
            btns[atual].findFocus();
        ShapeDrawable shapedrawable = new ShapeDrawable();
        shapedrawable.setShape(new RectShape());
        shapedrawable.getPaint().setColor(Color.RED);
        shapedrawable.getPaint().setStrokeWidth(10f);
        shapedrawable.getPaint().setStyle(Paint.Style.STROKE);
        btns[atual].setBackground(shapedrawable);
    }

    public void carregarJogo(int ind) {
        /*
        Intent intent = new Intent(SavesActivity.this, JogoActivity.class);
        Bundle params = new Bundle();
        intent.putExtras(params);
        startActivity(intent);*/
        Intent intent;
        Bundle params = new Bundle();
        if (ind == 0) {
            intent = new Intent(SavesActivity.this, Minigame1Activity.class);
        } else if (ind == 1) {
            intent = new Intent(SavesActivity.this, Minigame2Activity.class);
        }
        else {
            intent = new Intent(SavesActivity.this, JogoActivity.class);
        }
        params.putInt("cenario", R.drawable.oi);
        params.putInt("personagem", R.drawable.oi);
        params.putSerializable("jogo", jogos[ind]);
        params.putInt("diff", 1);
        controle.setEventos(null);

        params.putSerializable("controle", controle);
        intent.putExtras(params);
        startActivity(intent);
    }
}
