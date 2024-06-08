package com.hoc.androidlearning;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InsertActivity extends AppCompatActivity {

    EditText rdIdI, rdNameI, rdPriceI, rdDesI;
    Button btnadd;
    DBhelperActivity dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_insert);
        dbh =new DBhelperActivity(this);
        initView();
    }

    void initView(){
        rdIdI = findViewById(R.id.edIdI);
        rdNameI = findViewById(R.id.edNameI);
        rdPriceI = findViewById(R.id.edPriceI);
        rdDesI = findViewById(R.id.edPriceI);
        btnadd = findViewById(R.id.btnadd);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodId = rdIdI.getText().toString();
                String foodName = rdNameI.getText().toString();
                String foodPrice = rdPriceI.getText().toString();
                String foodDes = rdDesI.getText().toString();
                String sql = "INSERT INTO FOODS VALUES('"+foodId+"','"+foodName+"','"+foodPrice+"','"+foodDes+"')";
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
}