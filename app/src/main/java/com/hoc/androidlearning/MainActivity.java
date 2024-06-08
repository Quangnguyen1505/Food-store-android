package com.hoc.androidlearning;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import model.Food;

public class MainActivity extends AppCompatActivity {
    TextView txtAddItem, txtAddFood;
    ListView listItem;
    Cursor cs;
    ArrayList<Food> arrfoods;
    SQLiteDatabase db;
    DBhelperActivity dbh;
    CustomAdapterActivity adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        toolbar.setTitle("");
        toolbar.setBackgroundColor(Color.parseColor("#FF6EDF94"));
        setSupportActionBar(toolbar);
        try {
            initView();
            Log.d("kk", "onCreate: ");
            dbh = new DBhelperActivity(this);
            cs = dbh.initRecordFirstDB();
            showDataListView();
        } catch (Exception e) {
            Log.e("kk", "onCreate: Exception", e);
            Toast.makeText(getApplicationContext(), "Exception: " + e, Toast.LENGTH_LONG).show();
        }
    }

    void initView() {
        txtAddItem = findViewById(R.id.txtadditem);
        listItem = findViewById(R.id.listitem);
        txtAddFood = findViewById(R.id.txtadditem);

        txtAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), InsertActivity.class);
                startActivityForResult(in, 200);
                //startActivity(in);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 200){
            showDataListView();
        }else if(resultCode == 201){
            showDataListView();
        }
    }

    private void showDataListView() {
        try {
            db = dbh.ketNoiDBRead();
            arrfoods = new ArrayList<>();
            Cursor cursor = db.rawQuery("SELECT * FROM FOODS", null);
            try {
                while (cursor.moveToNext()) {
                    Food f = new Food(cursor.getString(0), cursor.getString(1), Float.parseFloat(cursor.getString(2)), cursor.getString(3), cursor.getString(4));
                    arrfoods.add(f);
                    adapter = new CustomAdapterActivity(this, arrfoods);
                    listItem.setAdapter(adapter);
                }

            } finally {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("ll", "showDataListView: Exception", e);
            Toast.makeText(getApplicationContext(), "Exception: " + e, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }else if(item.getItemId() == R.id.textsignout){
            showDialogconfirmSignOut();
        }else if(item.getItemId() == R.id.doimk){

            Toast.makeText(this, "Đổi mật khẩu được chọn", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ChangePassword.class);
            startActivity(intent);
            return true;
        }else if(item.getItemId() == R.id.doiten){

            Toast.makeText(this, "Đổi tên được chọn", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ChangeNameUser.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialogconfirmSignOut(){
        //Tạo đối tượng
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Xác nhận");
        b.setMessage("Bạn có đồng ý signout không ?");
        b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                SharedPreferences settings = getSharedPreferences("Login", MODE_PRIVATE);
                settings.edit().clear().commit();
                Intent in = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(in);
                Toast.makeText(getApplicationContext(), "SignOut thành công", Toast.LENGTH_LONG).show();
            }
        });
        b.setNegativeButton("Không đồng ý", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog al = b.create();
        al.show();
    }
}
