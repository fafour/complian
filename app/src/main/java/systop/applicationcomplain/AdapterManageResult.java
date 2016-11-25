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
import android.widget.Toast;

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
public class AdapterManageResult extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    Context context;
    private LayoutInflater inflater;
    List<DataEmployee> data= Collections.emptyList();
    private DataEmployee current = new DataEmployee();
    int currentPos=0;
    String data1;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterManageResult(Context context, List<DataEmployee> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.adapter_manage, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder= (MyHolder) holder;
        DataEmployee current=data.get(position);
        myHolder.textUsrNameEmp.setText("ชื่อผู้ใช้งาน : " +current.UsernameEmp);
        myHolder.textNameEmp.setText("ชื่อ : " + current.FristName);
        myHolder.textSurNameEmp.setText("นามสกุล : " + current.SurName);
        myHolder.txtNum.setText((position+1)+".");

        String status = "";
        String level  = "";
        if (current.StatusEmp.equals("1")){
            status = "สามารถใช้งานได้";
            myHolder.textStatusEmp.setText("สถานะ : " + status );
            myHolder.textStatusEmp.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }
        else if (current.StatusEmp.equals("0")){
            status = "ไม่สามารถใช้งานได้";
            myHolder.textStatusEmp.setText("สถานะ : " + status );
            myHolder.textStatusEmp.setTextColor(ContextCompat.getColor(context, R.color.bg_screen4));
        }
        if (current.LevelEmp.equals("1")){
            level = "เจ้าหน้าที่";
            myHolder.textLevelEmp.setText("สถานะ : " + level );

        }
        else if (current.LevelEmp.equals("2")){
            level = "Admin";
            myHolder.textLevelEmp.setText("สถานะ : " + level );

        }

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textUsrNameEmp;
        TextView textNameEmp;
        TextView textSurNameEmp;
        TextView textStatusEmp;
        TextView textLevelEmp;
        TextView txtNum;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textUsrNameEmp= (TextView) itemView.findViewById(R.id.textUsrNameEmp);
            textNameEmp = (TextView) itemView.findViewById(R.id.textNameEmp);
            textSurNameEmp = (TextView) itemView.findViewById(R.id.textSurNameEmp);
            textStatusEmp = (TextView) itemView.findViewById(R.id.textStatusEmp);
            textLevelEmp = (TextView) itemView.findViewById(R.id.textLevelEmp);
            txtNum = (TextView) itemView.findViewById(R.id.txtNum);
        }

        @Override
        public void onClick(View v) {
            final int position1 = getAdapterPosition();
            final AlertDialog.Builder adb=new AlertDialog.Builder(context);
            adb.setTitle("เลือกรายการ");
            final CharSequence[] items = { "ลบข้อมูล","แก้ไขข้อมูล","แก้ไขสถานะ","แก้ไขระดับ"};
            adb.setItems(items, new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            current=data.get(position1);
                            new AsyncFetch2().execute(current.UsernameEmp, String.valueOf(position1));
                            break;
                        case 1:
                            current=data.get(0);
                            Intent intent3 = new Intent(context, UpdateNameActivity.class);
                            intent3.putExtra("User",current.UsernameEmp);
                            intent3.putExtra("name",current.FristName);
                            intent3.putExtra("sname",current.SurName);
                            context.startActivity(intent3);
                            break;
                        case 2:
                            current=data.get(0);
                            Intent intent = new Intent(context, UpdateStatusEmployeeActivity.class);
                            intent.putExtra("User",current.UsernameEmp);
                            context.startActivity(intent);
                            break;
                        case 3:
                            current=data.get(0);
                            Intent intent2 = new Intent(context, UpdateLevelEmployeeActivity.class);
                            intent2.putExtra("User",current.UsernameEmp);
                            context.startActivity(intent2);
                            break;
                        default:
                            break;
                    }
                }
            });
            adb.create();
            adb.show();
        }

    }
    private class AsyncFetch1 extends AsyncTask<String, String, String> {
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
                url = new URL(localhost.url+"deleteResponsible.php");

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

                conn.setDoInput(true);
                conn.setDoOutput(true);


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

        }

    }
    private class AsyncFetch2 extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;
        String name ="";
        String positionData = "" ;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL(localhost.url+"employeeShowData1.php");

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

                conn.setDoInput(true);
                conn.setDoOutput(true);


                Uri.Builder builder = new Uri.Builder().appendQueryParameter("searchQuery", params[0]);
                String query = builder.build().getEncodedQuery();
                name = params[0];
                positionData = params[1];

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
                new AsyncFetch1().execute(name);

                int MyNumber = Integer.parseInt(positionData);

                data.remove(MyNumber);
                notifyItemRemoved(MyNumber);
                notifyItemRangeChanged(MyNumber, data.size());

                CharSequence text = "ทำรายการเสร็จสิ้น";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                ((Activity)context).finish();

            }else {
                AlertDialog.Builder dialogs = new AlertDialog.Builder(context);
                dialogs.setTitle("คำเตือน");
                dialogs.setMessage("ไม่สามารถลบได้ กรุณาตรวจสอบข้อมูลเจ้าหน้าที่ดูอีกรอบ");
                dialogs.setCancelable(true);
                dialogs.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialogs.show();
            }
        }

    }

}