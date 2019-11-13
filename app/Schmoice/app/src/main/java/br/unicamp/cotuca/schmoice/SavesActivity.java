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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        try {
            Consultor consultor = new Consultor();
            consultor.start();
            while (!consultor.isMorta()) {}
            Fase[] fases = consultor.getFases();
            Jogo[] jogosObtidos = consultor.getJogos();
            for (int i = 0; i < jogosObtidos.length; i++)
                if (jogosObtidos[i] != null) {
                    jogos[i] = jogosObtidos[i];
                    for (Fase f : fases) {
                        for (ArrayList<Nivel> arr : f.getNiveis())
                            for (Nivel n : arr) {
                                n.setParentFase(f);
                            }
                        jogos[i].getArvore().adicionar(fases[0]);
                    }
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
        Intent intent;
        if (jogos[ind].getAcabouDeComecar())
            intent = new Intent(SavesActivity.this, InicioJogoActivity.class);
        else
            intent = new Intent(SavesActivity.this, JogoActivity.class);
        Bundle params = new Bundle();
        intent.putExtras(params);
        controle.setEventos(null);

        params.putSerializable("controle", controle);
        params.putSerializable("jogo", jogos[ind]);
        params.putInt("slot", ind);
        intent.putExtras(params);
        startActivity(intent);
    }

    public class PersonagemRecebido
    {
        int id;
        int idJogo;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIdJogo() {
            return idJogo;
        }

        public void setIdJogo(int idJogo) {
            this.idJogo = idJogo;
        }

        public int getIdPersonagem() {
            return idPersonagem;
        }

        public void setIdPersonagem(int idPersonagem) {
            this.idPersonagem = idPersonagem;
        }

        public double getAmizade() {
            return amizade;
        }

        public void setAmizade(double amizade) {
            this.amizade = amizade;
        }

        int idPersonagem;
        double amizade;
    }
    public class Consultor extends Thread
    {
        Fase[] fases;
        Jogo[] jogosObtidos;

        boolean morta = false;
        public boolean isMorta()
        {
            return morta;
        }

        @Override
        public void run() {
            try {

                //FaseInfo f = (FaseInfo)ClienteWS.getObjeto(FaseInfo.class, "http://177.220.18.90:3000/get");
                //fases = f.getFases();
                String ipUsuario = getIPAddress(true); // IP do usuario
                fases = (Fase[])ClienteWS.getObjeto(Fase[].class, "http://" + ClienteWS.ipMaquina + ":3000/get");
                //jogosObtidos = new Jogo[3];
                JogoRecebido[] jogosRecebidos = (JogoRecebido[])ClienteWS.getObjeto(JogoRecebido[].class, "http://" + ClienteWS.ipMaquina + ":3000/jogos/" + ipUsuario);

                jogosObtidos = new Jogo[jogosRecebidos.length];
                for (int i = 0; i < jogosRecebidos.length; i++)
                {
                    PersonagemRecebido[] amigosRecebidos = (PersonagemRecebido[])ClienteWS.getObjeto(PersonagemRecebido[].class, "http://" + ClienteWS.ipMaquina + ":3000/personagensJogo/" + jogosRecebidos[i].getId());
                    jogosObtidos[i] = new Jogo();
                    jogosObtidos[i].setAcabouDeComecar(false);

                    Player jogador = jogosObtidos[i].getPlayer();
                    jogador.setTranquilidade(jogosRecebidos[i].getTranquilidade());
                    jogador.setSanidade(jogosRecebidos[i].getSanidade());
                    jogador.setInteligencia(jogosRecebidos[i].getInteligencia());
                    jogador.setForca(jogosRecebidos[i].getForca());
                    jogador.setFinancas(jogosRecebidos[i].getFinancas());
                    jogador.setFelicidade(jogosRecebidos[i].getFelicidade());
                    jogador.setCarisma(jogosRecebidos[i].getCarisma());

                    Personagem[] personagens = jogosObtidos[i].getPersonagens();

                    for (int j = 0; j < amigosRecebidos.length; j++)
                        personagens[j].setAmizade(amigosRecebidos[j].getAmizade());

                    Arvore arvore = jogosObtidos[i].getArvore();
                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }
            morta = true;
        }
        public Fase[] getFases()
        {
            return  fases;
        }
        public  Jogo[] getJogos() {return  jogosObtidos; }
    }
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";
    }
}
