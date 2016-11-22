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
 * Created by Fafour on 23/10/2559.
 */
public class AdapterEmployeeAll1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    Context context;
    private LayoutInflater inflater;
    List<DataComplain> data= Collections.emptyList();
    private DataComplain current = new DataComplain();
    int currentPos=0;
    String data1;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterEmployeeAll1(Context context, List<DataComplain> data){
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
            final int position3 = getAdapterPosition();
            current=data.get(position3);
            Intent intent3 = new Intent(context, ResultSearch1Activity.class);
            intent3.putExtra("User",current.IdCode);
            context.startActivity(intent3);
            ((Activity)context).finish();
        }

    }

}