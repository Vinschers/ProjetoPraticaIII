package br.unicamp.cotuca.schmoice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConfigActivity extends AppCompatActivity {

    Button btnConectar, btnDesconectar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        btnConectar = (Button)findViewById(R.id.btnConectar);
        btnDesconectar = (Button)findViewById(R.id.btnDesconectar);
        btnConectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });
        btnDesconectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
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
