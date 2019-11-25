package br.unicamp.cotuca.schmoice;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ThreadEscreverTv extends Thread {
    final TextView tv;
    final String s;
    final int indA, size;
    final boolean[] morto = new boolean[1];
    final Activity act;
    final TextView c;

    public boolean getMorto() {return morto[0];}
    public void setMorto(boolean b) {
        morto[0] = b;
        if (b)
        {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            c.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }, 5000);
        }
    }

    public ThreadEscreverTv(TextView tv, String s, int ind, int p, Activity a, TextView t)
    {
        this.tv = tv;
        this.s = s;
        indA = ind;
        morto[0] = false;
        size = p;
        act = a;
        c = t;
    }

    @Override
    public void run() {
        super.run();
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                morto[0] = false;
                tv.setText("");
                c.setVisibility(View.INVISIBLE);

                final int[] i = new int[1];
                i[0] = 0;
                final int length = s.length();
                final Handler handler = new Handler()
                {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        try {
                            char c= s.charAt(i[0]);
                            if (!morto[0]) {
                                tv.append(String.valueOf(c));
                                i[0]++;
                            } else {
                                i[0] = length - 1;
                            }
                        }
                        catch (StringIndexOutOfBoundsException ex) {
                            return;
                        }
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
                timer.schedule(taskEverySplitSecond, 1, 50);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!morto[0]) {
                            morto[0] = true;
                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    act.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            c.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            }, 5000);
                        }
                    }
                }, 50 * length + 50);
            }
        });
    }
}
