package br.unicamp.cotuca.schmoice;

import android.annotation.SuppressLint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class JogoActivity extends AppCompatActivity {
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
    }
    //endregion

    ImageView imgCenario;
    RelativeLayout conteudoFullScreen;
    LinearLayout rlPersonagens;

    Jogo jogo;
    Nivel nivel;
    Controle controle;

    //region Variáveis de nivel normal
    TextView tvDescricao;
    LinearLayout llEscolhas;
    Button[] btnsEscolha;
    ArrayList<Escolha> escolhas;
    //endregion

    //region Minigame 2
    LinearLayout llFundoMinigame2;

    CanvasMinigame canvas;
    ObjetoMinigame[] objs;
    Thread threadMinigame2;
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
        Uteis.alertar("Você perdeu. Deseja tentar novamente?", "Perdeu", new Runnable() {
            @Override
            public void run() {
                threadMinigame2.interrupt();
                canvas = null;
                objs = null;
                iniciarNivel();
            }
        }, new Runnable() {
            @Override
            public void run() {
                exit();
            }
        }, JogoActivity.this);
    }
    public void ganharMinigame2() {
        canvas.setFim(true);
        try {
            Thread.sleep(1000);
            jogo.getArvore().getFaseAtual().avancarNivel();
            iniciarNivel();
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public class ObjetoMinigame {
        private int tipo, cor, v, alpha;
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

        public ObjetoMinigame(int t, int vel) {
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
                setX(getX() - v);
            }
            if (getX() < -1 * getWidth() && alpha == 255) {
                perderMinigame2();
            }
        }
        private int getCorAleatoria() {
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            return color;
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
            controle.eventos.onOK();
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

    public class ThreadMinigame2 implements Runnable {
        CanvasMinigame canvas;

        public ThreadMinigame2(CanvasMinigame canvas)
        {
            this.canvas = canvas;
        }

        @Override
        public void run() {
            while(!canvas.isFim())
                canvas.invalidate();
        }
    }
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        Uteis.setActivity(JogoActivity.this);

        btnsEscolha        = new Button[4];
        imgCenario         = (ImageView)      findViewById(R.id.imgCenario);
        tvDescricao        = (TextView)       findViewById(R.id.tvDescricao);
        btnsEscolha[0]     = (Button)         findViewById(R.id.btnEscolha1);
        btnsEscolha[1]     = (Button)         findViewById(R.id.btnEscolha2);
        btnsEscolha[2]     = (Button)         findViewById(R.id.btnEscolha3);
        btnsEscolha[3]     = (Button)         findViewById(R.id.btnEscolha4);
        llFundoMinigame2   = (LinearLayout)   findViewById(R.id.llFundoMinigame2);
        llEscolhas         = (LinearLayout)   findViewById(R.id.llEscolhas);
        conteudoFullScreen = (RelativeLayout) findViewById(R.id.conteudoFullScreen);
        rlPersonagens      = (LinearLayout)   findViewById(R.id.rlPersonagens);

        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        jogo = (Jogo)params.getSerializable("jogo");
        controle = (Controle)params.getSerializable("controle");

        nivel = jogo.getArvore().getFaseAtual().getNivelAtual();

        imgCenario.setImageBitmap(Uteis.getImageByName(nivel.getBackground()));
        escolhas = nivel.getEscolhas();

        iniciarFullscreen();

        Uteis.setTimeout(new Runnable() {
            @Override
            public void run() {
                iniciarNivel();
            }
        }, 100);
    }

    public void iniciarNivel()
    {
        nivel = jogo.getArvore().getFaseAtual().getNivelAtual();

        if (nivel.isTerminado()) {
            exit();
            return;
        }

        int numPersonagens = nivel.getPersonagens().size();

        for(int i = 0; i < numPersonagens; i++) {
            Personagem personagem = nivel.getPersonagens().get(i);
            ImageView iv = new ImageView(JogoActivity.this);

            iv.setImageBitmap(personagem.getBmp());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.width = (int)personagem.getWidth();
            lp.setMargins((int)personagem.getX(), (int)personagem.getY(), 0, 0);

            rlPersonagens.addView(iv);
            iv.setPivotX(iv.getWidth()/2);
            iv.setPivotY(iv.getHeight()/2);
            iv.setRotation(personagem.getRotation());
            iv.setLayoutParams(lp);
            iv.requestLayout();
        }

        switch (nivel.getTipo())
        {
            case 0:
                Uteis.escurecerFundo(imgCenario, 0.6f);
                iniciarNivelNormal();
                break;

            case 1:
                llEscolhas.setVisibility(View.GONE);
                iniciarMinigame1();
                break;

            case 2:
                Uteis.escurecerFundo(imgCenario, 0.35f);
                llEscolhas.setVisibility(View.GONE);
                iniciarMinigame2();
                break;

            case -1:
                exit();
                break;
        }
    }

    public void iniciarNivelNormal()
    {
        Uteis.escreverAnimado(tvDescricao, nivel.getDescricao());

        for (int i = 0; i < escolhas.size(); i++) {
            final int ind = i;
            btnsEscolha[i].setText(escolhas.get(i).getNome());
            btnsEscolha[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nivel.efetuarEscolha(escolhas.get(ind));

                    nivel = jogo.getArvore().getFaseAtual().getNivelAtual();

                    iniciarNivel();
                }
            });
        }
    }
    public void iniciarMinigame1()
    {
        Intent intentMinigame = new Intent(JogoActivity.this, Minigame1Activity.class);
        Bundle pars = new Bundle();

        pars.putSerializable("controle", controle);
        pars.putSerializable("jogo", jogo);
        String img = jogo.getArvore().getFaseAtual().getNivelAtual().getBackground();
        int ids = Uteis.getImageIdByName(img);
        pars.putInt("cenario", ids);
        pars.putInt("personagem", ids);
        pars.putInt("diff", jogo.getArvore().getFaseAtual().getNivelAtual().getDiff());

        intentMinigame.putExtras(pars);
        startActivity(intentMinigame);
    }
    public void iniciarMinigame2()
    {
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controle.eventos.onOK();
            }
        });

        canvas = new CanvasMinigame(JogoActivity.this);

        int width = llFundoMinigame2.getMeasuredWidth();
        int height = llFundoMinigame2.getMeasuredHeight();
        int velocidade = 5 * nivel.getDiff();

        objs = new ObjetoMinigame[10 * nivel.getDiff()];

        for (int i = 0; i < 10 * nivel.getDiff(); i++) {
            ObjetoMinigame obj = new ObjetoMinigame(new Random().nextInt(4), velocidade);

            obj.setX(width + i * (450 - 20 * nivel.getDiff()));
            obj.setY((height-110)/2);

            objs[i] = obj;
        }

        canvas.setObjs(objs);

        llFundoMinigame2.removeAllViews();
        llFundoMinigame2.addView(canvas);

        threadMinigame2 = new Thread(new ThreadMinigame2(canvas));

        controle.setEventos(new Eventos(){
            @Override
            public void onOK() {
                ObjetoMinigame obj = getObjetoAtual();
                if (obj.getTipo() == -1 || obj.getX() + obj.getWidth()/2 > llFundoMinigame2.getMeasuredWidth() * click || obj.getX() + obj.getWidth()/2 < 0) {
                    perderMinigame2();
                } else {
                    obj.fade();
                }
            }

            @Override
            public void onPraEsquerda() {
                ObjetoMinigame obj = getObjetoAtual();
                if (obj.getTipo() != 0 || obj.getX() + obj.getWidth()/2 > llFundoMinigame2.getMeasuredWidth() * click || obj.getX() + obj.getWidth()/2 < 0) {
                    perderMinigame2();
                } else {
                    obj.fade();
                }
            }

            @Override
            public void onPraCima() {
                ObjetoMinigame obj = getObjetoAtual();
                if (obj.getTipo() != 1 || obj.getX() + obj.getWidth()/2 > llFundoMinigame2.getMeasuredWidth() * click || obj.getX() + obj.getWidth()/2 < 0) {
                    perderMinigame2();
                } else {
                    obj.fade();
                }
            }

            @Override
            public void onPraDireita() {
                ObjetoMinigame obj = getObjetoAtual();
                if (obj.getTipo() != 2 || obj.getX() + obj.getWidth()/2 > llFundoMinigame2.getMeasuredWidth() * click || obj.getX() + obj.getWidth()/2 < 0) {
                    perderMinigame2();
                } else {
                    obj.fade();
                }
            }

            @Override
            public void onPraBaixo() {
                ObjetoMinigame obj = getObjetoAtual();
                if (obj.getTipo() != 3 || obj.getX() + obj.getWidth()/2 > llFundoMinigame2.getMeasuredWidth() * click || obj.getX() + obj.getWidth()/2 < 0) {
                    perderMinigame2();
                } else {
                    obj.fade();
                }
            }
        });

        threadMinigame2.start();
    }

    public void exit()
    {
        threadMinigame2.interrupt();
        Intent intent = new Intent(JogoActivity.this, SavesActivity.class);
        Bundle params = new Bundle();
        controle.setEventos(null);
        params.putSerializable("controle", controle);
        intent.putExtras(params);
        startActivity(intent);
    }
}
