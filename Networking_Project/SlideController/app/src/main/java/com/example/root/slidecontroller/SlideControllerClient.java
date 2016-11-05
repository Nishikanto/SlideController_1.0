package com.example.root.slidecontroller;

/**
 * Created by root on 9/23/16.
 */

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.net.*;
import java.io.*;

public class SlideControllerClient {

    private Socket socket = null;
    private DataInputStream console = null;
    private DataOutputStream streamOut = null;
    private boolean flag;
    private OutputStream ostream;
    private PrintWriter pwrite;


    public boolean connect(Context context, String serverName, int serverPort) {
        System.out.println("Establishing connection. Please wait ...");
        DBhelper dBhelper = new DBhelper(context);
        String ip = dBhelper.getActiveIp();
        Log.d("simul", ip);
        if(!ip.equals("0")){
            try {

                Socket sock = new Socket(ip, 12345);
                ostream = sock.getOutputStream();
                pwrite = new PrintWriter(ostream, true);
                flag = true;
                return flag;
            } catch (UnknownHostException uhe) {
                System.out.println("Host unknown: " + uhe.getMessage());
                flag = false;
                return flag;
            } catch (IOException ioe) {
                System.out.println("Unexpected exception: " + ioe.getMessage());
                flag = false;
                return flag;
            }
        }else {
            flag = false;
            Toast.makeText(context, "IP not found", Toast.LENGTH_LONG).show();
            return flag;
        }

    }


    public void send(String line){
                pwrite.println(line);       // sending to server
                pwrite.flush();

    }

    public void start() throws IOException {
        console = new DataInputStream(System.in);
        streamOut = new DataOutputStream(socket.getOutputStream());
    }

    public void stop() {
        try {
            if (console != null) {
                console.close();
            }
            if (streamOut != null) {
                streamOut.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ioe) {
            System.out.println("Error closing ...");
        }
    }
}
