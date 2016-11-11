package com.example.root.slidecontroller;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

    @BindView(R.id.pptView)
    ImageView pptView;
    @BindView(R.id.nextButton)
    Button nextBtn;
    @BindView(R.id.previousButton)
    Button previousBtn;
    @BindView(R.id.connectButton)
    Button connectBtn;
    @BindView(R.id.closeButton)
    Button closeBtn;
    @BindView(R.id.exitButton)
    Button exitBtn;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    boolean isConnected;
    private SlideControllerClient client = null;
    private String receiveMessage;
    private InputStream istream;
    private int[] viewCoords;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        viewCoords = new int[2];
        pptView.getLocationOnScreen(viewCoords);






        nextBtn.setVisibility(View.INVISIBLE);
        previousBtn.setVisibility(View.INVISIBLE);
        closeBtn.setVisibility(View.INVISIBLE);
        exitBtn.setVisibility(View.INVISIBLE);
        nextBtn.setOnClickListener(this);
        previousBtn.setOnClickListener(this);
        connectBtn.setOnClickListener(this);
        closeBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);

        client = new SlideControllerClient();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int touchX = (int) event.getX();
        int touchY = (int) event.getY();

        int imageX = touchX - viewCoords[0]; // viewCoords[0] is the X coordinate
        int imageY = touchY - viewCoords[1]; // viewCoords[1] is the y coordinate

        Log.d("simul", "x = " + imageX);
        Log.d("simul", "y = " + imageY);

        client.send(".move "+imageX+" "+imageY);

        return super.onTouchEvent(event);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent settingsIntent = new Intent(MainActivity.this, IpSettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.connectButton) {
            new LongOperation().execute("");
        } else if (v.getId() == R.id.nextButton) {
            pptView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            client.send(".next");
        } else if (v.getId() == R.id.previousButton) {
            pptView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            client.send(".previous");
        } else if (v.getId() == R.id.closeButton) {
            Log.d("simul", ".close");
            client.send(".close");
        } else if (v.getId() == R.id.exitButton) {
            client.send(".exit");
            this.recreate();
        }
    }

    public void setButton() {
        if (isConnected == true) {
            nextBtn.setVisibility(View.VISIBLE);
            previousBtn.setVisibility(View.VISIBLE);
            closeBtn.setVisibility(View.VISIBLE);
            exitBtn.setVisibility(View.VISIBLE);
            connectBtn.setClickable(false);
            connectBtn.setText("Connected");
        } else {
            Toast.makeText(this, "Unexpected Error, Desktop application may not running, try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("");
        builder.setMessage("Do you want to exit?");

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isConnected == true) {
                            client.send(".exit");
                        }
                        MainActivity.this.finish();
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }


    private class LongOperation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            DBhelper dBhelper = new DBhelper(getApplicationContext());
            String ip = dBhelper.getActiveIp();
            Log.d("simul", ip);
            if (!ip.equals("0")) {
                isConnected = client.connect(getApplicationContext(), ip, Integer.parseInt("12345"));
                Log.d("simul", "" + isConnected);
            }

            if (isConnected) {
                new Thread() {
                    public void run() {
                        try {
                            String imageString = "";
                            istream = client.getSocket().getInputStream();
                            BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));

                            while (true) {
                                if ((receiveMessage = receiveRead.readLine()) != null) {
                                    System.out.println(receiveMessage);
                                    if (receiveMessage.equals(".exit")) {
                                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                                        MainActivity.this.finish();
                                    } else {

                                        String substring = receiveMessage.substring(Math.max(receiveMessage.length() - 3, 0));
                                        if(!substring.equals("%%%")){
                                            imageString+=receiveMessage;
                                        }else {
                                            imageString+=receiveMessage;
                                            System.out.println(imageString);
                                            try {
                                                byte [] encodeByte=Base64.decode(imageString,Base64.DEFAULT);
                                                final Bitmap bmp=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                                                imageString = "";
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        pptView.setImageBitmap(bmp);
                                                        pptView.setVisibility(View.VISIBLE);
                                                        progressBar.setVisibility(View.INVISIBLE);
                                                    }
                                                });
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            setButton();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


}
