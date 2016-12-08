package systop.applicationcomplain;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ScrollingData1Activity extends AppCompatActivity {
    TextView txtidpeole, peole, re, phone, txtemail, add, phonehome, atdat, date, doctor, hos, rec, res, main1, de, code, tus;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String image_data ="";
    Bitmap image;

    private ImageView imageView;
    String image_data1 ="";

    String noItem ="";
    ProgressDialog progressDialog ;

    String getStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_data1);
        clearCache.deleteCache(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setBackgroundDrawable( new ColorDrawable( getResources().getColor( R.color.title_color ) ) );

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txtidpeole = (TextView) findViewById(R.id.txtidpeole);
        peole = (TextView) findViewById(R.id.peole);
        re = (TextView) findViewById(R.id.re);
        phone = (TextView) findViewById(R.id.phone);
        txtemail = (TextView) findViewById(R.id.txtemail);
        add = (TextView) findViewById(R.id.add);
        phonehome = (TextView) findViewById(R.id.phonehome);
        atdat = (TextView) findViewById(R.id.atdat);
        date = (TextView) findViewById(R.id.date);
        doctor = (TextView) findViewById(R.id.doctor);
        hos = (TextView) findViewById(R.id.hos);
        rec = (TextView) findViewById(R.id.rec);
        res = (TextView) findViewById(R.id.res);
        main1 = (TextView) findViewById(R.id.main1);
        de = (TextView) findViewById(R.id.de);
        code = (TextView) findViewById(R.id.code);
        tus = (TextView) findViewById(R.id.tus);

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
                        if(!image_data.equals("null")){
                            new DownloadImage().execute(image_data);
                        }else{
                            noItem = "null";
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
                String IdPeople = getIntent().getStringExtra("IdPeople");
                Map<String,String> params = new HashMap<String, String>();
                params.put("image_tag",IdPeople);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

        txtidpeole.setText(IdPeople);
        peole.setText(TittleName + "  " + Name + "  นามสกุล  " + SurName);
        re.setText(Relationship);
        phone.setText(Phone);
        txtemail.setText(Email);
        //add.setText(Adress);
        atdat.setText(AtDay);
        date.setText(Day);
        de.setText(Detail);
//        tus.setText(Status);
        code.setText(IdCode);
        main1.setText(Main);
        phonehome.setText(PhoneHome);
        hos.setText(HospitalName);
        doctor.setText(DocterName);

        new getDataStatus().execute(Status);

        String[] parts = Adress.split("\\r?\\n");
        String part1 = parts[0];
        String part2 = parts[1];
        String part3 = parts[2];
        String part4 = parts[3];
        String part5 = parts[4];
        String part6 = parts[5];
        String part7 = parts[6];

        String[] HouseNo1 = part1.split("#:#");
        String dataAdress = HouseNo1[1];

        String dataAdress1 = "";
        if(!part2.equals("ซอย/หมู่บ้าน #:#")) {
            String[] Lane1 = part2.split("#:#");
            dataAdress1 = Lane1[1];
        }else {

        }

        String dataAdress2 ="";
        if(!part3.equals("ถนน #:#")){
            String[] Road1 = part3.split("#:#");
            dataAdress2 = Road1[1];
        }else {

        }

        String[] SubDistrict1 = part4.split("#:#");
        String dataAdress3 = SubDistrict1[1];

        String[] District1 = part5.split("#:#");
        String dataAdress4 = District1[1];

        String[] Province1 = part6.split("#:#");
        String dataAdress5 = Province1[1];

        String dataAdress6 ="";
        if(!part7.equals("ไปรษณีย์ #:#")) {
            String[] PostalCode1 = part7.split("#:#");
            dataAdress6 = PostalCode1[1];
        }else {

        }
        final String Adrres1 = "บ้านเลขที่ :"+dataAdress+"\n"+
                "ซอย/หมู่บ้าน :"+dataAdress1+"\n"+
                "ถนน :"+dataAdress2+"\n"+
                "ตำบล/แขวง :"+dataAdress3+"\n"+
                "อำเภอ/เขต :"+dataAdress4+"\n"+
                "จังหวัด :"+dataAdress5+"\n"+
                "ไปรษณีย์ :"+dataAdress6;

        add.setText(Adrres1);

        String Url1 = localhost.url+"pic_Get.php";
        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonarray = new JSONArray(response);
                            JSONObject jsonResponse = jsonarray.getJSONObject(0);
                            image_data1= jsonResponse.getString("image_data");

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "ไม่สามารถทำงานได้ ", Toast.LENGTH_LONG).show();
                        }
                        //Toast.makeText(getApplicationContext(),image_data, Toast.LENGTH_SHORT).show();
                        if( !image_data1.equals("null")) {
                            new DownloadImage1().execute(image_data1);
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
                String IdPeople1 = getIntent().getStringExtra("IdPeople");
                Map<String,String> params = new HashMap<String, String>();
                params.put("image_tag",IdPeople1);
                return params;
            }

        };
        RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
        requestQueue1.add(stringRequest1);


        new AsyncCheck().execute();
        new AsyncCheck1().execute();
        if (!isNetworkConnected() && !isWifiConnected()) {

            new AlertDialog.Builder(this)
                    .setTitle("ไม่มีการเชื่อมต่อข้อมูล")
                    .setMessage("กรุณาตรวจสอบอินเทอร์เน็ต")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), ClearActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();

        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab1);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                next();
