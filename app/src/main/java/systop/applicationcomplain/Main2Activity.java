package systop.applicationcomplain;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {
//    TextView txtMainApp;
//    Button btn1,btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

//        txtMainApp = (TextView) findViewById(R.id.txtMainApp);
//        btn1 = (Button) findViewById(R.id.btn1);
//        btn2 = (Button) findViewById(R.id.btn2);
//
//        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/th.ttf");
//        txtMainApp.setTypeface(face);
//        btn1.setTypeface(face);
//        btn2.setTypeface(face);





        clearCache.deleteCache(this);

        if (!isNetworkConnected() && !isWifiConnected() ) {

            new AlertDialog.Builder(this)
                    .setTitle("ไม่มีการเชื่อมต่อข้อมูล")
                    .setMessage("กรุณาตรวจสอบอินเทอร์เน็ต")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), ClearActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();

        }

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


    }

    public  void  Click1(View view){
        Intent intent = new Intent(getApplicationContext(), InputIdPeopleActivity.class);
        startActivity(intent);
    }
    public  void  Click2(View view){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("ต้องการออกจากโปรแกรมหรือไม่?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        dialog.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
    private boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE); // 1
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo(); // 2
        return networkInfo != null && networkInfo.isConnected(); // 3
    }
    private boolean isWifiConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) && networkInfo.isConnected();
    }


}
