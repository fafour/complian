package systop.applicationcomplain;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
import java.util.HashMap;
import java.util.Map;

public class FinishActivity extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String dataComID ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Toast.makeText(getApplicationContext(),"ทำรายการเสร็จสิ้น",Toast.LENGTH_LONG).show();
        new AsyncCheck2().execute();
        finish();

    }
    private class AsyncCheck2 extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;
        String IdCode = getIntent().getStringExtra("IdCode");

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL(localhost.url + "getComID.php");

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
                conn.setChunkedStreamingMode(1024);
                // setDoInput and setDoOutput to true as we send and recieve data
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // add parameter to our above url
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("searchQuery", IdCode);
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
            if(result.equals("no rows")){
                dataComID = ("");
            }else {
                try {
                    JSONArray jArray = new JSONArray(result);
                    JSONObject json_data = jArray.getJSONObject(0);
                    dataComID = (json_data.getString("com_id"));

                    Toast.makeText(getApplicationContext(),dataComID,Toast.LENGTH_LONG).show();

                    String DocterName = getIntent().getStringExtra("DoctorName");
                    String[] parts = DocterName.trim().split("\\r?\\n");

                    if(parts.length-1 == 0){
                        String a = parts[0];
                        final String[] b = a.split(" : ");
                        String dataDoctor = b[1];
                        final String[] dataDoc = dataDoctor.split("  ");
                        String Url3 = localhost.url+"insertAddDoctorWeb1.php";
                        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, Url3,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }){
                            @Override
                            protected Map<String,String> getParams()throws com.android.volley.AuthFailureError{

                                Map<String,String> params = new HashMap<String, String>();
                                params.put("Content-Type", "application/json; charset=utf-8");
                                params.put("doc_title",dataDoc[0]);
                                params.put("doc_name",dataDoc[1]);
                                params.put("doc_lname",dataDoc[2]);
                                params.put("doc_code",dataDoc[3]);
                                params.put("FK_com_id",dataComID);
                                return params;
                            }

                        };
                        RequestQueue requestQueue3 = Volley.newRequestQueue(getApplicationContext());
                        requestQueue3.add(stringRequest3);


                    }else{
                        for(int i = 0 ; i< parts.length;i++) {
                            String a = parts[i];
                            String[] b = a.split(" : ");
                            String dataDoctor = b[1];
                            final String[] dataDoc = dataDoctor.split("  ");
                            String Url3 = localhost.url+"insertAddDoctorWeb1.php";
                            StringRequest stringRequest3 = new StringRequest(Request.Method.POST, Url3,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    }){
                                @Override
                                protected Map<String,String> getParams()throws com.android.volley.AuthFailureError{

                                    Map<String,String> params = new HashMap<String, String>();
                                    params.put("Content-Type", "application/json; charset=utf-8");
                                    params.put("doc_title",dataDoc[0]);
                                    params.put("doc_name",dataDoc[1]);
                                    params.put("doc_lname",dataDoc[2]);
                                    params.put("doc_code",dataDoc[3]);
                                    params.put("FK_com_id",dataComID);
                                    return params;
                                }

                            };
                            RequestQueue requestQueue3 = Volley.newRequestQueue(getApplicationContext());
                            requestQueue3.add(stringRequest3);

                        }
                    }


                } catch (JSONException e) {

                }
            }
        }

    }

}
