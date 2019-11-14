package br.unicamp.cotuca.schmoice;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class InicioJogoActivity extends AppCompatActivity {
    //region Comandos gerados automaticamente para controle de fullscreen
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        controle.eventos.onOK(); // TIRAR DEPOIS!!!!!
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
    private void iniciarFullscreen() {
        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.conteudoFullScreen);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
    }
    //endregion
    LinearLayout llEscurecer, llExplicacao;
    TextView tvExplicacao, tvTranquilidade, tvFelicidade, tvSanidade, tvCarisma, tvFinancas, tvForca, tvInteligencia, tvRestantes;
    Button btnFinalizar, btnTP, btnTM, btnFeP, btnFeM, btnSP, btnSM, btnCP, btnCM, btnFiP, btnFiM, btnFoP, btnFoM, btnIP, btnIM;
    ThreadRedimensionar resizeLL;
    Controle controle;
    ConstraintLayout clAtributos;

    private void atualizarPositivos(boolean ativar)
    {
        btnTP.setEnabled(ativar);
        btnFeP.setEnabled(ativar);
        btnSP.setEnabled(ativar);
        btnCP.setEnabled(ativar);
        btnFiP.setEnabled(ativar);
        btnFoP.setEnabled(ativar);
        btnIP.setEnabled(ativar);

        btnFinalizar.setEnabled(!ativar);
    }

    int slotEnviado = 0;
    final double[] stats = {0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5 };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_jogo);
        iniciarFullscreen();
        llEscurecer  = (LinearLayout)findViewById(R.id.llEscurecer);
        llExplicacao = (LinearLayout)findViewById(R.id.llExplicacao);
        tvExplicacao = (TextView)findViewById(R.id.tvExplicacao);
        tvExplicacao.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        escurecerFundo();
        llExplicacao.setBackgroundColor(Color.argb(255, 255,255,255));
        llExplicacao.setVisibility(View.VISIBLE);
        tvExplicacao = findViewById(R.id.tvExplicacao);
        tvRestantes = findViewById(R.id.tvRestantes);
        final String textoPadrao = " pontos restantes";

        final int pontosDisponiveis[] = {3};

        tvCarisma = findViewById(R.id.tvCarisma);
        btnCP = findViewById(R.id.btnCP);
        btnCM = findViewById(R.id.btnCM);
        btnCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stats[0] += 0.1;
                tvCarisma.setText(Math.round(-5 + 10 * stats[0]) + "");

                tvRestantes.setText(--(pontosDisponiveis[0]) + textoPadrao);
                if(pontosDisponiveis[0] == 0)
                    atualizarPositivos(false);
                btnCM.setEnabled(true);
            }
        });
        btnCM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stats[0] -= 0.1;
                tvCarisma.setText(Math.round(-5 + 10 * stats[0]) + "");

                tvRestantes.setText(++(pontosDisponiveis[0]) + textoPadrao);
                if(stats[0] == 0.5)
                    btnCM.setEnabled(false);

                atualizarPositivos(true);
            }
        });

        tvFelicidade = findViewById(R.id.tvFelicidade);
        btnFeP = findViewById(R.id.btnFeP);
        btnFeM = findViewById(R.id.btnFeM);
        btnFeP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stats[1] += 0.1;
                tvFelicidade.setText(Math.round(-5 + 10 * stats[1]) + "");

                tvRestantes.setText(--(pontosDisponiveis[0]) + textoPadrao);
                if(pontosDisponiveis[0] == 0)
                    atualizarPositivos(false);
                btnFeM.setEnabled(true);
            }
        });
        btnFeM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stats[1] -= 0.1;
                tvFelicidade.setText(Math.round(-5 + 10 * stats[1]) + "");

                tvRestantes.setText(++(pontosDisponiveis[0]) + textoPadrao);
                if(stats[1] == 0.5)
                    btnFeM.setEnabled(false);

                atualizarPositivos(true);
            }
        });

        tvFinancas = findViewById(R.id.tvFinancas);
        btnFiM = findViewById(R.id.btnFiM);
        btnFiP = findViewById(R.id.btnFiP);
        btnFiP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stats[2] += 0.1;
                tvFinancas.setText(Math.round(-5 + 10 * stats[2]) + "");

                tvRestantes.setText(--(pontosDisponiveis[0]) + textoPadrao);
                if(pontosDisponiveis[0] == 0)
                    atualizarPositivos(false);
                btnFiM.setEnabled(true);
            }
        });
        btnFiM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stats[2] -= 0.1;
                tvFinancas.setText(Math.round(-5 + 10 * stats[2]) + "");

                tvRestantes.setText(++(pontosDisponiveis[0]) + textoPadrao);
                if(stats[2] == 0.5)
                    btnFiM.setEnabled(false);

                atualizarPositivos(true);
            }
        });

        tvForca = findViewById(R.id.tvForca);
        btnFoM = findViewById(R.id.btnFoM);
        btnFoP = findViewById(R.id.btnFoP);
        btnFoP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stats[3] += 0.1;
                tvForca.setText(Math.round(-5 + 10 * stats[3]) + "");

                tvRestantes.setText(--(pontosDisponiveis[0]) + textoPadrao);
                if(pontosDisponiveis[0] == 0)
                    atualizarPositivos(false);

                btnFoM.setEnabled(true);
            }
        });
        btnFoM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stats[3] -= 0.1;
                tvForca.setText(Math.round(-5 + 10 * stats[3]) + "");

                tvRestantes.setText(++(pontosDisponiveis[0]) + textoPadrao);
                if(stats[3] == 0.5)
                    btnFoM.setEnabled(false);

                atualizarPositivos(true);
            }
        });

        tvInteligencia = findViewById(R.id.tvInteligencia);
        btnIM = findViewById(R.id.btnIM);
        btnIP = findViewById(R.id.btnIP);
        btnIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stats[4] += 0.1;
                tvInteligencia.setText(Math.round(-5 + 10 * stats[4]) + "");

                tvRestantes.setText(--(pontosDisponiveis[0]) + textoPadrao);
                if(pontosDisponiveis[0] == 0)
                    atualizarPositivos(false);
                btnIM.setEnabled(true);
            }
        });
        btnIM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stats[4] -= 0.1;
                tvInteligencia.setText(Math.round(-5 + 10 * stats[4]) + "");

                tvRestantes.setText(++(pontosDisponiveis[0]) + textoPadrao);
                if(stats[4] == 0.5)
                    btnIM.setEnabled(false);

                atualizarPositivos(true);
            }
        });

        tvSanidade = findViewById(R.id.tvSanidade);
        btnSM = findViewById(R.id.btnSM);
        btnSP = findViewById(R.id.btnSP);
        btnSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stats[5] += 0.1;
                tvSanidade.setText(Math.round(-5 + 10 * stats[5])  + "");

                tvRestantes.setText(--(pontosDisponiveis[0]) + textoPadrao);
                if(pontosDisponiveis[0] == 0)
                    atualizarPositivos(false);
                btnSM.setEnabled(true);
            }
        });
        btnSM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stats[5] -= 0.1;
                tvSanidade.setText(Math.round(-5 + 10 * stats[5]) + "");

                tvRestantes.setText(++(pontosDisponiveis[0]) + textoPadrao);
                if(stats[5] == 0.5)
                    btnSM.setEnabled(false);

                atualizarPositivos(true);
            }
        });

        tvTranquilidade = findViewById(R.id.tvTranquilidade);
        btnTM = findViewById(R.id.btnTM);
        btnTP = findViewById(R.id.btnTP);
        btnTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stats[6] += 0.1;
                tvTranquilidade.setText(Math.round(-5 + 10 * stats[6]) + "");

                tvRestantes.setText(--(pontosDisponiveis[0]) + textoPadrao);
                if(pontosDisponiveis[0] == 0)
                    atualizarPositivos(false);
                btnTM.setEnabled(true);
            }
        });
        btnTM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stats[6] -= 0.1;
                tvTranquilidade.setText(Math.round(-5 + 10 * stats[6]) + "");

                tvRestantes.setText(++(pontosDisponiveis[0]) + textoPadrao);
                if(stats[6] == 0.5)
                    btnTM.setEnabled(false);

                atualizarPositivos(true);
            }
        });

        btnFinalizar = findViewById(R.id.btnFinalizar);

        clAtributos = findViewById(R.id.clAtributos);


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        String s = "Bem vindo! Você agora viverá a conturbada vida de um aluno de um colégio técnico. Me diga um pouco sobre você! (Distribua 3 pontos de atributo)";
                        escreverAnimado(tvExplicacao, s);
                    }
                }, 1000);
        controle = (Controle) getIntent().getExtras().getSerializable("controle");
        slotEnviado = getIntent().getExtras().getInt("slot", 0);

        resizeLL = new ThreadRedimensionar();
        resizeLL.start();

        configurarBotoes();
    }

    int atual = 1;
    public void configurarBotoes()
    {
        final Button[] botoes = {btnTM, btnTP, btnFeM, btnFeP, btnSM, btnSP, btnCM, btnCP, btnFoM, btnFoP, btnFiM, btnFiP, btnIM, btnIP, btnFinalizar};

        controle.setEventos(new Eventos(){
            @Override
            public void onPraBaixo() {
                desselecionar(botoes[atual], false);

                boolean jaSelecionou = false;
                int indiceSelecionadoA = atual, indiceSelecionadoB = atual;

                for (int i = atual; i < botoes.length; i += 2)
                    if (botoes[i].isEnabled())
                    {
                        jaSelecionou = true;
                        indiceSelecionadoA = i;
                        break;
                    }
                for (int i = (atual % 2 == 0 ? 0: 1); i < atual && !jaSelecionou; i+=2)
                    if (botoes[i].isEnabled()) {
                        indiceSelecionadoA = i;
                        break;
                    }

                jaSelecionou = false;

                for (int i = atual + 1; i < botoes.length; i += 2)
                    if (botoes[i].isEnabled() && (indiceSelecionadoA < atual || i - atual < indiceSelecionadoA - atual))
                    {
                        jaSelecionou = true;
                        indiceSelecionadoB = i;
                        break;
                    }
                for (int i = (atual % 2 == 0 ? 1: 0); i < botoes.length && !jaSelecionou && indiceSelecionadoA < atual; i += 2)
                    if (botoes[i].isEnabled() && atual - i < atual - indiceSelecionadoA)
                    {
                        jaSelecionou = true;
                        indiceSelecionadoB = i;
                        break;
                    }
                if (jaSelecionou)
                    atual = indiceSelecionadoB < atual && btnFinalizar.isEnabled()? botoes.length - 1 : indiceSelecionadoB;
                else
                    atual = indiceSelecionadoA < atual && btnFinalizar.isEnabled()? botoes.length - 1 : indiceSelecionadoA;

                selecionar(botoes[atual],false);
            }

            @Override
            public void onPraCima() {
                desselecionar(botoes[atual], false);

                boolean jaSelecionou = false;
                int indiceSelecionadoA = atual, indiceSelecionadoB = atual;

                for (int i = atual; i >= 0; i -= 2)
                    if (botoes[i].isEnabled())
                    {
                        jaSelecionou = true;
                        indiceSelecionadoA = i;
                        break;
                    }
                for (int i = (atual % 2 == 0 ? botoes.length - 2: botoes.length - 1); i > atual && !jaSelecionou; i -= 2)
                    if (botoes[i].isEnabled()) {
                        indiceSelecionadoA = i;
                        break;
                    }

                jaSelecionou = false;

                for (int i = atual - 1; i >= 0; i -= 2)
                    if (botoes[i].isEnabled() && (indiceSelecionadoA > atual || i - atual > indiceSelecionadoA - atual))
                    {
                        jaSelecionou = true;
                        indiceSelecionadoB = i;
                        break;
                    }
                for (int i = (atual % 2 == 0 ? botoes.length - 1: botoes.length - 2); i >= 0 && !jaSelecionou && indiceSelecionadoA > atual; i -= 2)
                    if (botoes[i].isEnabled() && atual - i > atual - indiceSelecionadoA)
                    {
                        jaSelecionou = true;
                        indiceSelecionadoB = i;
                        break;
                    }
                if (jaSelecionou)
                    atual = indiceSelecionadoB > atual && btnFinalizar.isEnabled()? botoes.length - 1 : indiceSelecionadoB;
                else
                    atual = indiceSelecionadoA > atual && btnFinalizar.isEnabled()? botoes.length - 1 : indiceSelecionadoA;

                selecionar(botoes[atual],false);
            }

            @Override
            public void onPraEsquerda() {
                if (atual != botoes.length - 1)
                {
                    if (atual % 2 == 1 && botoes[atual - 1].isEnabled())
                    {
                        desselecionar(botoes[atual], false);
                        selecionar(botoes[--atual], false);
                    }
                    else if (atual % 2 == 0 && botoes[atual + 1].isEnabled())
                    {
                        desselecionar(botoes[atual], false);
                        selecionar(botoes[++atual], false);
                    }
                }
            }

            @Override
            public void onPraDireita() {
                controle.eventos.onPraEsquerda();
            }

            @Override
            public void onOK() {
                if (resizeLL.isAlive())
                {
                    llExplicacao.setVisibility(View.INVISIBLE);
                    clarearFundo();
                    resizeLL.matar();
                    clAtributos.setVisibility(View.VISIBLE);
                }
                else if (botoes[atual].isEnabled())
                    botoes[atual].performClick();
            }
        });

        for (int i = 0; i < botoes.length; i++) {
            final int index = i;
            botoes[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    desselecionar(botoes[atual],true);
                    atual = index;
                    selecionar(botoes[atual],true);
                }
            });
        }

        backgroundDefault = botoes[atual].getBackground();

        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Jogo jogo = new Jogo();
                Player jogador = jogo.getPlayer();
                jogador.setCarisma(stats[0]);
                jogador.setFelicidade(stats[1]);
                jogador.setFinancas(stats[2]);
                jogador.setForca(stats[3]);
                jogador.setInteligencia(stats[4]);
                jogador.setSanidade(stats[5]);
                jogador.setTranquilidade(stats[6]);

                ObjetoEnvio obj = new ObjetoEnvio(jogador);

                Registrador reg = new Registrador(obj);
                reg.start();
                while (!reg.isMorta()) {}

                Intent intent = new Intent(InicioJogoActivity.this, SavesActivity.class);
                controle.setEventos(null);
                Bundle params = new Bundle();
                params.putSerializable("controle", controle);
                intent.putExtras(params);
                startActivity(intent);
            }
        });

        selecionar(botoes[atual], false);
    }

    Drawable backgroundDefault;
    public void desselecionar(Button botao, boolean fromFocusChange) {
        if (!fromFocusChange)
            botao.clearFocus();
        botao.setBackground(backgroundDefault);
    }
    public void selecionar(Button botao, boolean fromFocusChange) {
        if (!fromFocusChange)
            botao.findFocus();
        ShapeDrawable shapedrawable = new ShapeDrawable();
        shapedrawable.setShape(new RectShape());
        shapedrawable.getPaint().setColor(Color.RED);
        shapedrawable.getPaint().setStrokeWidth(10f);
        shapedrawable.getPaint().setStyle(Paint.Style.STROKE);
        botao.setBackground(shapedrawable);
    }

    private void escurecerFundo() {
        int colorFrom = Color.argb(0, 0, 0,0);
        int colorTo = Color.argb(220, 0, 0,0);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(750); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                llEscurecer.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }
    private void clarearFundo() {
        int colorFrom = Color.argb(220, 0, 0,0);
        int colorTo = Color.argb(0, 0, 0,0);
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(750); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                llEscurecer.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }
    public void escreverAnimado(final TextView tv, final String s)
    {
        final int[] i = new int[1];
        i[0] = 0;
        final int length = s.length();
        final Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                char c= s.charAt(i[0]);
                Log.d("Strange",""+c);
                tv.append(String.valueOf(c));
                i[0]++;
            }
        };

        final Timer timer = new Timer();
        TimerTask taskEverySplitSecond = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
                if (i[0] == length - 1) {
                    timer.cancel();
                }
            }
        };
        timer.schedule(taskEverySplitSecond, 1, 55);
    }
    public class ThreadRedimensionar extends Thread
    {
        boolean atual = false, funcionando = true;
        final int HEIGHT_SMALL = 180;
        final int HEIGHT_BIG = HEIGHT_SMALL + 20;
        final int WIDTH_SMALL = 325;
        final int WIDTH_BIG = WIDTH_SMALL + 20;
        final int MARGIN_SMALL = 100;
        final int MARGIN_BIG = 87;

        public void matar()
        {
            funcionando = false;
        }

        @Override
        public void run() {
            while(funcionando) {
                try {
                    if (atual) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)llExplicacao.getLayoutParams();
                                params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HEIGHT_SMALL, getResources().getDisplayMetrics());
                                params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, WIDTH_SMALL, getResources().getDisplayMetrics());
                                params.topMargin = MARGIN_SMALL;
                                llExplicacao.setLayoutParams(params);
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)llExplicacao.getLayoutParams();
                                params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HEIGHT_BIG, getResources().getDisplayMetrics());
                                params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, WIDTH_BIG, getResources().getDisplayMetrics());
                                params.topMargin = MARGIN_BIG;
                                llExplicacao.setLayoutParams(params);
                            }
                        });
                    }
                    atual = !atual;
                    Thread.sleep(500);
                } catch (Exception e) {
                    new AlertDialog.Builder(InicioJogoActivity.this)
                            .setTitle("Erro")
                            .setMessage(e.getMessage())
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
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
    public class ObjetoEnvio
    {
        public double getCarisma() {
            return carisma;
        }

        public void setCarisma(double carisma) {
            this.carisma = carisma;
        }

        public double getInteligencia() {
            return inteligencia;
        }

        public void setInteligencia(double inteligencia) {
            this.inteligencia = inteligencia;
        }

        public double getTranquilidade() {
            return tranquilidade;
        }

        public void setTranquilidade(double tranquilidade) {
            this.tranquilidade = tranquilidade;
        }

        public double getForca() {
            return forca;
        }

        public void setForca(double forca) {
            this.forca = forca;
        }

        public double getFinancas() {
            return financas;
        }

        public void setFinancas(double financas) {
            this.financas = financas;
        }

        public double getSanidade() {
            return sanidade;
        }

        public void setSanidade(double sanidade) {
            this.sanidade = sanidade;
        }

        public double getFelicidade() {
            return felicidade;
        }

        public void setFelicidade(double felicidade) {
            this.felicidade = felicidade;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getSlot() {
            return slot;
        }

        public void setSlot(int slot) {
            this.slot = slot;
        }

        double carisma, inteligencia, tranquilidade, forca, financas, sanidade, felicidade;
        String ip;
        int slot;

        public ObjetoEnvio(Player jogador)
        {
            carisma = jogador.getCarisma();
            inteligencia = jogador.getInteligencia();
            tranquilidade = jogador.getTranquilidade();
            forca = jogador.getForca();
            financas = jogador.getFinancas();
            sanidade = jogador.getSanidade();
            felicidade = jogador.getFelicidade();
            slot = slotEnviado;
            ip = getIPAddress(true);
        }
    }
    public class Registrador extends Thread
    {
        ObjetoEnvio obj;

        boolean morta = false;
        public boolean isMorta()
        {
            return morta;
        }

        public Registrador(ObjetoEnvio obj)
        {
            this.obj = obj;
        }

        @Override
        public void run() {
            try {
                ClienteWS.postObjeto(obj, Object.class, ClienteWS.webService + "/criarJogo");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            morta = true;
        }
    }
}
