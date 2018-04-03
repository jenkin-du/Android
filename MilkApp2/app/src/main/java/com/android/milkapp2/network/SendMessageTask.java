package com.android.milkapp2.network;

import android.os.Handler;
import android.os.Message;
import com.android.milkapp2.configration.Config;
import com.android.milkapp2.util.StreamWapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SendMessageTask extends Thread{

    private String msg;
    private Handler handler;

    private Socket socket=null;


    public SendMessageTask( String msg, Handler handler) {

        this.msg = msg+"`";
        this.handler = handler;
    }

    @Override
    public void run() {


        try {
            socket=new Socket(Config.IP,Config.PORT);
            //向服务器发送数据
            OutputStream os=socket.getOutputStream();
            PrintWriter writer= StreamWapper.toPrintWriter(os);
            writer.print(msg);
            writer.flush();

            //接受来自服务器的数据
            InputStream is=socket.getInputStream();
            BufferedReader br=StreamWapper.toBufferedReader(is);

            String jsonData=br.readLine();
            Message message=new Message();
            message.obj=jsonData;
            //将接受到的数据发送出去，用于更新UI
            handler.sendMessage(message);

            os.close();
            is.close();
            br.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (socket!=null){
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
