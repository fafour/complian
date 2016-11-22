package systop.applicationcomplain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Fafour on 19/10/2559.
 */
public class AdapterAddDoctor extends ArrayAdapter<DetailDoctor> {
    public AdapterAddDoctor(Context context, ArrayList<DetailDoctor> users) {
        super(context, 0, users);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DetailDoctor detailDoctor = getItem(position);
        detailDoctor.toString();

        if(convertView == null){
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.adapter_add_doctor, parent,false);
        }

        TextView txtName = (TextView) convertView.findViewById(R.id.nameDoctor);
        TextView no = (TextView) convertView.findViewById(R.id.num);


        txtName.setText(detailDoctor.getDoctorName().toString());
        no.setText(position+1 +".");
        return convertView;


    }



}
