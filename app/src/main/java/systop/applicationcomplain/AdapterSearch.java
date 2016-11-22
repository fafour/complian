package systop.applicationcomplain;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Fafour on 20/10/2559.
 */
public class AdapterSearch extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<DataSearch> data= Collections.emptyList();
    DataSearch current;

    // create constructor to initialize context and data sent from MainActivity
    public AdapterSearch(Context context, List<DataSearch> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when ViewHolder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.adapter_search, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in RecyclerView to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        DataSearch current=data.get(position);
        myHolder.textName.setText("ชื่อหมอ : "+current.nameDoc);
        myHolder.textSurName.setText("นามสกุล : " + current.snameDoc);
        myHolder.textHospital.setText("โรงพยาบาล : " + current.hospitalDoc);
        myHolder.textID.setText("เลขที่ใบประกอบวิชาชีพเวชกรรม " + current.idcodeDoc );
        myHolder.textID.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        myHolder.txtNum.setText((position+1)+".");

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textName;
        TextView textSurName;
        TextView textID;
        TextView textHospital;
        TextView txtNum;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textName= (TextView) itemView.findViewById(R.id.textName);
            textSurName = (TextView) itemView.findViewById(R.id.textSurName);
            textID = (TextView) itemView.findViewById(R.id.textID);
            textHospital = (TextView) itemView.findViewById(R.id.textHospital);
            txtNum = (TextView) itemView.findViewById(R.id.txtNum);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        // Click event for all items


    }

}
