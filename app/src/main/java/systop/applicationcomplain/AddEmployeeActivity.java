package systop.applicationcomplain;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddEmployeeActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener {
    EditText idUsername,idPass,idPassAgain,idNameEmp,idSurEmp;
    private Spinner spinner;
    String detailLevel="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        idUsername = (EditText) findViewById(R.id.idUsername);
        idPass = (EditText) findViewById(R.id.idPass);
        idPassAgain = (EditText) findViewById(R.id.idPassAgain);
        idNameEmp = (EditText) findViewById(R.id.idNameEmp);
        idSurEmp = (EditText) findViewById(R.id.idSurEmp);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        List dataLevel = new ArrayList();
        dataLevel.add("เจ้าหน้าที่");
        dataLevel.add("Admin");
        ArrayAdapter dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dataLevel);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        detailLevel = parent.getItemAtPosition(position).toString();
        if(detailLevel.equals("เจ้าหน้าที่")){
            detailLevel = "1";
        }else  if(detailLevel.equals("Admin")){
            detailLevel = "2";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void click1(View view){
        final String user = idUsername.getText().toString();
        final String nameUser = idNameEmp.getText().toString();
        final String sname = idSurEmp.getText().toString();
        final String pass = idPass.getText().toString();
        final String aPass = idPassAgain.getText().toString();

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("ต้องการทำรายการถัดไปแล้วหรือไม่?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (!user.isEmpty() &&
                    !nameUser.isEmpty() &&
                    !sname.isEmpty() &&
                    !pass.isEmpty() &&
                    !aPass.isEmpty() && pass.equals(aPass)
                        ){
                    String Url = localhost.url+"InsertEmployee.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(getApplicationContext(),"ทำรายการเสร็จสิ้น",Toast.LENGTH_LONG).show();
                                    finish();

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    AlertDialog.Builder dialogs = new AlertDialog.Builder(AddEmployeeActivity.this);
                                    dialogs.setTitle("คำเตือน");
                                    dialogs.setMessage("ระบบเกิดข้อผิดพลาด กรุณาตรวจสอบการเชื่อมต่อข้อมูลของท่าน");
                                    dialogs.setCancelable(true);
                                    dialogs.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialogs.show();
                                }
                            }){
                        @Override
                        protected Map<String,String> getParams()throws com.android.volley.AuthFailureError{


                            Map<String,String> params = new HashMap<String, String>();
                            params.put("Content-Type", "application/json; charset=utf-8");
                            params.put("Username",user);
                            params.put("NameUser",nameUser);
                            params.put("SurNameUser",sname);
                            params.put("Password", pass);
                            params.put("Level", detailLevel);
                            params.put("Status", "1");
                            return params;
                        }

                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);

                }else {

                    if (user.isEmpty()) {
                        idUsername.setError("กรุณากรอกชื่อผู้ใช้งาน");
                    }else {
                        idUsername.setError(null);
                    }

                    if (nameUser.isEmpty()) {
                        idNameEmp.setError("กรุณากรอกชื่อ");
                    }else {
                        idNameEmp.setError(null);
                    }

                    if (sname.isEmpty()) {
                        idSurEmp.setError("กรุณากรอกนามสกุล");
                    }else {
                        idSurEmp.setError(null);
                    }

                    if (pass.isEmpty()) {
                        idPass.setError("กรุณากรอกนามรหัส");
                    }else {
                        idPass.setError(null);
                    }

                    if (aPass.isEmpty() || !pass.equals(aPass))  {
                        idPassAgain.setError("กรุณาตรวสอบความถูกต้องของรหัสด้วย");
                    }else {
                        idPassAgain.setError(null);
                    }



                }
            }
        });

        dialog.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
}
