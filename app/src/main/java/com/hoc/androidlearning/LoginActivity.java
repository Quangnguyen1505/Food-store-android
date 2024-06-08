package com.hoc.androidlearning;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {
    EditText edUsername, edPassword;
    CheckBox Remember;
    Button Signin;

    TextView Signup;

    DBhelperActivity dbh;
    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        dbh =new DBhelperActivity(this);
        initView();
        settings = getSharedPreferences("Login", 0);
        String username = settings.getString("user", "");
        String password = settings.getString("pass", "");
        if(!username.isEmpty()){
            Intent in = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(in);
        }
    }

    void initView(){
        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        Remember = findViewById(R.id.cbRmb);
        Signin = findViewById(R.id.btnSignin);
        Signup = findViewById(R.id.btnSignup);

        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String u=edUsername.getText().toString();
                    String p=edPassword.getText().toString();
                    String sql="select * from login where username='"+u+"' and password='"+p+"'";
                    SQLiteDatabase db=dbh.ketNoiDBRead();
                    Cursor cs = db.rawQuery(sql, null);
                    if(cs.moveToNext()){
                        if(Remember.isChecked()){
                            settings.edit().putString("user", u).apply();
                            settings.edit().putString("pass", p).apply();
                        }
                        Intent in = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(in);
                    }else{
                        Toast.makeText(getApplicationContext(),"Tài khoản chưa đúng", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"loi"+e, Toast.LENGTH_LONG).show();
                }

            }
        });

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(in);
            }
        });
    }
}