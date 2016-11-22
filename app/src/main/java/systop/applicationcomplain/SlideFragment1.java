package systop.applicationcomplain;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SlideFragment1 extends android.support.v4.app.Fragment {
    int pos;
    Context context;

    public SlideFragment1(int position){
        this.pos=position;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_slide1, container, false);

        TextView cpage=(TextView)rootView.findViewById(R.id.txt1);

        switch (pos) {
            case 0:
                cpage.setText("เลื่อนไปทางขวา...");
                break;
            case 1:
                cpage.setText("กรุณารอสักครู่...");
                break;
            case 2:
                cpage.setText("กรุณารอสักครู่...");
                next();
                break;


        }

        return rootView;
    }
    public void next(){
        final Intent intent = new Intent(getActivity(), Success1Activity.class);
        intent.putExtra("IdCode",ScreenSlidePager1Activity.id);
        startActivity(intent);
        getActivity().finish();

    }
}