//            }
//        });
        com.getbase.floatingactionbutton.FloatingActionButton fab1 = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabPdf);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printPdf();
            }
        });

        com.getbase.floatingactionbutton.FloatingActionButton fab2 = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editData();
            }
        });

        com.getbase.floatingactionbutton.FloatingActionButton fab3 = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editStatus();
            }
        });


        com.getbase.floatingactionbutton.FloatingActionButton fab5 = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab5);
        fab5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteData();
            }
        });

        final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        frameLayout.getBackground().setAlpha(0);
        final FloatingActionsMenu fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        fabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                frameLayout.getBackground().setAlpha(240);
                frameLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        fabMenu.collapse();
                        return false;
                    }
                });
            }

            @Override
            public void onMenuCollapsed() {
                frameLayout.getBackground().setAlpha(0);
                frameLayout.setOnTouchListener(null);
            }
        });
    }
    public class getDataStatus extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL(localhost.url + "getStatus.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                // setDoInput and setDoOutput to true as we send and recieve data
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // add parameter to our above url
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("searchQuery", params[0]);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jArray = new JSONArray(result);
                JSONObject json_data = jArray.getJSONObject(0);
                getStatus = (json_data.getString("st_title"));
                tus.setText(getStatus );
            } catch (JSONException e) {

            }

        }

    }
    public void deleteData () {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("ต้องการลบข้อมูลหรือไม่?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String IdCode = getIntent().getStringExtra("IdCode");
                new AsyncFetch1().execute(IdCode);
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

    public void editStatus() {
        String IdCode = getIntent().getStringExtra("IdCode");
        Intent intent = new Intent(getApplicationContext(), UpdateStatusActivity.class);
        intent.putExtra("User",IdCode);
        startActivity(intent);
        finish();
    }
    public void editData() {
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

        Intent intent = new Intent(getApplicationContext(), ScrollingUpdateEmployeeAddDataActivity.class);
        intent.putExtra("IdPeople",IdPeople);
        intent.putExtra("TittleName",TittleName);
        intent.putExtra("Name",Name);
        intent.putExtra("SurName",SurName);
        intent.putExtra("Relationship",Relationship);
        intent.putExtra("Phone",Phone);
        intent.putExtra("Email",Email);
        intent.putExtra("Adress",Adress);
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

    public void printPdf() {
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

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "Test/PDF");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("App", "failed to create directory");
            }
        }

        String pdf = Environment.getExternalStorageDirectory()
                + "/Test/PDF/"+IdCode+".pdf";
        String root = Environment.getExternalStorageDirectory()
                + "/Test/PDF/";

        File myFile = new File(pdf);
        if(myFile.exists())
            myFile.delete();

        try
        {
            Document document = new Document();
            PdfWriter docWriter =PdfWriter.getInstance(document, new FileOutputStream(pdf));
            docWriter.getAcroForm().setNeedAppearances(true);
            document.open();

            document.setPageSize(PageSize.A4);



            Font font = FontFactory.getFont("assets/fonts/thnew.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            font.setSize(16);

            Font font1 = FontFactory.getFont("assets/fonts/thnew.ttf", BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
            font1.setSize(24);
            font1.setStyle("bold");

            if(!noItem.equals("null")) {
                Bitmap bmp = image;
                Bitmap resized = Bitmap.createScaledBitmap(bmp, (int) (bmp.getWidth() * 0.045), (int) (bmp.getHeight() * 0.045), true);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                resized.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                Image images = Image.getInstance(stream.toByteArray());
                document.add(images);
            }


            ColumnText column = new ColumnText(docWriter.getDirectContent());
            column.setSimpleColumn(36, 850, 569, 36);
            column.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            column.addElement(new Paragraph("เลขที่คำร้อง "+IdCode, font));
            column.go();

            ColumnText column1 = new ColumnText(docWriter.getDirectContent());
            column1.setSimpleColumn(240, 820, 569, 36);
            column1.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            column1.addElement(new Paragraph("แบบฟอร์มเรื่องร้องเรียน", font1));
            column1.go();


            ColumnText column2 = new ColumnText(docWriter.getDirectContent());
            column2.setSimpleColumn(460, 760, 700, 36);
            column2.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            column2.addElement(new Paragraph("เขียนที่  แพทยสภา", font));
            column2.go();

            String[] parts = AtDay.split("-");
            String part1 = parts[0];
            String part2 = parts[1];
            String part3 = parts[2];
            String mount = "";
            int year = Integer.parseInt(part1)+543;
            if (part2.equals("01") || part2.equals("1")){
                mount = "มกราคม";
            }else if (part2.equals("02") || part2.equals("2")){
                mount = "กุมภาพันธ์";
            }else if (part2.equals("03") || part2.equals("3")){
                mount = "มีนาคม";
            }else if (part2.equals("04") || part2.equals("4")){
                mount = "เมษายน";
            }else if (part2.equals("05") || part2.equals("5")){
                mount = "พฤษภาคม";
            }else if (part2.equals("06") || part2.equals("6")){
                mount = "มิถุนายน";
            }else if (part2.equals("07") || part2.equals("7")){
                mount = "กรกฎาคม";
            }else if (part2.equals("08") || part2.equals("8")){
                mount = "สิงหาคม";
            }else if (part2.equals("09") || part2.equals("9")){
                mount = "กันยายน";
            }else if (part2.equals("10")){
                mount = "ตุลาคม";
            }else if (part2.equals("11")){
                mount = "พฤศจิกายน";
            }else if (part2.equals("12")){
                mount = "ธันวาคม";
            }

            ColumnText column3 = new ColumnText(docWriter.getDirectContent());
            column3.setSimpleColumn(410, 740, 569, 36);
            column3.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            column3.addElement(new Paragraph("วันที่ "+part3+" "+mount+" พ.ศ. "+year, font));
            column3.go();

            ColumnText column4 = new ColumnText(docWriter.getDirectContent());
            column4.setSimpleColumn(48, 720, 569, 36);
            column4.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            column4.addElement(new Paragraph("เรื่อง  ร้องเรียนจริยธรรมของผู้ประกอบวิชาชีพเวชกรรม ", font));
            column4.go();

            ColumnText column5 = new ColumnText(docWriter.getDirectContent());
            column5.setSimpleColumn(48, 700, 569, 36);
            column5.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            column5.addElement(new Paragraph("เรียน  เลขาธิการแพทยสภา ", font));
            column5.go();

            String[] adress = Adress.split("\\r?\\n");
            String data = adress[0];
            String data1 = adress[1];
            String data2 = adress[2];
            String data3 = adress[3];
            String data4 = adress[4];
            String data5 = adress[5];
            String data6 = adress[6];

            String[] HouseNo1 = data.split("#:#");
            String HouseNo = HouseNo1[1];

            String Lane ="";
            if(!data1.equals("ซอย/หมู่บ้าน #:#")) {
                String[] Lane1 = data1.split("#:#");
                Lane = Lane1[1];
            }else {
                Lane = "                     ";

            }

            String Road = "";
            if(!data2.equals("ถนน #:#")){
                String[] Road1 = data2.split("#:#");
                Road = Road1[1];
            }else {
                Road ="                   ";
            }


            String[] SubDistrict1 = data3.split("#:#");
            String SubDistrict = SubDistrict1[1];

            String[] District1 = data4.split("#:#");
            String District = District1[1];

            String[] Province1 = data5.split("#:#");
            String Province = Province1[1];

            String PostalCode ="";
            if(!data6.equals("ไปรษณีย์ #:#")) {
                String[] PostalCode1 = data6.split("#:#");
                PostalCode = PostalCode1[1];
            }else {
                PostalCode ="                              ";
            }

            ColumnText column6 = new ColumnText(docWriter.getDirectContent());
            column6.setSimpleColumn(80, 680, 569, 36);
            column6.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            column6.addElement(new Paragraph("ด้วยข้าพเจ้า   "+TittleName+"    "+Name+"   นามสกุล    "+SurName+"    อยู่บ้านเลขที่   "
                    + HouseNo  +"     ซอย/หมู่บ้าน     "+Lane, font));
            column6.go();

            ColumnText column7 = new ColumnText(docWriter.getDirectContent());
            column7.setSimpleColumn(48, 660, 569, 36);
            column7.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            column7.addElement(new Paragraph("ถนน    "+Road+"       ตำบล/แขวง      "+SubDistrict+"      อำเภอ/เขต      "+District+
                    "      จังหวัด      "+Province, font));
            column7.go();

            ColumnText column8 = new ColumnText(docWriter.getDirectContent());
            column8.setSimpleColumn(48, 640, 569, 36);
            column8.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            column8.addElement(new Paragraph("รหัสไปรษณีย์    "+PostalCode+"    โทรศัพท์    "+Phone, font));
            column8.go();

            if (Relationship.isEmpty()){
                Relationship="ไม่เกี่ยวข้องกับผู้เสียหาย";
            }

            ColumnText column9 = new ColumnText(docWriter.getDirectContent());
            column9.setSimpleColumn(48, 620, 569, 36);
            column9.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            column9.addElement(new Paragraph("เกี่ยวข้องกับผู้เสียหาย  "+Relationship, font));
            column9.go();

            String txtname =  "";
            String[] partsDoc = DocterName.split("\\r?\\n");
            for(int i = 0 ; i< partsDoc.length;i++) {
                String a = partsDoc[i];
                String[] b = a.split(" : ");
                String dataDoctor =b[1];
                txtname = txtname+(i+1)+dataDoctor+"\n";
            }

            ColumnText column10 = new ColumnText(docWriter.getDirectContent());
            column10.setSimpleColumn(48, 600, 569, 36);
            column10.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            column10.addElement(new Paragraph("มีความประสงคจะร้องเรียน ", font));
            column10.go();

            ColumnText column11 = new ColumnText(docWriter.getDirectContent());
            column11.setSimpleColumn(180, 600, 569, 36);
            column11.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            column11.addElement(new Paragraph(txtname, font));
            column11.addElement(new Paragraph("โรงพยาบาล  "+HospitalName, font));
            column11.go();

            ColumnText column12 = new ColumnText(docWriter.getDirectContent());
            column12.setSimpleColumn(80, column11.getYLine(), 569, 36);
            column12.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            column12.addElement(new Paragraph("ดังนี้ พฤติกรรมโดยย่อที่ต้องการร้องเรียน  ", font));
            column12.go();

            ColumnText column13 = new ColumnText(docWriter.getDirectContent());
            column13.setSimpleColumn(48, column12.getYLine(), 569, 36);
            column13.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            column13.addElement(new Paragraph(Detail, font));
            column13.go();






            document.close();
            Log.d("OK", "done");


            File files = new File(pdf);

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(files);
            intent.setDataAndType(uri, "application/pdf");
            List<ResolveInfo> resolved = getPackageManager().queryIntentActivities(intent, 0);
            if(resolved != null && resolved.size() > 0)
            {
                startActivity(intent);
            }
            else
            {
                // notify the user they can't open it.
            }
            String filename=files.toString().substring(files.toString().lastIndexOf("/")+1);
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(ScrollingData1Activity.this)
                            .setSmallIcon(R.drawable.icon_app)
                            .setContentTitle("แสดงข้อมูล PDF")
                            .setContentText(filename.toString());
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(0, builder.build());

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (DocumentException e1) {
            e1.printStackTrace();
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

    private class AsyncCheck extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;
        String ResponsiblePerson = getIntent().getStringExtra("ResponsiblePerson");

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL(localhost.url + "getName.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setChunkedStreamingMode(1024);
                // setDoInput and setDoOutput to true as we send and recieve data
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // add parameter to our above url
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("searchQuery", ResponsiblePerson);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("no rows")){
                res.setText("ไม่มีผู้รับผิดชอบ");
            }else {
                try {
                    JSONArray jArray = new JSONArray(result);
                    JSONObject json_data = jArray.getJSONObject(0);
                    res.setText(json_data.getString("NameUser") + "  " + json_data.getString("SurNameUser"));

                } catch (JSONException e) {

                }
            }
        }

    }

    private class AsyncCheck1 extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;
        String RecipientComplaints = getIntent().getStringExtra("RecipientComplaints");

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL(localhost.url + "getName.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setChunkedStreamingMode(1024);
                // setDoInput and setDoOutput to true as we send and recieve data
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // add parameter to our above url
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("searchQuery", RecipientComplaints);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("no rows")){
                rec.setText("ไม่มีผู้รับเรื่อง");
            }else {
                try {
                    JSONArray jArray = new JSONArray(result);
                    JSONObject json_data = jArray.getJSONObject(0);
                    rec.setText(json_data.getString("NameUser") + "  " + json_data.getString("SurNameUser"));

                } catch (JSONException e) {

                }
            }
        }

    }
    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

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

            image = result;

        }
    }
    private class DownloadImage1 extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ScrollingData1Activity.this,"กำลังโหลดข้อมูล","กรุณารอสักครู่",false,false);
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


    private class AsyncFetch1 extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL(localhost.url+"deleteData.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);


                Uri.Builder builder = new Uri.Builder().appendQueryParameter("searchQuery", params[0]);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();


            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        @Override
        protected void onPostExecute(String result) {

        }

    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }


}
