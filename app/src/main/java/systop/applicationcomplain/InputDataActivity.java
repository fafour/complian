package systop.applicationcomplain;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class InputDataActivity extends AppCompatActivity {
    EditText txtTittle1,name1,surname1,idcode1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setBackgroundDrawable( new ColorDrawable( getResources().getColor( R.color.title_color ) ) );

        String txtIdpeople = getIntent().getStringExtra("Idpeople");


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog1 = new AlertDialog.Builder(InputDataActivity.this);
                dialog1.setTitle("ต้องการย้อนกลับไปหน้าหลักหรือไม่?");
                dialog1.setCancelable(true);
                dialog1.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                dialog1.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog1.show();
            }
        });


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        idcode1 = (EditText) findViewById(R.id.idcode1);
        txtTittle1 = (EditText) findViewById(R.id.txtTittle1);
        name1 = (EditText) findViewById(R.id.name1);
        surname1 = (EditText) findViewById(R.id.surname1);

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


    }
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("ต้องการย้อนกลับหรือไม่?");
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
    public void next(){
        if(!surname1.getText().toString().isEmpty() &&
          !txtTittle1.getText().toString().isEmpty() &&
          !name1.getText().toString().isEmpty() &&
          !idcode1.getText().toString().isEmpty()){

            String Url = localhost.url+"showDetail.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String response1 = "[{\"main\":null,\"status\":null,\"responsiblePerson\":null,\"atDay\":null,\"day\":null,\"detai\":null,\"hospitalName\":null,\"nameDoctor\":null}]";
//                            Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                            if (response.equals(response1) ){
                                AlertDialog.Builder dialogs = new AlertDialog.Builder(InputDataActivity.this);
                                dialogs.setTitle("คำเตือน");
                                dialogs.setMessage("ไม่พบข้อมูลที่ค้นหา");
                                dialogs.setCancelable(true);
                                dialogs.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                dialogs.show();
                            }else{
                                idcode1.setText("");
                                name1.setText("");
                                surname1.setText("");
                                txtTittle1.setText("");
                                Intent intent = new Intent(getApplicationContext(), ShowDataDetailActivity.class);
                                intent.putExtra("response",response);
                                startActivity(intent);
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(),"ระบบเกิดข้อผิดพลาด กรุณาตรวจสอบการเชื่อมต่อข้อมูลของท่าน", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }){
                @Override
                protected Map<String,String> getParams()throws com.android.volley.AuthFailureError{

                    String txtIdpeople = getIntent().getStringExtra("Idpeople");
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json; charset=utf-8");
                    params.put("idpeople",txtIdpeople);
                    params.put("tittleName",txtTittle1.getText().toString());
                    params.put("name",name1.getText().toString());
                    params.put("surName", surname1.getText().toString());
                    params.put("idCode", idcode1.getText().toString());
                    return params;
                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);

        }else{

            if (txtTittle1.getText().toString().isEmpty()) {
                txtTittle1.setError("กรุณากรอกคำขึ้นต้นชื่อ");
            }else {
                txtTittle1.setError(null);
            }
            if (name1.getText().toString().isEmpty()) {
                name1.setError("กรุณากรอกชื่อ");
            }else {
                name1.setError(null);
            }
            if (surname1.getText().toString().isEmpty()) {
                surname1.setError("กรุณากรอกนามสกุล");
            }else {
                surname1.setError(null);
            }
            if (idcode1.getText().toString().isEmpty()) {
                idcode1.setError("กรุณากรอกรหัส");
            }else {
                idcode1.setError(null);
            }
        }
    }
    public void yes(View view){
        next();
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

}
