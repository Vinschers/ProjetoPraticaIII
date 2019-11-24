package br.unicamp.cotuca.schmoice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ConfigActivity extends AppCompatActivity {
    Controle controle;
    EditText edtAdress;
    Button btnConectar, btnDesconectar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        controle = (Controle)params.getSerializable("controle");
        edtAdress = (EditText)findViewById(R.id.edtAddress);
        edtAdress.setText(Controle.macAdress);
        btnConectar = (Button)findViewById(R.id.btnConectar);
        btnDesconectar = (Button)findViewById(R.id.btnDesconectar);
        btnConectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (controle.conectar(false)) {
                    Intent intent = new Intent(ConfigActivity.this, MainActivity.class);
                    Bundle params = new Bundle();
                    params.putSerializable("controle", controle);
                    intent.putExtras(params);

                    controle.desconectar(false);

                    startActivity(intent);
                }
            }
        });
        btnDesconectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controle.desconectar(false);
            }
        });
    }
}
/*
buttonConnect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                /*if (!editTextAddress.getText().toString().isEmpty()) {
                    SERVER_IP = editTextAddress.getText().toString();
                    SERVERPORT = Integer.parseInt(editTextPort.getText().toString());
                }


                if (socket != null && !socket.isClosed()) {
                        try {
                        socket.close();
                        } catch (IOException e) {
                        e.printStackTrace();
                        }
                        }
                        new Thread(new ClientThread()).start();

                        }
                        });

                        buttonClose.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        val = "\r\n\r\n";
        new Thread(new SendThread()).start();
        }
        });

        btnEfeito1.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        try {

        val = "1";
        new Thread(new SendThread()).start();
        } catch (Exception e) {
        e.printStackTrace();
        }
        }
        });
        btnEfeito2.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        try {
        val = "2";
        new Thread(new SendThread()).start();
        } catch (Exception e) {
        e.printStackTrace();
        }
        }
        });
        btnEfeito3.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        try {
        val = "3";
        new Thread(new SendThread()).start();
        } catch (Exception e) {
        e.printStackTrace();
        }
        }
        });
        }
 */
