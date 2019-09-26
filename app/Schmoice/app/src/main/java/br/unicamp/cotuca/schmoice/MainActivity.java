package br.unicamp.cotuca.schmoice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView tvTitulo;
    Button btnJogar, btnConfig;
    Controle controle;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTitulo = (TextView)findViewById(R.id.tvTitulo);
        btnJogar = (Button)findViewById(R.id.btnJogar);
        btnConfig = (Button)findViewById(R.id.btnConfig);
        context = MainActivity.this;
        btnJogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (controle != null) {
                    Jogar();
                } else {
                    new AlertDialog.Builder(context)
                            .setTitle("Erro de comunicacao")
                            .setMessage("Controle nao foi conectado corretamente. Ir para configuracoes?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Configuracoes();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });
        SpannableString content = new SpannableString("Schmoice");
        content.setSpan(new UnderlineSpan(), 0, 8, 0);
        tvTitulo.setText(content);
    }

    private void Jogar() {
        //
    }
    private void Configuracoes() {
        //
    }
}
