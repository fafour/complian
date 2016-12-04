package systop.applicationcomplain;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Success1Activity extends AppCompatActivity {
    TextView txtID ;
    String TittleName = "";
    String Name ="";
    String SurName = "";
    String Relationship ="";
    String Phone = "";
    String Adress = "";
    String DocterName = "";
    String HospitalName = "";
    String Detail = "";
    String AtDay = "";
    String IdCode ="";
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String image_data ="";
    Bitmap image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setBackgroundDrawable( new ColorDrawable( getResources().getColor( R.color.title_color ) ) );

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog1 = new AlertDialog.Builder(Success1Activity.this);
                dialog1.setTitle("ต้องการย้อนกลับไปหน้าหลักหรือไม่?");
                dialog1.setCancelable(true);
                dialog1.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String txtIdpeople = getIntent().getStringExtra("IdPeople");
                        Intent intent = new Intent(getApplicationContext(), IndexActivity.class);
                        intent.putExtra("Idpeople", txtIdpeople);
                        startActivity(intent);
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

        txtID = (TextView) findViewById(R.id.txtID);
        String idcode = getIntent().getStringExtra("IdCode");
        txtID.setText(idcode);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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
                String IdPeople = getIntent().getStringExtra("IdPeople");
                Map<String,String> params = new HashMap<String, String>();
                params.put("image_tag",IdPeople);
                return params;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

        new AsyncFetch().execute();
        clearCache.deleteCache(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });
    }
    public void next() {
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


            Bitmap bmp = image;
            Bitmap resized = Bitmap.createScaledBitmap(bmp,(int)(bmp.getWidth()*0.045), (int)(bmp.getHeight()*0.045), true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            Image images = Image.getInstance(stream.toByteArray());
            document.add(images);


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

            String[] adress = Adress.split("\n");
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
            String[] partsDoc = DocterName.split("\n");
            for(int i = 0 ; i< partsDoc.length;i++) {
                String a = partsDoc[i];
                String[] b = a.split(":");
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
                    new NotificationCompat.Builder(Success1Activity.this)
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
    public void Click1(View view){
        String txtIdpeople = getIntent().getStringExtra("IdPeople");
        Intent intent = new Intent(getApplicationContext(), IndexActivity.class);
        intent.putExtra("Idpeople", txtIdpeople);
        startActivity(intent);
        finish();
    }
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("เสร็จสิ้นการทำงานแล้วหรือไม่?");
        dialog.setCancelable(true);
        dialog.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String txtIdpeople = getIntent().getStringExtra("IdPeople");
                Intent intent = new Intent(getApplicationContext(), IndexActivity.class);
                intent.putExtra("Idpeople", txtIdpeople);
                startActivity(intent);
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

    private class AsyncFetch extends AsyncTask<String, String, String> {
        HttpURLConnection conn;
        URL url = null;
        String idcode = getIntent().getStringExtra("IdCode");

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your json file resides
                // Even you can make call to php file which returns json data
                url = new URL(localhost.url+"showData.php");

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
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("searchQuery", idcode);
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

            //this method will be running on UI thread
            List<DataComplain> data=new ArrayList<>();

            try {

                JSONArray jArray = new JSONArray(result);
                    JSONObject json_data = jArray.getJSONObject(0);
                    DataComplain Data = new DataComplain();
                    Data.Main= json_data.getString("Main");
                    Data.IdCode= json_data.getString("IdCode");
                    Data.Status= json_data.getString("Status");
                    Data.DocterName= json_data.getString("NameDoctor");
                    Data.ResponsiblePerson= json_data.getString("responsiblePerson");

                    Data.IdPeople= json_data.getString("IdPeople");
                    Data.TittleName= json_data.getString("TittleName");
                    Data.Name= json_data.getString("Name");
                    Data.SurName= json_data.getString("SurName");
                    Data.Relationship= json_data.getString("Relationship");
                    Data.Phone= json_data.getString("PhoneNumber");
                    Data.PhoneHome= json_data.getString("PhoneHome");
                    Data.Email= json_data.getString("Email");
                    Data.Adress= json_data.getString("Adress");
                    Data.Day= json_data.getString("Day");
                    Data.AtDay= json_data.getString("AtDay");
                    Data.Detail= json_data.getString("Detai");
                    Data.HospitalName= json_data.getString("HospitalName");
                    Data.RecipientComplaints= json_data.getString("RecipientComplaints");

                    data.add(Data);

                    TittleName = json_data.getString("TittleName");
                    Name =json_data.getString("Name");
                    SurName =json_data.getString("SurName");
                    Relationship =json_data.getString("Relationship");
                    Phone = json_data.getString("PhoneNumber");
                    Adress = json_data.getString("Adress");
                    DocterName = json_data.getString("NameDoctor");
                    HospitalName =json_data.getString("HospitalName");
                    Detail = json_data.getString("Detai");
                    AtDay = json_data.getString("AtDay");
                    IdCode = json_data.getString("IdCode");

            } catch (JSONException e) {

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
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

}
