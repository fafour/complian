package systop.applicationcomplain;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static java.lang.Float.parseFloat;

public class ScrollingUpdateEmployeeAddDataActivity extends AppCompatActivity {
    EditText titlename,name,surname,relationship,email,phone;
    String txtIdpeople,txtTitleName,txtName,txtSurname,txtRelationship,txtEmail,txtPhone;
    EditText editText1,editText2,editText3,editText4,editText5;
    private ImageView imageView;
    String image_data ="";
    ProgressDialog progressDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_update_employee_add_data);
        clearCache.deleteCache(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setBackgroundDrawable( new ColorDrawable( getResources().getColor( R.color.title_color ) ) );

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog1 = new AlertDialog.Builder(ScrollingUpdateEmployeeAddDataActivity.this);
                dialog1.setTitle("ต้องการย้อนกลับไปหน้ารายการหรือไม่?");
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

        String IdPeople = getIntent().getStringExtra("IdPeople");
        String TittleName = getIntent().getStringExtra("TittleName");
        String Name = getIntent().getStringExtra("Name");
        String SurName = getIntent().getStringExtra("SurName");
        String Relationship = getIntent().getStringExtra("Relationship");
        String Phone = getIntent().getStringExtra("Phone");
        String Email = getIntent().getStringExtra("Email");
        String Adress = getIntent().getStringExtra("Adress");
        String PhoneHome = getIntent().getStringExtra("PhoneHome");
        String DocterName = getIntent().getStringExtra("DocterName");
        String HospitalName = getIntent().getStringExtra("HospitalName");
        String Detail = getIntent().getStringExtra("Detail");
        String Day = getIntent().getStringExtra("Day");
        String AtDay = getIntent().getStringExtra("AtDay");
        String IdCode = getIntent().getStringExtra("IdCode");
        String ResponsiblePerson = getIntent().getStringExtra("ResponsiblePerson");
        String Main = getIntent().getStringExtra("Main");
        String Status = getIntent().getStringExtra("Status");
        String RecipientComplaints = getIntent().getStringExtra("RecipientComplaints");

        titlename = (EditText) findViewById(R.id.titlename);
        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        relationship = (EditText) findViewById(R.id.relationship);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);

        titlename.setText(TittleName);
        name.setText(Name);
        surname.setText(SurName);
        relationship.setText(Relationship);
        email.setText(Email);
        phone.setText(Phone);


        txtTitleName = titlename.getText().toString();
        txtName = name.getText().toString();
        txtSurname = surname.getText().toString();
        txtRelationship = relationship.getText().toString();
        txtEmail = email.getText().toString();
        txtPhone = phone.getText().toString();

        imageView = (ImageView) findViewById(R.id.img);
        String Url = localhost.url+"pic_Get.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            JSONObject jsonResponse = jsonarray.getJSONObject(0);
                            image_data= jsonResponse.getString("image_data");

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "ไม่สามารถทำงานได้ ", Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(getApplicationContext(),image_data, Toast.LENGTH_SHORT).show();
                        if( !image_data.equals("null")) {
                            new DownloadImage().execute(image_data);
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
                String txtIdpeople = getIntent().getStringExtra("IdPeople");
                Map<String,String> params = new HashMap<String, String>();
                params.put("image_tag",txtIdpeople);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });
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
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        editText4 = (EditText) findViewById(R.id.editText4);
        editText5 = (EditText) findViewById(R.id.editText5);

        editText1.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1) {
                    editText2.requestFocus();
                }
            }
        });

        editText2.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 4) {
                    editText3.requestFocus();
                }
            }
        });

        editText3.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 5) {
                    editText4.requestFocus();
                }
            }
        });
        editText4.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 2) {
                    editText5.requestFocus();
                }
            }
        });
        String id = editText1.getText().toString()+editText2.getText().toString()+editText3.getText().toString()+editText4.getText().toString()
                +editText5.getText().toString();

        txtIdpeople = id;
        String txt = Character.toString(IdPeople.charAt(0));
        String txt1 = Character.toString(IdPeople.charAt(1));
        String txt2 = Character.toString(IdPeople.charAt(2));
        String txt3 = Character.toString(IdPeople.charAt(3));
        String txt4 = Character.toString(IdPeople.charAt(4));
        String txt5 = Character.toString(IdPeople.charAt(5));
        String txt6 = Character.toString(IdPeople.charAt(6));
        String txt7 = Character.toString(IdPeople.charAt(7));
        String txt8 = Character.toString(IdPeople.charAt(8));
        String txt9 = Character.toString(IdPeople.charAt(9));
        String txt10 = Character.toString(IdPeople.charAt(10));
        String txt11 = Character.toString(IdPeople.charAt(11));
        String txt12 = Character.toString(IdPeople.charAt(12));

        editText1.setText(txt);
        editText2.setText(txt1+txt2+txt3+txt4);
        editText3.setText(txt5+txt6+txt7+txt8+txt9);
        editText4.setText(txt10+txt11);
        editText5.setText(txt12);
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

    private void next() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("ต้องการทำรายการถัดไปแล้วหรือไม่?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                txtTitleName = titlename.getText().toString();
                txtName = name.getText().toString();
                txtSurname = surname.getText().toString();
                txtRelationship = relationship.getText().toString();
                txtEmail = email.getText().toString();
                txtPhone = phone.getText().toString();
                String emailRegex ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                int sum1 = 0;

                String id = editText1.getText().toString()+editText2.getText().toString()+editText3.getText().toString()+editText4.getText().toString()
                        +editText5.getText().toString();
                int count = editText1.length()+editText2.length()+editText3.length()+editText4.length()+editText5.length();
                txtIdpeople = id;

                if (count == 13 &&
                        !txtTitleName.isEmpty() &&
                        !txtName.isEmpty() &&
                        !txtSurname.isEmpty() &&
                        !txtPhone.isEmpty() &&
                        txtEmail.matches(emailRegex)
                        ){

                    for (int i = 0; i < 12; i++) {
                        sum1 += parseFloat(String.valueOf(id.charAt(i))) * (13 - i);
                    }

                    if ((11 - sum1 % 11) % 10 != parseFloat(String.valueOf(id.charAt(12)))){
                        AlertDialog.Builder dialogs = new AlertDialog.Builder(ScrollingUpdateEmployeeAddDataActivity.this);
                        dialogs.setTitle("คำเตือน");
                        dialogs.setMessage("เลขประจำตัวประชาชนไม่ถูกต้อง กรุณาตรวจสอบเลขประจำตัวประชาชนใหม่อีกครั้ง");
                        dialogs.setCancelable(true);
                        dialogs.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialogs.show();
                    }else {
                        String Adress = getIntent().getStringExtra("Adress");
                        String PhoneHome = getIntent().getStringExtra("PhoneHome");
                        String DocterName = getIntent().getStringExtra("DocterName");
                        String HospitalName = getIntent().getStringExtra("HospitalName");
                        String Detail = getIntent().getStringExtra("Detail");
                        String Day = getIntent().getStringExtra("Day");
                        String AtDay = getIntent().getStringExtra("AtDay");
                        String IdCode = getIntent().getStringExtra("IdCode");
                        String ResponsiblePerson = getIntent().getStringExtra("ResponsiblePerson");
                        String Main = getIntent().getStringExtra("Main");
                        String Status = getIntent().getStringExtra("Status");
                        String RecipientComplaints = getIntent().getStringExtra("RecipientComplaints");

                        Intent intent = new Intent(getApplicationContext(), ScrollingUpdateEmployeeAddAdressActivity.class);
                        intent.putExtra("Idpeople", txtIdpeople);
                        intent.putExtra("TitleName", txtTitleName);
                        intent.putExtra("Name", txtName);
                        intent.putExtra("Surname", txtSurname);
                        intent.putExtra("Relationship", txtRelationship);
                        intent.putExtra("phone", txtPhone);
                        intent.putExtra("Email", txtEmail);

                        intent.putExtra("Adress" ,Adress);
                        intent.putExtra("PhoneHome",PhoneHome);
                        intent.putExtra("DocterName",DocterName);
                        intent.putExtra("HospitalName",HospitalName);
                        intent.putExtra("Detail",Detail);
                        intent.putExtra("Day",Day);
                        intent.putExtra("AtDay",AtDay);
                        intent.putExtra("IdCode",IdCode);
                        intent.putExtra("ResponsiblePerson",ResponsiblePerson);
                        intent.putExtra("Main",Main);
                        intent.putExtra("Status",Status);
                        intent.putExtra("RecipientComplaints",RecipientComplaints);
                        startActivity(intent);
                        finish();
                    }

                }else {
                    if(count != 13 ) {
                        AlertDialog.Builder dialogs = new AlertDialog.Builder(ScrollingUpdateEmployeeAddDataActivity.this);
                        dialogs.setTitle("คำเตือน");
                        dialogs.setMessage("กรุณากรอกเลขประจำตัวประชาชนให้ถูกต้อง");
                        dialogs.setCancelable(true);
                        dialogs.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialogs.show();
                    }

                    if (txtTitleName.isEmpty()) {
                        titlename.setError("กรุณากรอกคำขึ้นต้นชื่อ");
                    }else {
                        titlename.setError(null);
                    }


                    if (txtName.isEmpty()) {
                        name.setError("กรุณากรอกชื่อ");
                    }else {
                        name.setError(null);
                    }

                    if (txtSurname.isEmpty()) {
                        surname.setError("กรุณากรอกนามสกุล");
                    }else {
                        surname.setError(null);
                    }


                    if (txtPhone.isEmpty()) {
                        phone.setError("กรุณากรอกเบอร์โทรศัพท์");
                    }else {
                        phone.setError(null);
                    }

                    if (!txtEmail.matches(emailRegex)) {
                        email.setError("กรุณากรอกอีเมล์ให้ถูกต้อง");
                    }else {
                        email.setError(null);
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
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ScrollingUpdateEmployeeAddDataActivity.this,"กำลังโหลดข้อมูล","กรุณารอสักครู่",false,false);
        }

        @Override
        protected Bitmap doInBackground(String... URL) {
            String imageURL = URL[0];
            Bitmap bitmap = null;
            try {
                InputStream input = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            progressDialog.dismiss();
            imageView.setImageBitmap( getRoundedShape(result));

        }
    }
    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 2000;
        int targetHeight = 2000;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }
}


