package systop.applicationcomplain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Fafour on 21/10/2559.
 */
public class AdapterManage1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<DataEmployee> data= Collections.emptyList();
    private DataEmployee current = new DataEmployee();
    int currentPos=0;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterManage1(Context context, List<DataEmployee> data){
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
            final int position3 = getAdapterPosition();
            current=data.get(position3);
            Intent intent3 = new Intent(context, Manage1Activity.class);
            intent3.putExtra("User",current.UsernameEmp);
            context.startActivity(intent3);
            ((Activity)context).finish();
        }
    }


}
