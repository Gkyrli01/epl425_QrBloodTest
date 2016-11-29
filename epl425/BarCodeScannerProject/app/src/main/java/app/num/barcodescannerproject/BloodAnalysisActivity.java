package app.num.barcodescannerproject;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by koko on 09/01/2016.
 */
public class BloodAnalysisActivity extends AppCompatActivity {

    public static final String UPLOAD_URL = "http://35.163.112.221/Upload1.php";
    public static final String UPLOAD_KEY = "image";
    public static final String TAG = "MY MESSAGE";

    private int PICK_IMAGE_REQUEST = 1;

    private Button buttonChoose;
    private Button buttonUpload;

    private ImageView imageView;

    private Bitmap bitmap;
    private  String barcode_content;
    private Uri filePath;


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String>{

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(BloodAnalysisActivity.this, "Uploading Image", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);
                data.put("username",getIntent().getExtras().getString("onoma"));
                String analysed="";
                for (int i=0;i<zapatas.size();i++){
                    analysed=analysed+zapatas.get(i).toString();
                }
                data.put("text",analysed);
                String result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }



    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    void recreateQr(){
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(barcode_content, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "BloodAnalysis Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://app.num.barcodescannerproject/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "BloodAnalysis Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://app.num.barcodescannerproject/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bloodactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.upload:
                recreateQr();
                uploadImage();
                break;
            case R.id.infoM:
                Toast.makeText(getApplicationContext(),R.string.infos,Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }

        return true;
    }

    public class Single {
        public static final int PERC=1;
        public static final int MMHR=2;
        public static final int DEKAENNATI=3;
        public static final int FL=4;
        public static final int PG=5;
        public static final int GDL=6;
        public static final int DEKADODEKATI=7;
        int type=0;
        boolean done=false;
        String myName;
        String shortName="";
        double myValue;
        double min=0;
        boolean mySex=false;
        boolean positive=false;
        boolean low=false;
        double max=Double.MAX_VALUE;
        public void setConstraints(double minV,double maxV){
                min=minV;
                max=maxV;

            if(shortName.equals("ESR")) {
                positive = myValue < minV;
                return;
            }
            if (myValue<min){
                low=true;
                return;
            }
            if(max!=Double.MAX_VALUE &&myValue>maxV) {
                low = false;
                return;
            }
            positive=true;

        }

        public  String getCorrectMonadaMetriseis(int i){

            switch (i){
                case PERC:return "%";
                case MMHR:return "mm/1hr";
                case DEKAENNATI:return  "10^9/L";
                case FL:return "fl";
                case  PG:return "pg";
                case GDL:return "g/dl";
                case DEKADODEKATI:return "10^12/L";
            }
            return "";
        }


        public Single(int singleNumber,double value,boolean sex){
            mySex=sex;
            myValue=value;
            switch(singleNumber){
                case 1:
                    myName="Hemoglobin";
                    shortName="Hb";
                    type=GDL;
                    if(sex)
                        setConstraints(13.5,17.5);
                    else
                        setConstraints(12,16);
                    break;
                case 2:
                    myName="Hematocrit";
                    shortName="Hct";
                    type=PERC;
                    if(sex)
                        setConstraints(41,53);
                    else setConstraints(36,46);
                    break;

                case 3:
                    myName="Red Blood Cells";
                    shortName="RBC";
                    type=DEKADODEKATI;
                    if(sex)
                        setConstraints(4.50,6.50);
                    else setConstraints(3.80,5.80);
                    break;
                case 4:
                    myName="Mean Cell Volume";
                    shortName="MCV";
                    type=FL;
                    setConstraints(3.80,5.80);
                    break;
                case 5:
                    myName="Mean Cell Haemoglobin";
                    shortName="MCH";
                    type=PG;
                    setConstraints(26,34);
                    break;
                case 6:
                    myName="Mean Cell Haemoglobin Concentration";
                    shortName="MCHC";
                    type=GDL;
                    setConstraints(31.5,37.5);
                    break;
                case 7:
                    myName="Red Distribution Width-Coefficient Variation";
                    shortName="RDW-CV";
                    type=PERC;
                    setConstraints(11,15);
                    break;
                case 8:
                    myName="Red Distribution Width-Standard Deviation";
                    shortName="RDW-SD";
                    type=FL;
                    setConstraints(37,47);
                    break;
                case 9:
                    myName="Platelets";
                    shortName="PLT";
                    type=DEKAENNATI;
                    setConstraints(150,400);
                    break;
                case 10:
                    myName="Mean Platelet Volume";
                    shortName="MPV";
                    type=FL;
                    setConstraints(8,12);
                    break;
                case 11:
                    myName="Platelet Distribution Width";
                    shortName="PDW";
                    type=PERC;
                    setConstraints(12,28);
                    break;
                case 12:
                    myName="Plateletcrit";
                    shortName="PCT";
                    type=PERC;
                    setConstraints(0.190,0.290);
                    break;
                case 13:
                    myName="White Blood Cells";
                    shortName="WBC";
                    type=DEKAENNATI;
                    setConstraints(4,10.80);
                    break;
                case 14:
                    myName="Differential count";
                    shortName="";
                    type=PERC;
                    break;
                case 15:
                    myName="Neutrophils";
                    shortName="";
                    type=PERC;
                    setConstraints(40,75);
                    break;
                case 16:
                    myName="Lymphocytes";
                    shortName="";
                    type=PERC;
                    setConstraints(20,45);
                    break;
                case 17:
                    myName="Monocytes";
                    shortName="";
                    type=PERC;
                    setConstraints(2,10);
                    break;
                case 18:
                    myName="Eosinophils";
                    shortName="";
                    type=PERC;
                    setConstraints(1,6);
                    break;
                case 19:
                    myName="Basophils";
                    shortName="";
                    type=PERC;
                    setConstraints(0,1);
                    break;
                case 20:
                    myName="Reticulocytes";
                    shortName="Retics";
                    type=PERC;
                    setConstraints(0.2,2);
                    break;
                case 21:
                    myName="Erythrocytes Sedimentation Rate";
                    shortName="ESR";
                    type=MMHR;
                    if(sex)
                        setConstraints(12,Double.MAX_VALUE);
                    else setConstraints(20,Double.MAX_VALUE);
                    break;
                case 22:
                    myName="Film";
                    shortName="";
                    type=0;
                    break;
            }
        }

        @Override
        public String toString() {
            String line=myName+","+shortName+","+myValue;
            if(min==0 && max==Double.MAX_VALUE){
                line=line+"\n";
            }
            else if(max==Double.MAX_VALUE){
                line=line+","+min+"\n";
            }
            else {
                line=line+","+min+","+max+"\n";
            }
            return line;

        }
    }

    public static final String PLACETAG = "com.kokos.workoutlist.place";
    public static final String WORKOUT = "com.kokos.workoutlist.takeWorkout";


    CrimeAdapter ok;
    private int current;
    private ListPopupWindow popupWindow;
    PopupWindow kokos;
    ArrayList<Single> zapatas;
    private int isDragged;

    public static View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;


        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    /*
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }
    */

    void seperateSingles(String from){
        int z=0;
        int sex=from.charAt(0)-'0';

        String current="";
        for(int i=2;i<=from.length();i++){
            if(from.charAt(i-1)==','){
                z++;
                zapatas.add(new Single(z,Double.parseDouble(current),sex==0));
                current="";
            }else
            current=current+from.charAt(i-1);
        }
        z++;
        zapatas.add(new Single(z,Double.parseDouble(current),sex==0));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Intent i=new Intent(getApplicationContext(), MainActivity.class);
        //startActivity(i);
        //getParent().finish();
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bloodanalysisactivity);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        zapatas=new ArrayList<Single>();
        String ofSingles=getIntent().getExtras().getString("");
        seperateSingles(ofSingles);
        //zapatas.add(new Single());
        final ListView listoua = (ListView) findViewById(R.id.list);
        barcode_content=getIntent().getExtras().getString("");
        RelativeLayout main = (RelativeLayout) findViewById(R.id.WorkoutsActivityLayout);
       /* main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(popupWindow!=null){
                ArrayList<Workout> zinonis=new ArrayList<Workout>();
                popupWindow.setAdapter(new SmallList(zinonis,getApplicationContext()));}
                return false;
            }
        });
*/


/*
        listoua.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), okFrAactivity.class);
                i.putExtra(PLACETAG, position);
                startActivity(i);
            }
        });
*/

        if (zapatas != null) {
            ok = new CrimeAdapter(zapatas, getApplicationContext());
            listoua.setAdapter(ok);
        }

        /*
        sendName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Workouts.getWorkOuts().add(new Workout(workOutName.getText().toString()));
                workOutName.setText("");
                workOutName.clearFocus();
                listoua.requestFocus();
                ok.notifyDataSetChanged();
                /*listoua.getAdapter().getView(Workouts.getWorkOuts().size() - 1, v.findViewById(R.id.listView), (ViewGroup) v.getParent()).
                        setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {

                                ClipData data = ClipData.newPlainText("", "");
                                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                                listoua.startDrag(data, shadowBuilder, v, 0);
                                return false;
                            }
                        });*/
        // }
        //});

        /*
        listoua.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent();
                i.putExtra("pou",position);
                isDragged=position;
                ClipData data = ClipData.newIntent("Nami", i);
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                Log.i("Y", "" + view.getY());
                Log.i("X",""+view.getX());
                listoua.startDrag(data, shadowBuilder, view, 0);


                return false;

            }
        });
*/
       /* listoua.setOnLongClickListener(new AdapterView.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //listoua.getAdapter().getView(1,v,(ViewGroup) v.getParent());

                return false;
            }
        });*/
        /*listoua.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {

                if(event.getAction()==DragEvent.ACTION_DRAG_LOCATION) {

                    Log.i("YDrag", "" + event.getY());
                    Log.i("XDrag", "" + event.getX());


                    int dist[] = new int[Workouts.getWorkOuts().size()];
                    for (int i = 0; i < Workouts.getWorkOuts().size(); i++) {
                        Log.i("XDrag", "" + getViewByPosition(i, listoua).getY());
                        if (Math.abs(getViewByPosition(i, listoua).getY() - event.getY()) < 20) {
                            if (isDragged != i) {
                                Workout o = Workouts.getWorkOuts().get(isDragged);
                                Workouts.getWorkOuts().remove(isDragged);
                                Workouts.getWorkOuts().add(i, o);
                                isDragged = i;
                                ok.notifyDataSetChanged();
                                return false;
                            }
                        }
                    }
                }
                else {

                    return true;
                }
                return false;
            }
        });*/

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /*
        private class SmallList extends ArrayAdapter<Workout> {
            public SmallList(ArrayList<Workout> crimes, Context ok) {

                super(ok, 0, crimes);
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater()
                            .inflate(R.layout.scroll, null);

                }
                /*
                View eikounou=getLayoutInflater().inflate(R.layout.imageview,null);
                View pouts=getLayoutInflater().inflate(R.layout.imageview, null);
                View lalw=getLayoutInflater().inflate(R.layout.imageview, null);

               LinearLayout lol=((LinearLayout) ((HorizontalScrollView) ((LinearLayout) convertView).
                       getChildAt(0)).getChildAt(0));

                ((LinearLayout) ((HorizontalScrollView) ((LinearLayout) convertView).
                                getChildAt(0)).getChildAt(0)).addView(eikounou);

                ((LinearLayout)((HorizontalScrollView) ((LinearLayout) convertView).
                        getChildAt(0)).getChildAt(0)).addView(pouts);

                ((LinearLayout)((HorizontalScrollView) ((LinearLayout) convertView).
                        getChildAt(0)).getChildAt(0)).addView(lalw);


                lalw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(),"Poutsa",Toast.LENGTH_LONG).show();
                    }
                });*/
    //   ((HorizontalScrollView) convertView).addView(eikounou);
            /*ImageButton edit = (ImageButton) convertView
                    .findViewById(R.id.Edit_nane_popUpDialog);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), okFrAactivity.class);
                    i.putExtra(PLACETAG, current);

                    startActivity(i);
                }
            });


            return convertView;

        }
    }*/


    private class CrimeAdapter extends ArrayAdapter<Single> {
        boolean justDismissed;
        boolean previous;

        public CrimeAdapter(ArrayList<Single> aSingle, Context ok) {

            super(ok, 0, aSingle);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater()
                        .inflate(R.layout.my_list_item, null);
            }
// Configure the view for this Crime
            convertView.setFocusableInTouchMode(true);
            convertView.setClickable(true);
            convertView.setFocusable(true);
            convertView.setLongClickable(true);

            Single c = zapatas.get(position);
            String tmp=c.myName;
            if(c.myName.length()>25 && c.shortName.length()!=0)
                tmp=c.shortName;
            TextView singleName =
                    (TextView)convertView.findViewById(R.id.singleName);
            singleName.setText(tmp);
            TextView constraint =
                    (TextView)convertView.findViewById(R.id.singleConstrain);
            if(c.shortName.equals("ESR")) {
                constraint.setText(c.myValue + " < " + c.min+" (mm/1hr)");}
            else if(c.min==0 && c.max==Double.MAX_VALUE){
                if(c.myName.equals("Film"))
                    constraint.setText(""+c.myValue);
                else
                    constraint.setText(""+c.myValue+" (%)");

            }
            else {
                constraint.setText(""+c.min+" < "+ c.myValue+ " < "+ c.max +" (" +c.getCorrectMonadaMetriseis(c.type)+")");
            }
            ImageView i=(ImageView)convertView.findViewById(R.id.green);
            ImageView j=(ImageView)convertView.findViewById(R.id.red);

            if(c.positive){
                j.setVisibility(View.INVISIBLE);
                i.setVisibility(View.VISIBLE);
            }
            else {
                i.setVisibility(View.INVISIBLE);
                j.setVisibility(View.VISIBLE);
            }
            if(c.min==0 && c.max==Double.MAX_VALUE){
                i.setVisibility(View.INVISIBLE);
                j.setVisibility(View.INVISIBLE);

            }
            /*positive=true;
            constraint.setText();
            count.setText("Count: " + c.getEx().size());
            count.setFocusable(false);
            CheckBox timer =
                    (CheckBox)convertView.findViewById(R.id.itemlist_timerIndication);
            timer.setChecked(c.isHasTimer());

            ImageButton btn=(ImageButton)convertView.findViewById(R.id.play_button_list);*/
            /*
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), Play.class);
                    i.putExtra(WORKOUT, Workouts.getWorkOuts().get(position));
                    i.putExtra(PLACETAG, position);
                    startActivity(i);
                }
            });
            */
            /*
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (justDismissed) {
                        justDismissed = false;
                    }
                    else {
                        popupWindow = new ListPopupWindow(getApplicationContext());
                        SmallList attin = new SmallList(zapatas, getApplicationContext());
                        current=position;
                        popupWindow.setAdapter(attin);
                        popupWindow.setAnchorView(v);
                        Drawable a = v.getBackground();
                        a.setAlpha(0);
                        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                            @Override
                            public void onDismiss() {
                                justDismissed = true;
                                popupWindow.dismiss();
                            }
                        });
                        popupWindow.setBackgroundDrawable(a);
                        popupWindow.show();
                    }



                    //Intent i = new Intent(getApplicationContext(), okFrAactivity.class);
                    // i.putExtra(PLACETAG, position);
                    //startActivity(i);
                }
            });
*/
            return convertView;
        }
    }
}


