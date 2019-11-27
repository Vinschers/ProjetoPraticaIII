package br.unicamp.cotuca.schmoice;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class JogoActivity extends AppCompatActivity {
    //region Fullscreen
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
        if (mVisible) {
            hide();
        //} else {
        //    show();
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
    }
    //endregion

    ImageView imgCenario;
    RelativeLayout conteudoFullScreen;
    FrameLayout flPersonagens;
    FrameLayout llTopo;
    Button btnAvancarMinigame;
    final int btnsPorLinha = 2;
    int slotAtual;

    Jogo jogo;
    Nivel nivel;

    //region Nivel normal
    TextView tvDescricao, tvContinuarDesc;
    LinearLayout llEscolhas;
    ArrayList<Escolha> escolhas;
    int[] btnAtual = new int[btnsPorLinha];
    //endregion

    //region Minigame 1
    TextView tvTempoMinigame1;
    ProgressBar pbVidaMinigame1;
    LinearLayout llFundoMinigame1;
    TimerJogo timerMinigame1;

    class TimerJogo extends Thread {

        TextView tvTimer;
        ProgressBar pbVida;
        Runnable onGanhou, onPerdeu;
        boolean pausado = false;

        public TimerJogo(final TextView tvTimer, final ProgressBar pbVida, Runnable onGanhou, Runnable onPerdeu) {
            this.tvTimer = tvTimer;
            this.pbVida = pbVida;
            this.onGanhou = onGanhou;
            this.onPerdeu = onPerdeu;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvTimer.setText("15");
                    pbVida.setProgress(0);
                }
            });
        }

        public void run() {
            try {
                for (final int[] segundos = {15}; segundos[0] >= 0; segundos[0]--)
                {
                    if (pausado)
                        segundos[0]++;
                    else {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvTimer.setText(segundos[0] + "");
                            }
                        });

                        if (pbVida.getProgress() >= pbVida.getMax())
                            break;
                    }
                }
                if (pbVida.getProgress() >= pbVida.getMax()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvTimer.setText("Ganhou!");
                        }
                    });
                    onGanhou.run();
                }
                else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvTimer.setText("Perdeu!");
                        }
                    });
                    onPerdeu.run();
                }
            }
            catch (InterruptedException ex) {}
            catch (Throwable ex) {System.out.println(ex.getMessage());}
        }
        public void pausar()
        {
            pausado = true;
        }
        public void continuar()
        {
            pausado = false;
        }
    }

    //endregion

    //region Minigame 2
    LinearLayout llFundoMinigame2;

    CanvasMinigame canvas;
    ObjetoMinigame[] objs;
    ThreadMinigame2 threadMinigame2;
    final float click = 0.30f;

    public ObjetoMinigame getObjetoAtual() {
        for (ObjetoMinigame obj : objs) {
            if (obj.getX() + obj.getWidth() > 0)
                return obj;
        }
        return new ObjetoMinigame(-1, 0);
    }

    public void perderMinigame2() {
        canvas.setFim(true);
        threadMinigame2.interrupt();
        canvas = null;
        objs = null;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnAvancarMinigame.setVisibility(View.VISIBLE);
                Uteis.controle.setEventos(new Eventos() {
                    @Override
                    public void onOK() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnAvancarMinigame.performClick();
                            }
                        });

                    }
                });
            }
        });
        jogo.getArvore().getFaseAtual().getNivelAtual().setTerminado(false);
        jogo.getArvore().getFaseAtual().avancarNivel();
    }
    public void ganharMinigame2() {
        canvas.setFim(true);
        threadMinigame2.interrupt();
        canvas = null;
        objs = null;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnAvancarMinigame.setVisibility(View.VISIBLE);
                Uteis.controle.setEventos(new Eventos() {
                    @Override
                    public void onOK() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnAvancarMinigame.performClick();
                            }
                        });

                    }
                });
            }
        });
        nivel.setTerminado(true);
        nivel.getParentFase().avancarNivel();
    }

    public class ObjetoMinigame {
        private int tipo, cor, alpha;
        private double v;
        private float x, y;

        public int getTipo() {
            return tipo;
        }
        public void setTipo(int tipo) {
            this.tipo = tipo;
        }
        public int getCor() {
            return cor;
        }
        public void setCor(int cor) {
            this.cor = cor;
        }
        public float getX() {
            return x;
        }
        public void setX(float x) {
            this.x = x;
        }
        public float getY() {
            return y;
        }
        public void setY(float y) {
            this.y = y;
        }
        public int getWidth() {
            if (tipo == 0 || tipo == 2)
                return 150;
            return 110;
        }

        public ObjetoMinigame(int t, double vel) {
            tipo = t;
            cor = getCorAleatoria();
            v = vel;
            alpha = 255;
        }
        public void draw(Canvas canvas) {
            cor = Color.argb(alpha, Color.red(cor), Color.green(cor), Color.blue(cor));
            switch (tipo) {
                case 0:
                    draw0(canvas);
                    break;
                case 1:
                    draw1(canvas);
                    break;
                case 2:
                    draw2(canvas);
                    break;
                case 3:
                    draw3(canvas);
                    break;
            }
        }
        private void draw0(Canvas canvas) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(cor);
            canvas.drawRect(getX() + 50, getY() + 30, getX() + 150, getY() + 80, paint);
            Path path = new Path();
            Point p1 = new Point((int)getX() + 50, (int)getY());
            Point p2 = new Point((int)getX(), (int)getY() + 55);
            Point p3 = new Point((int)getX() + 50, (int)getY()+110);
            drawTriangulo(canvas, p1, p2, p3);
        }
        private void draw1(Canvas canvas) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(cor);
            canvas.drawRect(getX() + 30, getY() + 50, getX() + 80, getY() + 150, paint);
            Path path = new Path();
            Point p1 = new Point((int)getX(), (int)getY() + 50);
            Point p2 = new Point((int)getX()+55, (int)getY());
            Point p3 = new Point((int)getX()+110, (int)getY() + 50);
            drawTriangulo(canvas, p1, p2, p3);
        }
        private void draw2(Canvas canvas) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(cor);
            canvas.drawRect(getX(), getY(), getX() + 100, getY() + 50, paint);
            Path path = new Path();
            Point p1 = new Point((int)getX() + 100, (int)getY()-30);
            Point p2 = new Point((int)getX()+150, (int)getY() + 25);
            Point p3 = new Point((int)getX() + 100, (int)getY()+80);
            drawTriangulo(canvas, p1, p2, p3);
        }
        private void draw3(Canvas canvas) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(cor);
            canvas.drawRect(getX() + 30, getY(), getX() + 80, getY() + 100, paint);
            Path path = new Path();
            Point p1 = new Point((int)getX(), (int)getY()+100);
            Point p2 = new Point((int)getX()+55, (int)getY() + 150);
            Point p3 = new Point((int)getX()+110, (int)getY()+100);
            drawTriangulo(canvas, p1, p2, p3);
        }
        private void drawTriangulo(Canvas canvas, Point p1, Point p2, Point p3) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

            paint.setStrokeWidth(2);
            paint.setColor(cor);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setAntiAlias(true);

            Path path = new Path();
            path.setFillType(Path.FillType.EVEN_ODD);
            path.moveTo(p1.x, p1.y);
            path.lineTo(p2.x, p2.y);
            path.lineTo(p3.x, p3.y);
            path.lineTo(p1.x, p1.y);
            path.close();

            canvas.drawPath(path, paint);
        }
        public void atualizar() {
            if (getX() + getWidth() >= 0) {
                setX(getX() - (float)v);
            }
            if (getX() < -1 * getWidth() && alpha == 255) {
                perderMinigame2();
            }
        }
        private int getCorAleatoria() {
            Random random = new Random();
            float r = 70 + random.nextInt(185);
            float g = 70 + random.nextInt(185);
            float b = 70 + random.nextInt(185);
            return Color.rgb(r, g, b);
        }
        public void fade() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(alpha > 0) {
                        alpha--;
                        try {
                            Thread.sleep(0, 250);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    public class CanvasMinigame extends View {
        private Paint paint;
        private ObjetoMinigame[] objs;
        private boolean fim = false;

        public ObjetoMinigame[] getObjs() {
            return objs;
        }

        public void setObjs(ObjetoMinigame[] objs) {
            this.objs = objs;
        }

        public boolean isFim() {return fim;}
        public void setFim(boolean f) {fim = f;}

        public CanvasMinigame(Context context) {
            super(context);
        }

        @Override
        public void setOnClickListener(@Nullable OnClickListener l) {
            super.setOnClickListener(l);
            Uteis.controle.eventos.onOK();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (!isFim())
            {
                paint = new Paint();
                drawPartes(canvas);
                for (ObjetoMinigame obj : objs) {
                    obj.atualizar();
                    obj.draw(canvas);
                }
                if (objs[objs.length - 1].getX() + objs[objs.length - 1].getWidth() < 0)
                    ganharMinigame2();
            }
        }
        private void drawPartes(Canvas canvas) {
            int w = llFundoMinigame2.getMeasuredWidth();
            int h = llFundoMinigame2.getMeasuredHeight();
            int corPassar = Color.argb(230, 0, 0, 0);
            int corClick = Color.argb(120, 255, 255, 0);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(corPassar);
            canvas.drawPaint(paint);
            paint.setColor(corClick);
            canvas.drawRect(getX(), getY(), w * click, h, paint);
        }
    }

    public class ThreadMinigame2 extends Thread {
        CanvasMinigame canvas;
        boolean pausado = false;

        public ThreadMinigame2(CanvasMinigame canvas)
        {
            this.canvas = canvas;
        }

        @Override
        public void run() {
            while(!canvas.isFim()) {
                if (!pausado)
                    canvas.invalidate();
            }
        }

        public void pausar()
        {
            pausado = true;
        }
        public void continuar()
        {
            pausado = false;
        }
    }
    //endregion

    //region Extras

    public void iniciarVariaveis()
    {
        btnAvancarMinigame = (Button)         findViewById(R.id.btnAvancarMinigame);
        tvTempoMinigame1   = (TextView)       findViewById(R.id.tvTempoMinigame1);
        imgCenario         = (ImageView)      findViewById(R.id.imgCenario);
        llTopo             = (FrameLayout)    findViewById(R.id.llTopo);
        pbVidaMinigame1    = (ProgressBar)    findViewById(R.id.pbVidaMinigame1);
        llFundoMinigame1   = (LinearLayout)   findViewById(R.id.llFundoMinigame1);
        llFundoMinigame2   = (LinearLayout)   findViewById(R.id.llFundoMinigame2);
        flPersonagens      = (FrameLayout)    findViewById(R.id.flPersonagens);
        conteudoFullScreen = (RelativeLayout) findViewById(R.id.conteudoFullScreen);

        tvDescricao = new TextView(JogoActivity.this);
        tvDescricao.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        tvDescricao.setTypeface(Typeface.create("monospace", Typeface.BOLD));
        tvDescricao.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        tvDescricao.setGravity(Gravity.CENTER_HORIZONTAL);

        tvContinuarDesc = new TextView(JogoActivity.this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        tvContinuarDesc.setLayoutParams(lp);
        tvContinuarDesc.setTypeface(Typeface.create("monospace", Typeface.NORMAL));
        tvContinuarDesc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        tvContinuarDesc.setText("(OK) Continuar");
        tvContinuarDesc.setVisibility(View.INVISIBLE);

        llEscolhas = new LinearLayout(JogoActivity.this);
        llEscolhas.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        llEscolhas.setOrientation(LinearLayout.VERTICAL);
        llEscolhas.setGravity(Gravity.CENTER_HORIZONTAL);

        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        jogo = (Jogo)params.getSerializable("jogo");
        slotAtual = params.getInt("slot");
    }

    public void atualizarFase(final Runnable onContinuar)
    {
        Fase atual = jogo.getArvore().getFaseAtual();
        jogo.getArvore().atualizarFaseAtual();
        Fase nova = jogo.getArvore().getFaseAtual();

        if (!atual.equals(nova))
        {
            final Player pAnt = atual.getPlayerAntigo();
            final Player pNovo = jogo.getPlayer();
            final String[] attrs = {"tranquilidade", "sanidade", "inteligencia", "forca", "financas", "felicidade", "carisma"};
            final int maxPb = 10000000;

            final Dialog dialog = new Dialog(JogoActivity.this);
            dialog.setContentView(R.layout.status_dialog);

            final LinearLayout llBarras = dialog.findViewById(R.id.llBarras);
            for(int i = 0; i < llBarras.getChildCount() - 1; i++)
            {
                LinearLayout ll = (LinearLayout)llBarras.getChildAt(i);
                ((TextView)ll.getChildAt(0)).setText(String.format("%-13s", attrs[i]));
                ProgressBar pb = (ProgressBar)ll.getChildAt(1);
                pb.setMax(maxPb);
                pb.setProgress((int)(pAnt.getAttr(i) * maxPb));
            }

            final Handler handler = new Handler()
            {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    if (msg.what != 0)
                        return;

                    for(int i = 0; i < llBarras.getChildCount() - 1; i++)
                    {
                        LinearLayout ll = (LinearLayout)llBarras.getChildAt(i);
                        ProgressBar pb = (ProgressBar)ll.getChildAt(1);

                        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(pb, "progress", pb.getProgress(), (int)(pNovo.getAttr(i) * maxPb));
                        progressAnimator.setDuration(1000);
                        progressAnimator.setInterpolator(new LinearInterpolator());
                        progressAnimator.start();
                    }
                }
            };

            if (jogo.getArvore().getFaseAtual() != null)
                jogo.getArvore().getFaseAtual().setPlayerAntigo(pNovo);

            Uteis.setTimeout(new Runnable() {
                @Override
                public void run() {
                    JogoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(0);
                        }
                    });
                }
            }, 1500);

            dialogButton = (Button) dialog.findViewById(R.id.btnContinuar);

            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            hide();
                            onContinuar.run();
                        }
                    });
                }
            });
            Uteis.controle.setEventos(new Eventos()
            {
                @Override
                public void onOK() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialogButton.performClick();
                        }
                    });
                }
            });

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dialog.show();
                    Window window = dialog.getWindow();
                    window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                }
            });
        }
        else
            onContinuar.run();
    }
    Button dialogButton;

    //region botões
    public void setupBtns(final ArrayList<Escolha> escolhas)
    {
        Button btn;
        LinearLayout ll = null;

        for (int i = 0; i < escolhas.size(); i++) {
            if (i % btnsPorLinha == 0) {
                if (ll != null)
                    llEscolhas.addView(ll);
                ll = new LinearLayout(JogoActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.topMargin = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20f, getResources().getDisplayMetrics());
                ll.setLayoutParams(lp);
                ll.setGravity(Gravity.CENTER_HORIZONTAL);
                ll.setOrientation(LinearLayout.HORIZONTAL);
            }
            final int ind = i;
            btn = new Button(JogoActivity.this);
            btn.setText(escolhas.get(i).getNome());
            btn.setBackgroundColor(Color.argb(1, 255, 255, 255));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nivel.efetuarEscolha(escolhas.get(ind));
                    iniciarNivel();
                }
            });
            btn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        btnAtual[0] = ind / 2;
                        btnAtual[1] = ind % 2;
                        selecionar(false);
                    }
                }
            });

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.weight = (float)1/btnsPorLinha;
            btn.setLayoutParams(lp);
            ll.addView(btn);
        }
        if (ll != null)
            llEscolhas.addView(ll);
        ((LinearLayout)llEscolhas.getChildAt(0)).getChildAt(0).findFocus();
    }

    private void desselecionar()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < llEscolhas.getChildCount(); i -=- 1)
                {
                    LinearLayout ll = (LinearLayout)llEscolhas.getChildAt(i);
                    for (int j = 0; j < ll.getChildCount(); j -=- 1)
                    {
                        ll.getChildAt(j).setBackground(null);
                    }
                }
            }
        });

    }

    public void selecionar(final boolean focus)
    {
        desselecionar();
        int y = btnAtual[0], x = btnAtual[1];
        final Button btn = (Button)((LinearLayout)llEscolhas.getChildAt(y)).getChildAt(x);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (focus) {
                    btn.findFocus();
                }
                ShapeDrawable shapedrawable = new ShapeDrawable();
                shapedrawable.setShape(new RectShape());
                shapedrawable.getPaint().setColor(Uteis.corSelecionado);
                shapedrawable.getPaint().setStrokeWidth(10f);
                shapedrawable.getPaint().setStyle(Paint.Style.STROKE);
                btn.setBackground(shapedrawable);
            }
        });

    }
    //endregion

    //endregion

    //region Inicio de niveis
    public void colocarPersonagens() {
        for(int i = 0; i < nivel.getPersonagens().size(); i++) {
            try{
                Personagem personagem = nivel.getPersonagens().get(i);
                ImageView iv = new ImageView(JogoActivity.this);

                personagem.setX((personagem.getX() / 632) * conteudoFullScreen.getWidth());
                personagem.setY((personagem.getY() / 1126) * conteudoFullScreen.getHeight());

                Bitmap bmp = personagem.getBmp().copy(Bitmap.Config.ARGB_8888,true);
                iv.setImageBitmap(bmp);
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                lp.width = (int)personagem.getWidth();
                lp.gravity = Gravity.TOP;
                lp.setMargins((int)personagem.getX(), (int)personagem.getY(), 0, 0);

                iv.setPivotX(iv.getWidth()/2);
                iv.setPivotY(iv.getHeight()/2);
                iv.setRotation(personagem.getRotation());
                iv.setLayoutParams(lp);
                iv.requestLayout();
                flPersonagens.addView(iv);
                iv.bringToFront();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void comecarNivelAtual() {
        switch (nivel.getTipo())
        {
            case 0:
                Uteis.escurecerFundo(imgCenario, 0.6f);
                llTopo.addView(llEscolhas);
                iniciarNivelNormal();
                break;

            case 1:
                llTopo.addView(llFundoMinigame1);
                iniciarMinigame1();
                break;

            case 2:
                Uteis.escurecerFundo(imgCenario, 0.35f);
                llTopo.addView(llFundoMinigame2);
                iniciarMinigame2();
                break;

            case -1:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        exit();
                    }
                });
                break;
        }
    }

    public void iniciarNivel()
    {
        flPersonagens.removeAllViews();
        llTopo.removeAllViews();
        llTopo.addView(tvDescricao);
        llTopo.addView(tvContinuarDesc);
        Uteis.clarearFundo(imgCenario);

        atualizarFase(new Runnable() {
            @Override
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nivel = jogo.getArvore().getFaseAtual().getNivelAtual();
                            imgCenario.setImageBitmap(Uteis.getImageByName(nivel.getBackground()));
                            escolhas = nivel.getEscolhas();

                            if (nivel.isTerminado()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        exit();
                                    }
                                });
                                return;
                            }

                            Uteis.escreverAnimado(tvDescricao, tvContinuarDesc, nivel.getDescricao(), JogoActivity.this, new Runnable() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            llTopo.removeAllViews();
                                            comecarNivelAtual();
                                        }
                                    });
                                }
                            }, new Runnable() {
                                @Override
                                public void run() {
                                    abrirMenu();
                                }
                            });

                            colocarPersonagens();
                        }
                    });
                } catch (Exception ex) { runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        exit();
                    }
                }); }
            }
        });
    }

    public void iniciarNivelNormal()
    {
        llEscolhas.removeAllViews();

        setupBtns(escolhas);

        Uteis.controle.setEventos(new Eventos(){
            @Override
            public void onPraCima() {
                btnAtual[0]--;
                if (btnAtual[0] < 0)
                    btnAtual[0] = nivel.getEscolhas().size() / 2 - 1;
                selecionar(true);
            }

            @Override
            public void onPraBaixo() {
                btnAtual[0]++;
                if (btnAtual[0] > nivel.getEscolhas().size() / 2 - 1)
                    btnAtual[0] = 0;
                selecionar(true);
            }

            @Override
            public void onPraEsquerda() {
                btnAtual[1]--;
                if (btnAtual[1] < 0)
                    btnAtual[1] = ((LinearLayout)llEscolhas.getChildAt(btnAtual[0])).getChildCount() - 1;
                selecionar(true);
            }

            @Override
            public void onPraDireita() {
                btnAtual[1]++;
                if (btnAtual[1] > ((LinearLayout)llEscolhas.getChildAt(btnAtual[0])).getChildCount() - 1)
                    btnAtual[1] = 0;
                selecionar(true);
            }

            @Override
            public void onMenu() {
                abrirMenu();
            }
            @Override
            public void onOK() {
                int y = btnAtual[0], x = btnAtual[1];
                final Button btn = (Button)((LinearLayout)llEscolhas.getChildAt(y)).getChildAt(x);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btn.performClick();
                    }
                });
            }
        });
        btnAtual[0] = btnAtual[1] = 0;
        selecionar(true);
    }
    public void iniciarMinigame1()
    {
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uteis.controle.eventos.onOK();
            }
        });
        Uteis.controle.setEventos(new Eventos(){
            @Override
            public void onOK() {
                if (!tvTempoMinigame1.getText().equals("Ganhou!") && !tvTempoMinigame1.getText().equals("Perdeu!") && pbVidaMinigame1.getProgress() < pbVidaMinigame1.getMax())
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pbVidaMinigame1.incrementProgressBy(1);
                        }
                    });
            }

            @Override
            public void onMenu() {
                abrirMenu();
            }
        });
        timerMinigame1 = new TimerJogo(tvTempoMinigame1, pbVidaMinigame1, new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        nivel.setTerminado(true);
                        nivel.getParentFase().avancarNivel();
                        btnAvancarMinigame.setVisibility(View.VISIBLE);
                        Uteis.controle.setEventos(new Eventos() {
                            @Override
                            public void onOK() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        btnAvancarMinigame.performClick();
                                    }
                                });

                            }
                        });
                    }
                });
            }
        }, new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnAvancarMinigame.setVisibility(View.VISIBLE);
                        Uteis.controle.setEventos(new Eventos() {
                            @Override
                            public void onOK() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        btnAvancarMinigame.performClick();
                                    }
                                });
                            }
                        });
                    }
                });
                nivel.setTerminado(false);
                nivel.getParentFase().avancarNivel();
            }
        });
        timerMinigame1.start();
    }
    public void iniciarMinigame2()
    {
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uteis.controle.eventos.onOK();
            }
        });

        canvas = new CanvasMinigame(JogoActivity.this);

        int width = llFundoMinigame2.getMeasuredWidth();
        int height = llFundoMinigame2.getMeasuredHeight();
        double diff = (new Random().nextInt(1) + 1.5);
        if (nivel.getDependenciaStatus() != -1)
            diff -= jogo.getPlayer().getAttr(nivel.getDependenciaStatus());

        double velocidade = 5 * diff;

        objs = new ObjetoMinigame[10 * (int)diff];

        for (int i = 0; i < 10 * (int)diff; i++) {
            ObjetoMinigame obj = new ObjetoMinigame(new Random().nextInt(4), velocidade);

            obj.setX(width + i * (450 - 20 * (float)diff));
            obj.setY((height-110)/2);

            objs[i] = obj;
        }

        canvas.setObjs(objs);

        llFundoMinigame2.removeAllViews();
        llFundoMinigame2.addView(canvas);

        threadMinigame2 = new ThreadMinigame2(canvas);

        Uteis.controle.setEventos(new Eventos(){
            @Override
            public void onOK() {
                final ObjetoMinigame obj = getObjetoAtual();
                if (obj.getTipo() == -1 || obj.getX() + obj.getWidth()/2 > llFundoMinigame2.getMeasuredWidth() * click || obj.getX() + obj.getWidth()/2 < 0) {
                    perderMinigame2();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            obj.fade();
                        }
                    });
                }
            }

            @Override
            public void onPraEsquerda() {
                final ObjetoMinigame obj = getObjetoAtual();
                if (obj.getTipo() != 0 || obj.getX() + obj.getWidth()/2 > llFundoMinigame2.getMeasuredWidth() * click || obj.getX() + obj.getWidth()/2 < 0) {
                    perderMinigame2();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            obj.fade();
                        }
                    });
                }
            }

            @Override
            public void onPraCima() {
                final ObjetoMinigame obj = getObjetoAtual();
                if (obj.getTipo() != 1 || obj.getX() + obj.getWidth()/2 > llFundoMinigame2.getMeasuredWidth() * click || obj.getX() + obj.getWidth()/2 < 0) {
                    perderMinigame2();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            obj.fade();
                        }
                    });
                }
            }

            @Override
            public void onPraDireita() {
                final ObjetoMinigame obj = getObjetoAtual();
                if (obj.getTipo() != 2 || obj.getX() + obj.getWidth()/2 > llFundoMinigame2.getMeasuredWidth() * click || obj.getX() + obj.getWidth()/2 < 0) {
                    perderMinigame2();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            obj.fade();
                        }
                    });
                }
            }

            @Override
            public void onPraBaixo() {
                final ObjetoMinigame obj = getObjetoAtual();
                if (obj.getTipo() != 3 || obj.getX() + obj.getWidth()/2 > llFundoMinigame2.getMeasuredWidth() * click || obj.getX() + obj.getWidth()/2 < 0) {
                    perderMinigame2();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            obj.fade();
                        }
                    });
                }
            }

            @Override
            public void onMenu() {
                abrirMenu();
            }
        });

        threadMinigame2.start();
    }

    //endregion

    //region Menu
    int atualMenu;
    Button[] btnsMenu;
    public void pararTudo()
    {
        switch (nivel.getTipo())
        {
            case 0:
                Uteis.pararEscrever();
                break;

            case 1:
                timerMinigame1.pausar();
                break;

            case 2:
                threadMinigame2.pausar();
                break;
        }
    }

    public void continuarTudo()
    {
        switch (nivel.getTipo())
        {
            case 0:
                Uteis.continuarEscrever();
                break;

            case 1:
                timerMinigame1.continuar();
                break;

            case 2:
                threadMinigame2.continuar();
                break;
        }
    }

    public void desselecionarMenu()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Button btn : btnsMenu) {
                    btn.setBackgroundResource(android.R.drawable.btn_default);
                }
            }
        });
    }

    public void selecionarMenu(final boolean focus)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                desselecionarMenu();
                if (focus)
                    btnsMenu[atualMenu].findFocus();

                ShapeDrawable shapedrawable = new ShapeDrawable();
                shapedrawable.setShape(new RectShape());
                shapedrawable.getPaint().setColor(Uteis.corSelecionado);
                shapedrawable.getPaint().setStrokeWidth(10f);
                shapedrawable.getPaint().setStyle(Paint.Style.STROKE);
                btnsMenu[atualMenu].setBackground(shapedrawable);
            }
        });

    }

    public void abrirMenu()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Dialog menu = new Dialog(JogoActivity.this);
                final Eventos eventos = Uteis.controle.eventos;

                pararTudo();

                Uteis.controle.setEventos(new Eventos() {
                    @Override
                    public void onCancelar() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                menu.dismiss();
                                hide();
                                Uteis.controle.setEventos(eventos);
                                continuarTudo();
                            }
                        });
                    }

                    @Override
                    public void onOK() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnsMenu[atualMenu].performClick();
                            }
                        });
                    }
                    @Override
                    public void onPraCima() {
                        atualMenu++;
                        if (atualMenu > 1)
                            atualMenu = 0;
                        selecionarMenu(true);
                    }

                    @Override
                    public void onPraBaixo() {
                        atualMenu--;
                        if (atualMenu < 0)
                            atualMenu = 1;
                        selecionarMenu(true);
                    }
                });

                menu.setContentView(R.layout.menu_dialog);

                atualMenu = 0;
                btnsMenu = new Button[2];
                btnsMenu[0] = (Button)menu.findViewById(R.id.btnContinuar);
                btnsMenu[1] = (Button)menu.findViewById(R.id.btnSair);
                selecionarMenu(true);

                btnsMenu[0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uteis.controle.eventos.onCancelar();
                    }
                });
                btnsMenu[0].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        atualMenu = 0;
                        if (b)
                            selecionarMenu(false);
                    }
                });

                btnsMenu[1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uteis.alertar("Ao sair agora, a fase atual NÃO será salva. Sair mesmo assim?", "ATENÇÃO", new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        exit();
                                    }
                                });
                            }
                        }, null, JogoActivity.this);
                    }
                });
                btnsMenu[1].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        atualMenu = 1;
                        if (b)
                            selecionarMenu(false);
                    }
                });
                menu.show();
                Window window = menu.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        });


    }
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_jogo);

            Uteis.setActivity(JogoActivity.this);

            iniciarVariaveis();

            iniciarFullscreen();

            btnAvancarMinigame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnAvancarMinigame.setVisibility(View.INVISIBLE);
                    iniciarNivel();
                }
            });

            Uteis.controle.setEventos(new Eventos() {
                @Override
                public void onMenu() {
                    abrirMenu();
                }
            });

            Uteis.setTimeout(new Runnable() {
                @Override
                public void run() {
                    iniciarNivel();
                }
            }, 100);

        } catch (Exception ex) {
            //Toast.makeText(JogoActivity.this, ex.getMessage(), Toast.LENGTH_LONG).show();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    exit();
                }
            });
        }
    }

    public void exit()
    {
        final Runnable sair = new Runnable() {
            @Override
            public void run() {
                ClienteWS.postObjeto(new JogoRecebido(jogo, slotAtual, Uteis.getIPAddress(true)), String.class, ClienteWS.webService + "/atualizarJogo");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (threadMinigame2 != null)
                            threadMinigame2.interrupt();

                        Intent intent = new Intent(JogoActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        };
        final Dialog fim = new Dialog(JogoActivity.this, android.R.style.Theme_Light);
        fim.requestWindowFeature(Window.FEATURE_NO_TITLE);
        fim.setContentView(R.layout.fim_dialog);

        Uteis.controle.setEventos(new Eventos() {
            @Override
            public void onOK() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fim.dismiss();
                        sair.run();
                    }
                });
            }
        });

        fim.show();
    }
}
