package com.hoc.androidlearning;

import static android.Manifest.permission_group.CAMERA;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InsertActivity extends AppCompatActivity {

    EditText rdIdI, rdNameI, rdPriceI, rdDesI;
    Button btnadd;
    DBhelperActivity dbh;
    Bitmap myBitmap;
    Uri picUri;

    ImageView imgInsert;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_insert);

        Toolbar toolbar = findViewById(R.id.toolbar_insert);
        toolbar.setTitle("Insert Foods");
        toolbar.setBackgroundColor(Color.parseColor("#FF6EDF94"));
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        };

        dbh =new DBhelperActivity(this);
        initView();
    }

    void initView(){
        rdIdI = findViewById(R.id.edIdI);
        rdNameI = findViewById(R.id.edNameI);
        rdPriceI = findViewById(R.id.edPriceI);
        rdDesI = findViewById(R.id.edPriceI);
        btnadd = findViewById(R.id.btnadd);
        imgInsert = findViewById(R.id.imgInsert);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodId = rdIdI.getText().toString();
                String foodName = rdNameI.getText().toString();
                String foodPrice = rdPriceI.getText().toString();
                String foodDes = rdDesI.getText().toString();
                String str = "";

                if( myBitmap != null){
                    str = BitMapToString(myBitmap);
                    Log.d("anh chuoi", str);
                }else{
                    str = "";
                }
                String sql = "INSERT INTO FOODS VALUES('"+foodId+"','"+foodName+"','"+foodPrice+"','"+foodDes+"','"+str+"')";
                try {
                    SQLiteDatabase db1 = dbh.ketNoiDBWrite();
                    db1.execSQL(sql);
                    db1.close();
                    Intent intent = new Intent();
                    setResult(200, intent);
                    finish();
                    Toast.makeText(getApplicationContext(), "Thêm thành công", Toast.LENGTH_LONG).show();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Khong thanh cong"+e, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_insert, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }else if(item.getItemId() == R.id.camera){
            Toast.makeText(getApplicationContext(), "open camera", Toast.LENGTH_LONG).show();
            permissions.add(CAMERA);
            permissionsToRequest = findUnAskedPermissions(permissions);
            startActivityForResult(getPickImageChooserIntent(), 200);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0)
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }

        }
        allIntents.add(0,captureIntent);

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);

        }
        Intent chooserIntent = Intent.createChooser(captureIntent, "Select source");
        //chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, captureIntent);

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
        Log.v("allIntents",""+allIntents.size());
        return chooserIntent;
    }

    //Nhận tấm ảnh mà mình chụp và chọn
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap;
        if (resultCode == Activity.RESULT_OK) {

            //    imageView = (ImageView) findViewById(R.id.imageViewCamera);

            if (getPickImageResultUri(data) != null) {
                picUri = getPickImageResultUri(data);
                Log.v("picUri",""+picUri.getPath());
                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);


                    //  croppedImageView.setImageBitmap(myBitmap);
                    imgInsert.setImageBitmap(myBitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {

                Log.v("picUri","null1");
                bitmap = (Bitmap) data.getExtras().get("data");

                myBitmap = bitmap;


                imgInsert.setImageBitmap(myBitmap);

            }

        }

    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }
    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }


        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }
    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }
    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("pic_uri", picUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        picUri = savedInstanceState.getParcelable("pic_uri");
        Log.v("pic_uri",picUri.getPath());
    }
}