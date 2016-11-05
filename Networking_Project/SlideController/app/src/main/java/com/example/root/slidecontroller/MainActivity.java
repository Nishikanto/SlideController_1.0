package com.example.root.slidecontroller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

    @BindView(R.id.nextButton) Button nextBtn;
    @BindView(R.id.previousButton) Button previousBtn;
    @BindView(R.id.connectButton) Button connectBtn;
    @BindView(R.id.closeButton) Button closeBtn;
    @BindView(R.id.exitButton) Button exitBtn;
    boolean isConnected;
    private SlideControllerClient client = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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
        if(v.getId() == R.id.connectButton){
            new LongOperation().execute("");
        }
        else if(v.getId() == R.id.nextButton){
            client.send(".next");
        }else if(v.getId() == R.id.previousButton){
            client.send(".previous");
        }else if(v.getId() == R.id.closeButton){
            Log.d("simul", ".close");
            client.send(".close");
        }else if(v.getId() == R.id.exitButton){
            client.send(".exit");
            this.recreate();
        }
    }

    public void setButton(){
        if(isConnected == true){
            nextBtn.setVisibility(View.VISIBLE);
            previousBtn.setVisibility(View.VISIBLE);
            closeBtn.setVisibility(View.VISIBLE);
            exitBtn.setVisibility(View.VISIBLE);
            connectBtn.setClickable(false);
            connectBtn.setText("Connected");
        }else{
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
                        if(isConnected==true){
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
            if(!ip.equals("0")){
                isConnected = client.connect(getApplicationContext(), ip, Integer.parseInt("12345"));
                Log.d("simul", ""+isConnected);
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
