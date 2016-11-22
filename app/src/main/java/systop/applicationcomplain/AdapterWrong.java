package systop.applicationcomplain;

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
 * Created by Fafour on 24/10/2559.
 */
public class AdapterWrong extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    Context context;
    private LayoutInflater inflater;
    List<DataComplain> data= Collections.emptyList();
    private DataComplain current = new DataComplain();
    int currentPos=0;
    String data1;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterWrong(Context context, List<DataComplain> data){
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
        myHolder.textResponsiblePerson.setText("ผู้รับผิดชอบเรื่อง : " + current.ResponsiblePerson);
        myHolder.textIdcode.setText("เลขที่รับเรื่อง : " + current.IdCode);
        myHolder.textStatus.setText("สถานะ : " + current.Status );
        myHolder.textStatus.setTextColor(ContextCompat.getColor(context, R.color.dot_dark_screen2));
        myHolder.txtNum.setText((position+1)+".");
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
            final int position1 = getAdapterPosition();
            final AlertDialog.Builder adb=new AlertDialog.Builder(context);
            adb.setTitle("เลือกรายการ");
            final CharSequence[] items = { "ดูรายละเอียด","แก้ไขข้อมูล","แก้ไขสถานะ","แกไขผู้รับผิดชอบ","ลบข้อมูล"};
            adb.setItems(items, new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            current=data.get(position1);
                            Intent intent31 = new Intent(context, ScrollingDataActivity.class);
                            intent31.putExtra("IdPeople",current.IdPeople);
                            intent31.putExtra("TittleName",current.TittleName);
                            intent31.putExtra("Name",current.Name);
                            intent31.putExtra("SurName",current.SurName);
                            intent31.putExtra("Relationship",current.Relationship);
                            intent31.putExtra("Phone",current.Phone);
                            intent31.putExtra("Email",current.Email);
                            intent31.putExtra("Adress",current.Adress);
                            intent31.putExtra("PhoneHome",current.PhoneHome);
                            intent31.putExtra("DocterName",current.DocterName);
                            intent31.putExtra("HospitalName",current.HospitalName);
                            intent31.putExtra("Detail",current.Detail);
                            intent31.putExtra("Day",current.Day);
                            intent31.putExtra("AtDay",current.AtDay);
                            intent31.putExtra("IdCode",current.IdCode);
                            intent31.putExtra("ResponsiblePerson",current.ResponsiblePerson);
                            intent31.putExtra("Main",current.Main);
                            intent31.putExtra("Status",current.Status);
                            intent31.putExtra("RecipientComplaints",current.RecipientComplaints);
                            context.startActivity(intent31);
                            break;
                        case 1:
                            current=data.get(position1);
                            Intent intent3 = new Intent(context, ScrollingUpdateEmployeeAddDataActivity.class);
                            intent3.putExtra("IdPeople",current.IdPeople);
                            intent3.putExtra("TittleName",current.TittleName);
                            intent3.putExtra("Name",current.Name);
                            intent3.putExtra("SurName",current.SurName);
                            intent3.putExtra("Relationship",current.Relationship);
                            intent3.putExtra("Phone",current.Phone);
                            intent3.putExtra("Email",current.Email);
                            intent3.putExtra("Adress",current.Adress);
                            intent3.putExtra("PhoneHome",current.PhoneHome);
                            intent3.putExtra("DocterName",current.DocterName);
                            intent3.putExtra("HospitalName",current.HospitalName);
                            intent3.putExtra("Detail",current.Detail);
                            intent3.putExtra("Day",current.Day);
                            intent3.putExtra("AtDay",current.AtDay);
                            intent3.putExtra("IdCode",current.IdCode);
                            intent3.putExtra("ResponsiblePerson",current.ResponsiblePerson);
                            intent3.putExtra("Main",current.Main);
                            intent3.putExtra("Status",current.Status);
                            intent3.putExtra("RecipientComplaints",current.RecipientComplaints);
                            context.startActivity(intent3);
                            break;
                        case 2:
                            current=data.get(position1);
                            Intent intent = new Intent(context, UpdateStatusActivity.class);
                            intent.putExtra("User",current.IdCode);
                            context.startActivity(intent);
                            break;
                        case 3:
                            current=data.get(position1);
                            Intent intent2 = new Intent(context, UpdateWorkActivity.class);
                            intent2.putExtra("User",current.IdCode);
                            context.startActivity(intent2);
                            break;
                        case 4:
                            current=data.get(position1);
                            new AsyncFetch1().execute(current.IdCode);
                            data.remove(position1);
                            notifyItemRemoved(position1);
                            notifyItemRangeChanged(position1, data.size());
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
                url = new URL(localhost.url+"deleteData.php");

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

}