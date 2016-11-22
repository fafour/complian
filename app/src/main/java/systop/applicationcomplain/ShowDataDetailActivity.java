package systop.applicationcomplain;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ShowDataDetailActivity extends AppCompatActivity {
    TextView txtData,txtData1,txtData2,txtData3,txtData4,txtData5,txtData6,txtData7 ;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String txtResponsiblePerson ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });


        txtData = (TextView) findViewById(R.id.txtData);
        txtData1 = (TextView) findViewById(R.id.txtData1);
        txtData2 = (TextView) findViewById(R.id.txtData2);
        txtData3 = (TextView) findViewById(R.id.txtData3);
        txtData4 = (TextView) findViewById(R.id.txtData4);
        txtData5 = (TextView) findViewById(R.id.txtData5);
        txtData6 = (TextView) findViewById(R.id.txtData6);
        txtData7 = (TextView) findViewById(R.id.txtData7);

        String response = getIntent().getStringExtra("response");

        String mainData="",status="",responsiblePerson="",atDay="",day="",detai="",hospitalName="",nameDoctor="";
        try {
            JSONArray jsonarray = new JSONArray(response);
            JSONObject jsonResponse = jsonarray.getJSONObject(0);
            mainData= jsonResponse.getString("main");
            status = jsonResponse.getString("status");
            responsiblePerson = jsonResponse.getString("responsiblePerson");
            atDay = jsonResponse.getString("atDay");
            day = jsonResponse.getString("day");
            detai = jsonResponse.getString("detai");
            hospitalName = jsonResponse.getString("hospitalName");
            nameDoctor = jsonResponse.getString("nameDoctor");


        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "ไม่สามารถทำงานได้ ", Toast.LENGTH_LONG).show();
        }
        txtData.setText(mainData);
        txtData1.setText(status);
        //txtData2.setText(responsiblePerson);
        txtResponsiblePerson = responsiblePerson;
        txtData3.setText(atDay);
        txtData4.setText(day);
        txtData5.setText(detai);
        txtData6.setText(hospitalName);
        txtData7.setText(nameDoctor);

        new AsyncCheck().execute();


    }
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("ต้องการย้อนกลับหรือไม่?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        dialog.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
    private class AsyncCheck extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL(localhost.url+"getName.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput to true as we send and recieve data
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // add parameter to our above url
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("searchQuery", txtResponsiblePerson);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {
            try {
                String txtResponsiblePerson1 = "ไม่มีผู้รับผิดชอบ";
                if(txtResponsiblePerson.equals(txtResponsiblePerson1)){
                    txtData2.setText(txtResponsiblePerson1);
                }else {
                    JSONArray jArray = new JSONArray(result);
                    JSONObject json_data = jArray.getJSONObject(0);
                    txtData2.setText(json_data.getString("NameUser") + "  " + json_data.getString("SurNameUser"));
                }



            } catch (JSONException e) {

            }
        }

    }
}
