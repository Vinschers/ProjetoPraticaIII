package br.unicamp.cotuca.schmoice;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Minigame1Activity extends AppCompatActivity {
    //region Fullscreen
    private void iniciarFullscreen()
    {
        mVisible = true;
        mContentView = findViewById(R.id.fullscreen_content);


        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
    }

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
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                pbVida.incrementProgressBy(1); // TIRAR DEPOIS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            }
        });
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
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
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
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
    //endregion

    ImageView ivCenario, ivPersonagem;
    TextView tvTempo;
    ProgressBar pbVida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Intent intent = getIntent();
        Bundle params = intent.getExtras();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_minigame1);
        iniciarFullscreen();

        final Controle controle = (Controle)params.getSerializable("controle");
        controle.setEventos(new Eventos(){
            @Override
            public void onOK() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (!tvTempo.getText().equals("Ganhou!") && !tvTempo.getText().equals("Perdeu!"))
                            pbVida.incrementProgressBy(1); // TIRAR DEPOIS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    }
                });
            }
        });

        int idCenario = (int)params.get("cenario");
        int idPersonagem = (int)params.get("personagem");

        ivCenario = findViewById(R.id.ivCenario);
        ivPersonagem = findViewById(R.id.ivPersonagem);
        tvTempo = findViewById(R.id.tvTempo);
        pbVida = findViewById(R.id.pbVida);

        ivCenario.setImageResource(idCenario);
        ivPersonagem.setImageResource(idPersonagem);

        TimerJogo tmr = new TimerJogo(tvTempo, pbVida, new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    Intent intent2 = new Intent(Minigame1Activity.this, SavesActivity.class);
                    Bundle params = new Bundle();
                    controle.setEventos(null);
                    params.putSerializable("controle", controle);
                    params.putBoolean("ganhou", true);
                    intent2.putExtras(params);
                    startActivity(intent2);
                }
                catch (InterruptedException ex) {}
            }
        }, new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    Intent intent2 = new Intent(Minigame1Activity.this, SavesActivity.class);
                    Bundle params = new Bundle();
                    controle.setEventos(null);
                    params.putSerializable("controle", controle);
                    params.putBoolean("ganhou", false);
                    intent2.putExtras(params);
                    startActivity(intent2);
                }
                catch (InterruptedException ex) {}
            }
        });
        tmr.start();
    }
}
class TimerJogo extends Thread {

    TextView tvTimer;
    ProgressBar pbVida;
    Runnable onGanhou, onPerdeu;

    public TimerJogo(TextView tvTimer, ProgressBar pbVida, Runnable onGanhou, Runnable onPerdeu) {
        this.tvTimer = tvTimer;
        this.pbVida = pbVida;
        this.onGanhou = onGanhou;
        this.onPerdeu = onPerdeu;
    }

    public void run() {
        try {
            for (int segundos = 15; segundos >= 0; segundos--)
            {
                Thread.sleep(1000);
                tvTimer.setText(segundos + "");
                if (pbVida.getProgress() == pbVida.getMax())
                    break;
            }
            if (pbVida.getProgress() == pbVida.getMax()) {
                tvTimer.setText("Ganhou!");
                onGanhou.run();
            }
            else {
                tvTimer.setText("Perdeu!");
                onPerdeu.run();
            }
        }
        catch (InterruptedException ex) {}
        catch (Throwable ex) {System.out.println(ex.getMessage());}
    }
}
