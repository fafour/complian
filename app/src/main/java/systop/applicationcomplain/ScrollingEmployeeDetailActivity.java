package systop.applicationcomplain;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ScrollingEmployeeDetailActivity extends AppCompatActivity {
    EditText txtMain;
    EditText editText1,editText2;
    private int year;
    private int month;
    private int day;
    private int year1;
    private int month1;
    private int day1;
    private static final int DATE_DIALOG_ID = 1;
    private String currentDate = "";
    private String currentDate1 = "";
    private String data = "";
    String time ="";
    private ImageView imageView;
    String image_data ="";
    ProgressDialog progressDialog ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_employee_detail);
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
                final AlertDialog.Builder dialog1 = new AlertDialog.Builder(ScrollingEmployeeDetailActivity.this);
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
        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);

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
                        new DownloadImage().execute(image_data);
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
                params.put("image_tag",txtIdpeople);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

        Date dNow = new Date( );
        SimpleDateFormat ft =
                new SimpleDateFormat ("yyyy-MM-dd");

        editText1.setText( ft.format(dNow));
        editText2.setText( ft.format(dNow));
        currentDate = ft.format(dNow);
        currentDate1 = ft.format(dNow);

        txtMain = (EditText) findViewById(R.id.txtMain);


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
    public void enterData(final View view){
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        final View subView = inflater.inflate(R.layout.at_data, null);
        final EditText name1 = (EditText)subView.findViewById(R.id.item_name);
        name1.setText(data);
        name1.setScroller(new Scroller(getApplicationContext()));
        name1.setVerticalScrollBarEnabled(true);
        name1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        name1.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setView(subView);
        dialog.create();
        final Button button1 = (Button)subView.findViewById(R.id.button1);
        final Button button2 = (Button)subView.findViewById(R.id.button2);

        final AlertDialog alertDialog = dialog.create();

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String txtname = name1.getText().toString();
                if(!txtname.isEmpty()) {
                    data = txtname;
                    alertDialog.cancel();
                }else {
                    if (txtname.isEmpty()) {
                        name1.setError("กรุณากรอกพฤติกรรมโดยย่อ");

                    } else {
                        name1.setError(null);
                    }

                }
            }
        });

        alertDialog.show();
    }

    public void yes(final View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("ต้องการทำรายการถัดไปแล้วหรือไม่?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (!txtMain.getText().toString().isEmpty() && !data.isEmpty()
                        ){
                    final String txtIdpeople = getIntent().getStringExtra("Idpeople");
                    final String txtTitleName = getIntent().getStringExtra("TitleName");
                    final String txtName = getIntent().getStringExtra("Name");
                    final String txtSurname = getIntent().getStringExtra("Surname");
                    final String txtRelationship = getIntent().getStringExtra("Relationship");
                    final String txtPhone = getIntent().getStringExtra("phone");
                    final String txtEmail = getIntent().getStringExtra("Email");

                    final String txtHouseNo = getIntent().getStringExtra("HouseNo");
                    final String txtLane = getIntent().getStringExtra("Lane");
                    final String txtRoad = getIntent().getStringExtra("Road");
                    final String txtSubDistrict = getIntent().getStringExtra("SubDistrict");
                    final String txtDistrict = getIntent().getStringExtra("District");
                    final String txtProvince = getIntent().getStringExtra("Province");
                    final String txtPostalCode = getIntent().getStringExtra("PostalCode");
                    final String txtPhoneHome = getIntent().getStringExtra("PhoneHome");

                    final String DataCode  = getIntent().getStringExtra("code");

                    final String Adrres = "บ้านเลขที่ #:#"+txtHouseNo+"\n"+
                            "ซอย/หมู่บ้าน #:#"+txtLane+"\n"+
                            "ถนน #:#"+txtRoad+"\n"+
                            "ตำบล/แขวง #:#"+txtSubDistrict+"\n"+
                            "อำเภอ/เขต #:#"+txtDistrict+"\n"+
                            "จังหวัด #:#"+txtProvince+"\n"+
                            "ไปรษณีย์ #:#"+txtPostalCode;

                    final String txtDoctorName = getIntent().getStringExtra("DoctorName");
                    final String txtHospitalName = getIntent().getStringExtra("HospitalName");

                    final String txtDay = currentDate +"  ถึง  "+currentDate1;

                    Date dNow = new Date( );
                    SimpleDateFormat ft =
                            new SimpleDateFormat ("yyyy-MM-dd");

                    String User = getIntent().getStringExtra("User");
                    final String txtAtDay = ft.format(dNow);
                    final String txtMain1 = txtMain.getText().toString().trim();
                    final String txtDetail = data;
                    final String txtStatus = "1";
                    final String txtResponsiblePerson = User;
                    final String txtRecipientComplaints = User;


                    String Url = localhost.url+"insert.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(getApplicationContext(),"ทำรายการเสร็จสิ้น",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), Success2Activity.class);
                                    intent.putExtra("IdCode", DataCode);
                                    intent.putExtra("IdPeople",txtIdpeople);
                                    intent.putExtra("TittleName",txtTitleName);
                                    intent.putExtra("Name",txtName);
                                    intent.putExtra("SurName",txtSurname);
                                    intent.putExtra("Relationship",txtRelationship);
                                    intent.putExtra("Phone",txtPhone);
                                    intent.putExtra("Email",txtEmail);
                                    intent.putExtra("Adress",Adrres);
                                    intent.putExtra("PhoneHome",txtPhoneHome);
                                    intent.putExtra("DocterName",txtDoctorName);
                                    intent.putExtra("HospitalName",txtHospitalName);
                                    intent.putExtra("Detail",txtDetail);
                                    intent.putExtra("Day",txtDay);
                                    intent.putExtra("AtDay",txtAtDay);
                                    intent.putExtra("ResponsiblePerson",txtResponsiblePerson);
                                    intent.putExtra("Main",txtMain1);
                                    intent.putExtra("Status",txtStatus);
                                    intent.putExtra("RecipientComplaints",txtRecipientComplaints);
                                    startActivity(intent);
                                    finish();

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    AlertDialog.Builder dialogs = new AlertDialog.Builder(ScrollingEmployeeDetailActivity.this);
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
                            params.put("idpeople",txtIdpeople);
                            params.put("tittleName",txtTitleName);
                            params.put("name",txtName);
                            params.put("surName", txtSurname);
                            params.put("relationship", txtRelationship);
                            params.put("phoneNumber",txtPhone);
                            params.put("email", txtEmail);
                            params.put("adress",Adrres);
                            params.put("phoneHome", txtPhoneHome);
                            params.put("nameDoctor",txtDoctorName);
                            params.put("hospitalName", txtHospitalName);
                            params.put("detai", txtDetail);
                            params.put("day", txtDay);
                            params.put("atDay", txtAtDay);
                            params.put("idCode", DataCode);
                            params.put("responsiblePerson",txtResponsiblePerson);
                            params.put("main", txtMain1);
                            params.put("status", txtStatus);
                            params.put("recipientComplaints", txtRecipientComplaints);

                            return params;
                        }

                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(stringRequest);

                    //---------------------------------------------------------------------------------------------------------------------

                    String Url1 = localhost.url+"insertWeb.php";
                    StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Url1,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }){
                        @Override
                        protected Map<String,String> getParams()throws com.android.volley.AuthFailureError{
                            java.util.Date dt = new java.util.Date();
                            java.text.SimpleDateFormat sdf =
                                    new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                            String currentTime = sdf.format(dt);

                            Map<String,String> params = new HashMap<String, String>();
                            params.put("Content-Type", "application/json; charset=utf-8");
                            params.put("com_code",DataCode);
                            params.put("com_card_number",txtIdpeople);
                            params.put("com_title_name",txtTitleName);
                            params.put("com_name",txtName);
                            params.put("com_lname",txtSurname);
                            params.put("no_address",txtHouseNo);
                            params.put("alley",txtLane);
                            params.put("road",txtRoad);
                            params.put("district",txtSubDistrict);
                            params.put("prefecture",txtDistrict);
                            params.put("province",txtProvince);
                            params.put("zip_code",txtPostalCode);
                            params.put("relevance",txtRelationship);
                            params.put("com_tel",txtPhone);
                            params.put("com_email",txtEmail);
                            params.put("com_date",currentTime);
                            params.put("com_status",String.valueOf(Integer.parseInt("1")));
                            params.put("authority_id", String.valueOf(Integer.parseInt("0")));
                            params.put("PhoneHome",txtPhoneHome);
                            params.put("responsiblePerson",txtResponsiblePerson);
                            params.put("Main",txtMain1);
                            return params;
                        }

                    };
                    RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                    requestQueue1.add(stringRequest1);

                    //---------------------------------------------------------------------------------------------------------------

                }else {
                    if (txtMain.getText().toString().isEmpty()) {
                        txtMain.setError("กรุณากรอกหัวข้อ");
                    }else {
                        txtMain.setError(null);
                    }
                    if (data.isEmpty()) {
                        AlertDialog.Builder dialogs = new AlertDialog.Builder(ScrollingEmployeeDetailActivity.this);
                        dialogs.setTitle("คำเตือน");
                        dialogs.setMessage("กรุณากรอกพฤติกรรมโดยย่อ");
                        dialogs.setCancelable(true);
                        dialogs.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        dialogs.show();
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
    @TargetApi(Build.VERSION_CODES.N)
    public void select(View view){

        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        showDialog(DATE_DIALOG_ID);

    }

    @TargetApi(Build.VERSION_CODES.N)
    public void select1(View view){
        Calendar c1 = Calendar.getInstance();
        year1 = c1.get(Calendar.YEAR);
        month1 = c1.get(Calendar.MONTH);
        day1 = c1.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd = new DatePickerDialog(ScrollingEmployeeDetailActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        editText2.setText( year + "-" + (String.format("%02d", monthOfYear + 1  ))
                                + "-" + String.format("%02d", dayOfMonth  ));
                        currentDate1 = year + "-" + (String.format("%02d", monthOfYear + 1  ))
                                + "-" + String.format("%02d", dayOfMonth  );
                    }
                }, year1, month1, day1);
        dpd.show();
        SimpleDateFormat formatter1 = new SimpleDateFormat ("yyyy-MM-dd");
        try {
            time = currentDate;
            Date date1 = formatter1.parse(time);
            dpd.getDatePicker().setMinDate(date1.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        dpd.getDatePicker().setMaxDate(new Date().getTime());


    }
    private void updateDisplay() {
        currentDate = new StringBuilder().append(year).append("-")
                .append(String.format("%02d", month + 1  )).append("-").append(String.format("%02d", day  )).toString();

        Log.i("DATE", currentDate);
        SimpleDateFormat formatter2 = new SimpleDateFormat ("yyyy-MM-dd");
        try {

            Date date1 = formatter2.parse(currentDate);
            Date date2 = formatter2.parse(currentDate1);

            if(date2.before(date1)){
                editText2.setText(currentDate);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    DatePickerDialog.OnDateSetListener myDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker datePicker, int i, int j, int k) {
            year = i;
            month = j;
            day = k;
            updateDisplay();
            editText1.setText(currentDate);

        }
    };



    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                DatePickerDialog dialog = new DatePickerDialog(this, myDateSetListener, year, month,
                        day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                return dialog;

        }
        return null;
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
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ScrollingEmployeeDetailActivity.this,"กำลังโหลดข้อมูล","กรุณารอสักครู่",false,false);
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
