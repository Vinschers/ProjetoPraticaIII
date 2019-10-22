package br.unicamp.cotuca.schmoice;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
                toggle();
            }
        });
    }
    //endregion

    LinearLayout llFundoMinigame, llClick, llPassar;
    ImageView imgCenario, imgViewFundoJogo;
    AnimacaoMinigame animacao;

    Jogo jogo;
    int diff;
    //ObjetoMinigame animacao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minigame2);
        imgViewFundoJogo = (ImageView)findViewById(R.id.imgViewFundoJogo);
        llFundoMinigame = (LinearLayout)findViewById(R.id.llFundoMinigame);
        imgCenario = (ImageView)findViewById(R.id.imgCenario);
        llClick = (LinearLayout)findViewById(R.id.llClick);
        llPassar = (LinearLayout)findViewById(R.id.llPassar);
        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        //diff = params.getInt("diff");
        jogo = (Jogo)params.getSerializable("jogo");
        diff = 1;
        //imgCenario.setImageBitmap(jogo.getArvore().getFaseAtual().getNivelAtual().getBackground());
        llFundoMinigame.setBackgroundColor(Color.argb(240, 0, 0, 0));
        llClick.setBackgroundColor(Color.argb(200, 255, 255, 224));
        llPassar.setBackgroundColor(Color.argb(120, 255, 255, 255));
        imgCenario.setImageBitmap(getImageByName("oi"));

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        int w = llFundoMinigame.getMeasuredWidth();
                        int h = llFundoMinigame.getMeasuredHeight();
                        ObjetoMinigame[] objs = new ObjetoMinigame[10 * diff];
                        for (int i = 0; i < 10 * diff; i++) {
                            objs[i] = new ObjetoMinigame(Minigame2Activity.this);
                            objs[i].setTipo(1);
                            objs[i].setX(w - (i * objs[i].getWidth()) + 40);
                            objs[i].setY(h);
                            llFundoMinigame.addView(objs[i]);
                        }

                        animacao = new AnimacaoMinigame(objs);
                        new Thread(animacao).start();
                    }
                },
                500);
        iniciarFullscreen();
    }
    public Bitmap getImageByName(String imageName){
        int id = getResources().getIdentifier(imageName, "drawable", getPackageName());
        return BitmapFactory.decodeResource(getResources(), id);
    }


    public class AnimacaoMinigame implements Runnable {
        private int vel;
        private boolean morta;
        private ObjetoMinigame[] objsMinigame;
        public AnimacaoMinigame(ObjetoMinigame[] o) {
            objsMinigame = o;
            vel = diff * 10;
        }
        private void mover() {
            boolean acabou = true;
            for(ObjetoMinigame obj : objsMinigame) {
                if (obj.getWidth() + obj.getX() > 0) {
                    obj.setX(obj.getX() - vel);
                    acabou = false;
                }
            }
            setMorta(acabou);
        }
        public void setMorta(boolean t) {
            morta = t;
        }
        @Override
        public void run() {
            while(!morta) {
                mover();
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public class ObjetoMinigame extends View {
        private Paint paint;
        private int tipo, cor;

        public int getTipo() {
            return tipo;
        }

        public void setTipo(int tipo) {
            this.tipo = tipo;
        }

        public ObjetoMinigame(Context context) {
            super(context);
            cor = getCorAleatoria();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            paint = new Paint();
            drawArrow(canvas);
        }
        private int getCorAleatoria() {
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            return color;
        }
        private void drawArrow(Canvas canvas) {
            paint.setStyle(Paint.Style.STROKE);
            //paint.setStyle(Paint.Style.FILL);
            paint.setColor(cor);
            canvas.drawRect(getX(), getY(), getX() + 100, getY() + 50, paint);
            Path path = new Path();
            Point p1 = new Point((int)getX(), (int)getY()-30);
            Point p2 = new Point((int)getX()-50, (int)getY() + 25);
            Point p3 = new Point((int)getX(), (int)getY()+80);
            path.moveTo(p1.x, p1.y);
            path.lineTo(p2.x, p2.y);
            path.moveTo(p2.x, p2.y);
            path.lineTo(p3.x, p3.y);
            path.moveTo(p3.x, p3.y);
            path.lineTo(p1.x, p1.y);
            path.close();
            canvas.drawPath(path, paint);
            canvas.rotate(tipo * 90);
        }
    }
}
