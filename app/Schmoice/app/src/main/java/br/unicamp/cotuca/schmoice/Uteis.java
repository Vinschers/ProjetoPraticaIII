package br.unicamp.cotuca.schmoice;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

public class Uteis {
    public static Activity activity;

    public static Controle controle;

    public static int corSelecionado = Color.BLUE;

    public static void setActivity(Activity a) {activity = a;}

    private static final Uteis ourInstance = new Uteis();

    public static Uteis getInstance() {
        return ourInstance;
    }

    private Uteis() {
    }

    public static void escurecerFundo(final View v, float alphaEscuro) {
        final ValueAnimator colorAnimation = ValueAnimator.ofFloat(v.getAlpha(), alphaEscuro);
        colorAnimation.setDuration(750); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                v.setAlpha((float) colorAnimation.getAnimatedValue());
            }
        });
        colorAnimation.start();
    }
    public static void clarearFundo(final View v) {
        final ValueAnimator colorAnimation = ValueAnimator.ofFloat(v.getAlpha(), 0);
        colorAnimation.setDuration(750); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                v.setAlpha((float) colorAnimation.getAnimatedValue());
            }
        });
        colorAnimation.start();
    }


    public static void escreverAnimado(final TextView tv, final TextView c, final String s, final Controle controle, final Activity act, final Runnable onFim)
    {
        if (s == null || s.equals(""))
        {
            onFim.run();
            return;
        }
        String[] palavras = s.split("(?<= )");

        final ArrayList<String> partes = new ArrayList<String>();
        String parteAtual = "";
        int letterCount = 0;

        for (String palavra : palavras) {
            letterCount += palavra.length();

            if (letterCount > 64) {
                partes.add(parteAtual);
                parteAtual = "";
                letterCount = 0;
            }
            parteAtual += palavra;
        }
        partes.add(parteAtual);

        final int[] i = new int[1];
        i[0] = -1;

        final ThreadEscreverTv[] runs = new ThreadEscreverTv[partes.size()];
        for (int ind = 0; ind < partes.size(); ind -=- 1) {
            runs[ind] = new ThreadEscreverTv(tv, partes.get(ind), ind, partes.size(), act, c);
        }

        final Runnable avancar = new Runnable() {
            @Override
            public void run() {
                if (i[0] > -1 && !runs[i[0]].getMorto())
                {
                    runs[i[0]].setMorto(true);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(partes.get(i[0]));
                        }
                    });
                }
                else {
                    try {
                        runs[++i[0]].start();
                    } catch (IndexOutOfBoundsException ex) {
                        onFim.run();
                    }
                }
            }
        };

        controle.setEventos(new Eventos(){
            @Override
            public void onOK() {
                avancar.run();
            }
        });

        avancar.run();

        tv.setOnClickListener(new View.OnClickListener() {      //TIRAR DEPOIS
            @Override
            public void onClick(View view) {
                controle.eventos.onOK();
            }
        });
    }
    public static class ThreadRedimensionar extends Thread
    {
        boolean atual = false, funcionando = true;
        final int HEIGHT_SMALL = 180;
        final int HEIGHT_BIG = HEIGHT_SMALL + 20;
        final int WIDTH_SMALL = 325;
        final int WIDTH_BIG = WIDTH_SMALL + 20;
        final int MARGIN_SMALL = 100;
        final int MARGIN_BIG = 87;
        private ViewGroup ll;

        public void matar()
        {
            funcionando = false;
        }

        public ThreadRedimensionar(ViewGroup l)
        {
            ll = l;
        }

        @Override
        public void run() {
            while(funcionando) {
                try {
                    if (atual) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)ll.getLayoutParams();
                                params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HEIGHT_SMALL, activity.getResources().getDisplayMetrics());
                                params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, WIDTH_SMALL, activity.getResources().getDisplayMetrics());
                                params.topMargin = MARGIN_SMALL;
                                ll.setLayoutParams(params);
                            }
                        });
                    } else {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)ll.getLayoutParams();
                                params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HEIGHT_BIG, activity.getResources().getDisplayMetrics());
                                params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, WIDTH_BIG, activity.getResources().getDisplayMetrics());
                                params.topMargin = MARGIN_BIG;
                                ll.setLayoutParams(params);
                            }
                        });
                    }
                    atual = !atual;
                    Thread.sleep(500);
                } catch (Exception e) {
                    new AlertDialog.Builder(activity.getBaseContext())
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

    public static int getImageIdByName(String imageName) {
        return activity.getResources().getIdentifier(imageName, "drawable", activity.getPackageName());
    }
    public static Bitmap getImageByName(String imageName){
        return BitmapFactory.decodeResource(activity.getResources(), getImageIdByName(imageName));
    }

    public static void setTimeout(final Runnable func, int millis)
    {
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        func.run();
                    }
                },
                millis);
    }

    public static void alertar(String txt, String titulo, final Runnable onYes, final Runnable onNo, Context context)
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        onYes.run();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        onNo.run();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(txt).setTitle(titulo)
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }
}