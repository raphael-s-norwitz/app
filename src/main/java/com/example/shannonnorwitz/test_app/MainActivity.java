package com.example.shannonnorwitz.test_app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.net.Uri;
import android.widget.MediaController;
import android.widget.VideoView;

// import network utilities
import java.net.*;
import java.io.*;

public class MainActivity extends AppCompatActivity {

    public String server_addr;
    public int broadcast_port;
    public String local_addr;
    public TextView txtView;

    public VideoView vidView;
    public String vidAddress;
    public Uri vidUri;
    public MediaController vidControl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        System.out.println("In OnCreate");


        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        @SuppressWarnings("deprecation")
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        local_addr = ip;

        txtView=(TextView)findViewById(R.id.txtView);
        txtView.setText(local_addr);

        server_addr = "";
        broadcast_port = 0;

        vidView = (VideoView)findViewById(R.id.myVideo);
        vidControl = new MediaController(this);




       FloatingActionButton test = (FloatingActionButton) findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "do something", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                String n_val = "";
                n_val = ((EditText) findViewById(R.id.txtHost)).getText().toString();
                //String port_str  = ((EditText) findViewById(R.id.txtPort)).getText().toString();
                int port = Integer.parseInt(((EditText) findViewById(R.id.txtPort)).getText().toString());
                System.out.println(n_val);
                System.out.println(port);
                System.out.println(local_addr);

                server_addr = n_val;
                broadcast_port = port;

                vidAddress = "http://" +server_addr +":" + Integer.toString(broadcast_port) +"/non_existant.mp4";
                //amuse.wimnet.ee.columbia.edu:8088/mp4/Football/Output_Video/1-10.mp4";
                vidUri = Uri.parse(vidAddress);
                vidView.setVideoURI(vidUri);
                vidControl.setAnchorView(vidView);
                vidView.setMediaController(vidControl);
                vidView.start();

                /*thread_this th = new thread_this("test_thread", server_addr, broadcast_port);
                th.start();*/



            }
        });


        //run_sock();

        System.out.println("Before end OnCreate");

    }

    public void run_sock(String serverName, int port)
    {

        System.out.println("before try");
        try
        {
            System.out.println("Connecting to " + serverName +
                    " on port " + port);
            Socket client = new Socket(serverName, port);
            System .out.println("Just connected to "
                    + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream ();
            DataOutputStream out = new DataOutputStream (outToServer);
            out.writeUTF("Hello from "
                    + client.getLocalSocketAddress());
            InputStream inFromServer = client.getInputStream ();
            DataInputStream in =
                    new DataInputStream (inFromServer);
            System .out.println("Server says " + in.readUTF());
            client.close();
        }catch(IOException e)
        {
            System.out.println("here");
            e.printStackTrace();
        }

    }

    class thread_this implements Runnable{
        private Thread t;
        private String threadname;
        private String servername;
        private int port;
        thread_this( String name, String server, int port_b){
            threadname = name;
            servername = server;
            port = port_b;

        }
        public void run(){
            run_sock(servername, port);
        }

        public void start()
        {
            t = new Thread(this, threadname);
            t.start();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
