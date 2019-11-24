package br.unicamp.cotuca.schmoice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class HandlerControle extends Handler {
    @Override
    public void handleMessage(Message msg) {

            /* Esse método é invocado na Activity principal
                sempre que a thread de conexão Bluetooth recebe
                uma mensagem.
             */
        Bundle bundle = msg.getData();
        byte[] data = bundle.getByteArray("data");
        String dataString= new String(data);

            /* Aqui ocorre a decisão de ação, baseada na string
                recebida. Caso a string corresponda à uma das
                mensagens de status de conexão (iniciadas com --),
                atualizamos o status da conexão conforme o código.
             */
        //if(dataString.equals("---N"))
        //    statusMessage.setText("Ocorreu um erro durante a conexão D:");
        //else if(dataString.equals("---S"))
        //    statusMessage.setText("Conectado :D");
        //else {

                /* Se a mensagem não for um código de status,
                    então ela deve ser tratada pelo aplicativo
                    como uma mensagem vinda diretamente do outro
                    lado da conexão. Nesse caso, simplesmente
                    atualizamos o valor contido no TextView do
                    contador.
                 */
        //   counterMessage.setText(dataString);
        //}

    }
}
