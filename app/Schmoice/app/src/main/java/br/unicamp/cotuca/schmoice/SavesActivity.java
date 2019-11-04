package br.unicamp.cotuca.schmoice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

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
        jogos = new Jogo[maxJogos];
        try {
            jogos[1] = new Jogo();
            Consultor consultor = new Consultor();
            consultor.start();
            while (!consultor.isMorta()) {}
            Fase[] fases = consultor.getFases();
            for (Fase f : fases) {
                for (ArrayList<Nivel> arr : f.getNiveis())
                    for(Nivel n : arr) {
                        n.setParentFase(f);
                    }
                jogos[1].getArvore().adicionar(fases[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jogos;
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
        Intent intent = new Intent(SavesActivity.this, JogoActivity.class);
        Bundle params = new Bundle();
        intent.putExtras(params);
        controle.setEventos(null);

        params.putSerializable("controle", controle);
        params.putSerializable("jogo", jogos[ind]);
        intent.putExtras(params);
        startActivity(intent);
    }
    public class Consultor extends Thread
    {
        Fase[] fases;
        boolean morta = false;
        public boolean isMorta()
        {
            return morta;
        }

        @Override
        public void run() {
            try {
                                                                                                //trocar por ipv4 do pc
                //FaseInfo f = (FaseInfo)ClienteWS.getObjeto(FaseInfo.class, "http://177.220.18.97:3000/get");
                //fases = f.getFases();
                fases = (Fase[])ClienteWS.getObjeto(Fase[].class, "http://177.220.18.97:3000/get");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            morta = true;
        }
        public Fase[] getFases()
        {
            return  fases;
        }
    }

    public class MyTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
