package systop.applicationcomplain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Fafour on 19/10/2559.
 */
public class SlideFragment extends android.support.v4.app.Fragment {
    int pos;
    Context context;

    public SlideFragment(int position){
        this.pos=position;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_slide, container, false);

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
                getActivity().finish();
                break;


        }

        return rootView;
    }
    public void next(){
        final Intent intent = new Intent(getActivity(), IndexActivity.class);
        startActivity(intent);
        getActivity().finish();

    }
}
