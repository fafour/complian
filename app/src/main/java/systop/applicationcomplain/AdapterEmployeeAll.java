package systop.applicationcomplain;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import java.util.Collections;
import java.util.List;

/**
 * Created by Fafour on 23/10/2559.
 */
public class AdapterEmployeeAll extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    Context context;
    private LayoutInflater inflater;
    List<DataComplain> data= Collections.emptyList();
    private DataComplain current = new DataComplain();
    int currentPos=0;
    String data1;
    String getName;
    String getStatus;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterEmployeeAll(Context context, List<DataComplain> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.adapter_employee, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder= (MyHolder) holder;
        current=data.get(position);
        myHolder.textMain.setText("หัวข้อ : " +current.Main);
        myHolder.textIdcode.setText("เลขที่รับเรื่อง : " + current.IdCode);
        myHolder.txtNum.setText((position+1)+".");

        getDataName task = new getDataName(holder);
        task.execute(current.ResponsiblePerson);

        getDataStatus tasks = new getDataStatus(holder);
        tasks.execute(current.Status);

    }
    public class getDataName extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        private final RecyclerView.ViewHolder holder;

        public getDataName(RecyclerView.ViewHolder holder) {
            this.holder = holder;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL(localhost.url + "getName.php");

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
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("searchQuery", params[0]);
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
                getName = "ไม่มีผู้รับผิดชอบ";
                MyHolder myHolder= (MyHolder) holder;
                myHolder.textResponsiblePerson.setText("ผู้รับผิดชอบเรื่อง : " +getName);
            }else {
                try {
                    JSONArray jArray = new JSONArray(result);
                    JSONObject json_data = jArray.getJSONObject(0);
                    getName = (json_data.getString("NameUser") + "  " + json_data.getString("SurNameUser"));
                    MyHolder myHolder= (MyHolder) holder;
                    myHolder.textResponsiblePerson.setText("ผู้รับผิดชอบเรื่อง : " +getName);

                } catch (JSONException e) {

                }
            }
        }

    }

    public class getDataStatus extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        private final RecyclerView.ViewHolder holder;

        public getDataStatus(RecyclerView.ViewHolder holder) {
            this.holder = holder;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL(localhost.url + "getStatus.php");

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
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("searchQuery", params[0]);
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
                JSONArray jArray = new JSONArray(result);
                JSONObject json_data = jArray.getJSONObject(0);
                getStatus = (json_data.getString("st_title"));
                MyHolder myHolder= (MyHolder) holder;
                myHolder.textStatus.setText("สถานะ : " + getStatus );
                myHolder.textStatus.setTextColor(ContextCompat.getColor(context, R.color.dot_dark_screen2));
            } catch (JSONException e) {

            }

        }

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textMain;
        TextView textResponsiblePerson;
        TextView textIdcode;
        TextView textStatus;
        TextView txtNum;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textMain= (TextView) itemView.findViewById(R.id.textMain);
            textResponsiblePerson = (TextView) itemView.findViewById(R.id.textResponsiblePerson);
            textIdcode = (TextView) itemView.findViewById(R.id.textIdcode);
            textStatus = (TextView) itemView.findViewById(R.id.textStatus);
            txtNum = (TextView) itemView.findViewById(R.id.txtNum);
        }

        @Override
        public void onClick(View v) {
            final int position3 = getAdapterPosition();
            current=data.get(position3);
            Intent intent3 = new Intent(context, ResultSearchActivity.class);
            intent3.putExtra("User",current.IdCode);
            context.startActivity(intent3);
            ((Activity)context).finish();
        }

    }

}