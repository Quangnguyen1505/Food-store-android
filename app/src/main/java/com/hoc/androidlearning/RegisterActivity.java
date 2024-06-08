package com.hoc.androidlearning;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    RadioButton rdMen, rdWomen;
    Button btnSignUp;
    EditText txtusername, txtpassword, txtfullname;
    Spinner spbirth;
    String valueBirth;
    DBhelperActivity dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        dbh =new DBhelperActivity(this);
        initView();
    }

    void initView (){
        rdMen = findViewById(R.id.rdMen);
        rdWomen = findViewById(R.id.rdWomen);
        btnSignUp = findViewById(R.id.btnRegister);
        txtusername = findViewById(R.id.txtnameS);
        txtpassword = findViewById(R.id.txtPassworkS);
        txtfullname = findViewById(R.id.txtFullNameS);
        spbirth = findViewById(R.id.spbirth);
        ArrayList<String> arrayList = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spbirth.setAdapter(arrayAdapter);
        for (int i = 1900; i<= 2000; i++){
            arrayList.add(""+i);
        }
        rdMen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdWomen.setChecked(false);
            }
        });

        rdWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdMen.setChecked(false);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String user = txtusername.getText().toString();
                    String pass = txtpassword.getText().toString();
                    String fullname = txtfullname.getText().toString();
                    String gender = "Nam";
                    if(rdWomen.isChecked()){
                        gender = "Nu";
                    }

                    String sql = "insert into login values('"+user+"','"+pass+"','"+fullname+"','"+gender+"', '"+valueBirth+"')";
                    SQLiteDatabase db = dbh.ketNoiDBWrite();
                    db.execSQL(sql);
                    Log.d("sql", "sql: "+sql);
                    finish();
                }catch (Exception e) {
                    Log.d("error", "error: " + e);
                }
            }
        });
        spbirth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                valueBirth = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}