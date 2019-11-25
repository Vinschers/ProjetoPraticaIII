package br.unicamp.cotuca.schmoice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.concurrent.Callable;

public class MainActivity extends AppCompatActivity {
    Button btnJogar, btnConfig;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnJogar = (Button)findViewById(R.id.btnJogar);
        btnConfig = (Button)findViewById(R.id.btnConfig);
        context = MainActivity.this;
        btnJogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Uteis.controle != null && Uteis.controle.funcionando) {
                    Jogar();
                } else {
                    new AlertDialog.Builder(context)
                            .setTitle("Erro de comunicação")
                            .setMessage("Controle não foi conectado corretamente. Ir para configurações?")
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
        btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Configuracoes();
            }
        });
    }

    private void Jogar() {
        Intent intent = new Intent(MainActivity.this, SavesActivity.class);
        startActivity(intent);
    }
    private void Configuracoes() {
        if (Uteis.controle == null)
            Uteis.controle = new Controle();

        Intent intent = new Intent(MainActivity.this, ConfigActivity.class);
        startActivity(intent);
    }
}
