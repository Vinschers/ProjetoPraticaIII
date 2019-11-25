package br.unicamp.cotuca.schmoice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.security.ConfirmationPrompt;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SavesActivity extends AppCompatActivity {
    final int maxJogos = 3;

    Jogo jogos[] = new Jogo[maxJogos];
    Button[] btns = new Button[maxJogos];
    int atual = 0;
    Fase[] fases;
    SavesActivity este = this;
    LinearLayout llCarregando, llPrincipal;
    Handler handlerTerminouDeCarregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saves);
        llCarregando = findViewById(R.id.llCarregando);
        llPrincipal = findViewById(R.id.llPrincipal);
        jogos = getJogos();
        btns[0] = (Button)findViewById(R.id.btnJogo1);
        btns[1] = (Button)findViewById(R.id.btnJogo2);
        btns[2] = (Button)findViewById(R.id.btnJogo3);

        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        try {
            fases = (Fase[]) params.getSerializable("fases");
        }
        catch (Exception ex) {fases = null; }

        selecionar(false);

        Uteis.controle.setEventos(new Eventos(){
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btns[atual].performClick();
                    }
                });
            }

            @Override
            public void onCancelar() {
                if (jogos[atual] != null && !jogos[atual].getAcabouDeComecar())
                new AlertDialog.Builder(este)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Removendo save")
                        .setMessage("Tem certeza que deseja remover este save?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int id = jogos[atual].getId();
                                Deletor d = new Deletor(id);
                                d.start();
                                while (!d.isMorta());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        btns[atual].setText("Novo Jogo");
                                        jogos[atual] = new Jogo();
                                    }
                                });
                            }

                        })
                        .setNegativeButton("Não", null)
                        .show();
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
            final Consultor consultor = new Consultor();
            handlerTerminouDeCarregar = new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:
                            if (fases == null)
                                fases = consultor.getFases();
                            Jogo[] jogosObtidos = consultor.getJogos();

                            for (int i = 0; i < jogosObtidos.length; i++)
                                if (jogosObtidos[i] != null) {
                                    jogos[i] = jogosObtidos[i];
                                    for (Fase f : fases) {
                                        for (Nivel[] arr : f.getNiveis())
                                            for (Nivel n : arr) {
                                                if (n != null)
                                                    n.setParentFase(f);
                                            }
                                        jogos[i].getArvore().adicionar(f);
                                    }
                                }
                            llCarregando.setVisibility(View.GONE);
                            llPrincipal.setVisibility(View.VISIBLE);
                            break;
                        default:
                            break;
                    }
                }
            };

            consultor.execute(fases == null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jogos;
    }
    public void desselecionar(final boolean fromFocusChange) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!fromFocusChange)
                    btns[atual].clearFocus();
                btns[atual].setBackground(null);
            }
        });
    }
    public void selecionar(final boolean fromFocusChange) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!fromFocusChange)
                    btns[atual].findFocus();
                ShapeDrawable shapedrawable = new ShapeDrawable();
                shapedrawable.setShape(new RectShape());
                shapedrawable.getPaint().setColor(Uteis.corSelecionado);
                shapedrawable.getPaint().setStrokeWidth(10f);
                shapedrawable.getPaint().setStyle(Paint.Style.STROKE);
                btns[atual].setBackground(shapedrawable);
            }
        });
    }

    public void carregarJogo(int ind) {
        Intent intent;
        if (jogos[ind].getAcabouDeComecar())
            intent = new Intent(SavesActivity.this, InicioJogoActivity.class);
        else
            intent = new Intent(SavesActivity.this, JogoActivity.class);
        Bundle params = new Bundle();
        intent.putExtras(params);
        Uteis.controle.setEventos(null);

        params.putSerializable("jogo", jogos[ind]);
        if (jogos[ind].getAcabouDeComecar())
            params.putSerializable("fases", fases);

        params.putInt("slot", ind);
        intent.putExtras(params);
        startActivity(intent);
    }


    public class Consultor extends AsyncTask<Boolean, Integer, Integer>
    {
        Fase[] fasesObtidas;
        Jogo[] jogosObtidos;

        @Override
        protected Integer doInBackground(Boolean... obterFases) {
            try {
                String ipUsuario = getIPAddress(true); // IP do usuario

                if (obterFases[0])
                    fasesObtidas = (Fase[])ClienteWS.getObjeto(Fase[].class, ClienteWS.webService + "/get");
                JogoRecebido[] jogosRecebidos = (JogoRecebido[])ClienteWS.getObjeto(JogoRecebido[].class, ClienteWS.webService + "/jogos/" + ipUsuario);

                jogosObtidos = new Jogo[3];

                for (int i = 0; jogosRecebidos != null && i < jogosRecebidos.length; i++)
                {
                    PersonagemRecebido[] amigosRecebidos = (PersonagemRecebido[])ClienteWS.getObjeto(PersonagemRecebido[].class, ClienteWS.webService + "/personagensJogo/" + jogosRecebidos[i].getId());
                    Jogo novoJogo = new Jogo();
                    novoJogo.setId(jogosRecebidos[i].getId());
                    novoJogo.setAcabouDeComecar(false);
                    novoJogo.setEscolhasImportantes(jogosRecebidos[i].getEscolhasImportantes());

                    Player jogador = novoJogo.getPlayer();
                    jogador.setTranquilidade(jogosRecebidos[i].getTranquilidade());
                    jogador.setSanidade(jogosRecebidos[i].getSanidade());
                    jogador.setInteligencia(jogosRecebidos[i].getInteligencia());
                    jogador.setForca(jogosRecebidos[i].getForca());
                    jogador.setFinancas(jogosRecebidos[i].getFinancas());
                    jogador.setFelicidade(jogosRecebidos[i].getFelicidade());
                    jogador.setCarisma(jogosRecebidos[i].getCarisma());

                    /*novoJogo.getArvore().setCaminho(jogosRecebidos[i].getCaminho());
                    novoJogo.getArvore().getFaseAtual().setNivelAtual(jogosRecebidos[i].getRotaAtual(), jogosRecebidos[i].getRotaAtual());*/

                    jogosObtidos[jogosRecebidos[i].getSlot()] = novoJogo;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            morta = true;

            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // The results of the above method
            // Processing the results here
            handlerTerminouDeCarregar.sendEmptyMessage(0);
        }

        boolean morta = false;
        public boolean isMorta()
        {
            return morta;
        }

        public Fase[] getFases()
        {
            return  fasesObtidas;
        }
        public  Jogo[] getJogos() {return  jogosObtidos; }
    }

    public class Deletor extends Thread
    {
        int id;
        boolean morta = false;
        public Deletor(int id)
        {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                String ipUsuario = getIPAddress(true);
                ClienteWS.getObjeto(null, ClienteWS.webService + "/delete/" + id);
            }
            catch (Exception ex) {ex.printStackTrace();}
            morta = true;
        }

        public boolean isMorta()
        {
            return morta;
        }
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
