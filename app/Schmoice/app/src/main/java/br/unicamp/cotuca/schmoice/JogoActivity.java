package br.unicamp.cotuca.schmoice;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

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


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
    }
    //endregion

    Button[] btnsEscolha;
    ImageView imgCenario;
    Jogo jogo;
    ArrayList<Escolha> escolhas;
    Controle controle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);
        iniciarFullscreen();
        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        jogo = (Jogo)params.getSerializable("jogo");
        controle = (Controle)params.getSerializable("controle");
        final Jogo jogoAtual = jogo;
        /*if (jogo.getAcabouDeComecar()) {
            Intent intentInicio = new Intent(JogoActivity.this, Minigame2Activity.class);
            startActivity(intentInicio);
        }*/
        if (jogo.getArvore().getFaseAtual().getNivelAtual().getTipo() == 1) {
            Intent intentMinigame = new Intent(JogoActivity.this, Minigame1Activity.class);
            Bundle pars = new Bundle();

            pars.putSerializable("controle", controle);
            pars.putSerializable("jogo", jogo);
            String img = jogo.getArvore().getFaseAtual().getNivelAtual().getBackground();
            int ids = getImageIdByName(img);
            pars.putInt("cenario", ids);
            pars.putInt("personagem", ids);

            intentMinigame.putExtras(pars);
            startActivity(intentMinigame);
        } else if (jogo.getArvore().getFaseAtual().getNivelAtual().getTipo() == 2) {
            Intent intentMinigame = new Intent(JogoActivity.this, Minigame2Activity.class);
            Bundle pars = new Bundle();

            pars.putSerializable("controle", controle);
            pars.putSerializable("diff", jogo.getArvore().getFaseAtual().getNivelAtual().getDiff());

            intentMinigame.putExtras(pars);
            startActivity(intentMinigame);
        } else {
            imgCenario = (ImageView)findViewById(R.id.imgCenario);

            btnsEscolha = new Button[4];
            btnsEscolha[0] = (Button)findViewById(R.id.btnEscolha1);
            btnsEscolha[1] = (Button)findViewById(R.id.btnEscolha2);
            btnsEscolha[2] = (Button)findViewById(R.id.btnEscolha3);
            btnsEscolha[3] = (Button)findViewById(R.id.btnEscolha4);

            escolhas = jogo.getArvore().getFaseAtual().getNivelAtual().getEscolhas();
            imgCenario.setImageBitmap(getImageByName(jogo.getArvore().getFaseAtual().getNivelAtual().getBackground()));

            for (int i = 0; i < escolhas.size(); i++) {
                final int ind = i;
                btnsEscolha[i].setText(escolhas.get(i).getNome());
                btnsEscolha[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        jogoAtual.getArvore().getFaseAtual().getNivelAtual().efetuarEscolha(escolhas.get(ind));
                        Intent inte = new Intent(JogoActivity.this, JogoActivity.class);
                        Bundle par = new Bundle();
                        par.putSerializable("jogo", jogo);
                        par.putSerializable("controle", controle);
                        inte.putExtras(par);
                        startActivity(inte);
                    }
                });
            }
        }
    }

    public int getImageIdByName(String imageName) {
        return getResources().getIdentifier(imageName, "drawable", getPackageName());
    }
    public Bitmap getImageByName(String imageName){
        return BitmapFactory.decodeResource(getResources(), getImageIdByName(imageName));
    }
}
