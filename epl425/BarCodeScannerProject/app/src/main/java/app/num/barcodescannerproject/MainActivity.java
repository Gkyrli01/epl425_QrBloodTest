package app.num.barcodescannerproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.WriterException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Random;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;


    public static final String UPLOAD_URL = "http://35.163.112.221//Upload.php";
    public static final String UPLOAD_KEY = "image";
    public static final String TAG = "MY MESSAGE";

    private int PICK_IMAGE_REQUEST = 1;
    boolean open=false;
    private Button buttonChoose;
    private Button buttonUpload;

    private ImageView imageView;

    private Bitmap bitmap;

    private Uri filePath;
/*
    private void SaveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences sharedPref = getSharedPreferences("myprefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
        finish();
        //Intent i=new Intent(getApplicationContext(),LoginActivity.class);
        //startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String barcode_content="014,43,5,54,32,33,14,40,200,9,8,0.200,4,30,50,33,6,5,1,2,7,21";
       /* try {
            // generate a 150x150 QR code
            Bitmap bm = E(barcode_content, BarcodeFormat.QR_CODE, 150, 150);

            if(bm != null) {
                image_view.setImageBitmap(bm);
            }
        } catch (WriterException e) { //eek }*/




        //SaveImage(bitmap);


/*
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        UploadRequest registerRequest = new UploadRequest(getStringImage(bitmap), responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(registerRequest);
*/


/*
        Intent i = new Intent(getApplicationContext(), BloodAnalysisActivity.class);
        i.putExtra("","014,43,5,54,32,33,14,40,200,9,8,0.200,4,30,50,33,6,5,1,2,7,21");

        startActivity(i);*/
    }

    public void QrScanner(View view){

        open=true;
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);

        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();         // Start camera

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mScannerView!=null) {
            mScannerView.resumeCameraPreview(this);
            mScannerView.startCamera();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(open){
        mScannerView.resumeCameraPreview(this);
        mScannerView.stopCamera();     }      // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here

        Log.e("handler", rawResult.getText()); // Prints scan results
        Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)

        // show the scanner result into dialog box.
        //AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Scan Result");
        //builder.setMessage(rawResult.getText());
        //AlertDialog alert1 = builder.create();
        //alert1.show();

        Intent i = new Intent(getApplicationContext(), BloodAnalysisActivity.class);
        i.putExtra("",rawResult.getText());
        i.putExtra("onoma",getIntent().getExtras().getString("username"));
        startActivity(i);

        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }
}
