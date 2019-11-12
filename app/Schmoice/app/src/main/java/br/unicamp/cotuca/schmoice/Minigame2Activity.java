package br.unicamp.cotuca.schmoice;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Random;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Minigame2Activity extends AppCompatActivity {
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


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controle.eventos.onOK();
            }
        });
    }
    //endregion

    LinearLayout llFundoMinigame;
    ImageView imgCenario, imgViewFundoJogo;
    CanvasMinigame canvas;
    ObjetoMinigame[] objs;
    final float click = 0.30f;

    Controle controle;
    Jogo jogo;
    int diff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minigame2);
        iniciarFullscreen();
        canvas = new CanvasMinigame(Minigame2Activity.this);
        imgViewFundoJogo = (ImageView)findViewById(R.id.imgViewFundoJogo);
        llFundoMinigame = (LinearLayout)findViewById(R.id.llFundoMinigame);
        imgCenario = (ImageView)findViewById(R.id.imgCenario);
        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        diff = params.getInt("diff");
        controle = (Controle)params.getSerializable("controle");
        jogo = (Jogo)params.getSerializable("jogo");
        //imgCenario.setImageBitmap(jogo.getArvore().getFaseAtual().getNivelAtual().getBackground());
        imgCenario.setImageBitmap(getImageByName("oi"));

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        iniciarMinigame();
                    }
                },
                100);
    }
    public void iniciarMinigame() {
        int w = llFundoMinigame.getMeasuredWidth();
        int h = llFundoMinigame.getMeasuredHeight();
        int v = 5 * diff;
        objs = new ObjetoMinigame[10 * diff];
        for (int i = 0; i < 10 * diff; i++) {
            ObjetoMinigame obj = new ObjetoMinigame(new Random().nextInt(4), v);
            obj.setX(w + i * (450 - 20*diff));
            obj.setY((h-110)/2);
            objs[i] = obj;
        }
        canvas.setObjs(objs);
        llFundoMinigame.removeAllViews();
        llFundoMinigame.addView(canvas);

        Thread thread = new Thread(new ThreadJogo(canvas));

        controle.setEventos(new Eventos(){
            @Override
            public void onOK() {
                ObjetoMinigame obj = getObjetoAtual();
                if (obj.getTipo() == -1 || obj.getX() + obj.getWidth()/2 > llFundoMinigame.getMeasuredWidth() * click || obj.getX() + obj.getWidth()/2 < 0) {
                    perder();
                } else {
                    obj.fade();
                }
            }

            @Override
            public void onPraEsquerda() {
                ObjetoMinigame obj = getObjetoAtual();
                if (obj.getTipo() != 0 || obj.getX() + obj.getWidth()/2 > llFundoMinigame.getMeasuredWidth() * click || obj.getX() + obj.getWidth()/2 < 0) {
                    perder();
                } else {
                    obj.fade();
                }
            }

            @Override
            public void onPraCima() {
                ObjetoMinigame obj = getObjetoAtual();
                if (obj.getTipo() != 1 || obj.getX() + obj.getWidth()/2 > llFundoMinigame.getMeasuredWidth() * click || obj.getX() + obj.getWidth()/2 < 0) {
                    perder();
                } else {
                    obj.fade();
                }
            }

            @Override
            public void onPraDireita() {
                ObjetoMinigame obj = getObjetoAtual();
                if (obj.getTipo() != 2 || obj.getX() + obj.getWidth()/2 > llFundoMinigame.getMeasuredWidth() * click || obj.getX() + obj.getWidth()/2 < 0) {
                    perder();
                } else {
                    obj.fade();
                }
            }

            @Override
            public void onPraBaixo() {
                ObjetoMinigame obj = getObjetoAtual();
                if (obj.getTipo() != 3 || obj.getX() + obj.getWidth()/2 > llFundoMinigame.getMeasuredWidth() * click || obj.getX() + obj.getWidth()/2 < 0) {
                    perder();
                } else {
                    obj.fade();
                }
            }
        });
        thread.start();
    }

    public Bitmap getImageByName(String imageName){
        int id = getResources().getIdentifier(imageName, "drawable", getPackageName());
        return BitmapFactory.decodeResource(getResources(), id);
    }
    public ObjetoMinigame getObjetoAtual() {
        for (ObjetoMinigame obj : objs) {
            if (obj.getX() + obj.getWidth() > 0)
                return obj;
        }
        return new ObjetoMinigame(-1, 0);
    }
    public void perder() {
        canvas.setFim(true);
        new AlertDialog.Builder(Minigame2Activity.this)
                .setTitle("Perdeu")
                .setMessage("Você perdeu. Deseja tentar novamente?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(getIntent());
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Minigame2Activity.this, SavesActivity.class);
                        Bundle params = new Bundle();
                        params.putSerializable("controle", controle);
                        intent.putExtras(params);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public void ganhar() {
        canvas.setFim(true);
        try {
            Thread.sleep(1000);
            Intent intent2 = new Intent(Minigame2Activity.this, JogoActivity.class);
            Bundle params = new Bundle();
            jogo.getArvore().getFaseAtual().avancarNivel();
            controle.setEventos(null);
            params.putSerializable("controle", controle);
            params.putSerializable("jogo", jogo);
            intent2.putExtras(params);
            startActivity(intent2);
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
                perder();
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
                    ganhar();
            }
        }
        private void drawPartes(Canvas canvas) {
            int w = llFundoMinigame.getMeasuredWidth();
            int h = llFundoMinigame.getMeasuredHeight();
            int corPassar = Color.argb(230, 0, 0, 0);
            int corClick = Color.argb(120, 255, 255, 0);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(corPassar);
            canvas.drawPaint(paint);
            paint.setColor(corClick);
            canvas.drawRect(getX(), getY(), w * click, h, paint);
        }
    }

    public class ThreadJogo implements Runnable {
        CanvasMinigame canvas;

        public ThreadJogo(CanvasMinigame canvas)
        {
            this.canvas = canvas;
        }

        @Override
        public void run() {
            while(!canvas.isFim())
                canvas.invalidate();
        }
    }
}
