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
 * Created by Fafour on 29/11/2559.
 */
public class AdapterHistory extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    Context context;
    private LayoutInflater inflater;
    List<history> data= Collections.emptyList();
    private history current = new history();
    int currentPos=0;
    String data1;

    String image_data ="";
    // create constructor to innitilize context and data sent from MainActivity
    public AdapterHistory(Context context, List<history> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.adapter_history, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MyHolder myHolder= (MyHolder) holder;
        current=data.get(position);
        myHolder.textUsername.setText("ชื่อผู้ใช้งาน : " +current.usernameLoging);
        myHolder.textDate.setText("เวลาเข้าใช้งาน : " + current.timeDate);
        myHolder.txtNum.setText((position+1)+".");

    }
    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }




    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textUsername;
        TextView textDate;
        TextView txtNum;



        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textUsername= (TextView) itemView.findViewById(R.id.textIdUser);
            textDate = (TextView) itemView.findViewById(R.id.textDate);
            txtNum = (TextView) itemView.findViewById(R.id.txtNum);

        }

        @Override
        public void onClick(View v) {

        }

    }



}