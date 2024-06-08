package com.hoc.androidlearning;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ChangePassword extends AppCompatActivity {
    DBhelperActivity dbh;
    Button btnchangeP;
    EditText ednewPassword, edPasswordOld;
    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        dbh =new DBhelperActivity(this);
        initView();
    }

    void initView(){
        edPasswordOld = findViewById(R.id.edPassOld);
        ednewPassword = findViewById(R.id.edNewPass);
        btnchangeP = findViewById(R.id.btnchangeP);

        settings = getSharedPreferences("Login", 0);
        String username = settings.getString("user", "");
        String password = settings.getString("pass", "");
        btnchangeP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordOld = edPasswordOld.getText().toString();
                String newPassword = ednewPassword.getText().toString();
                try {
                    if(passwordOld.equals(password)){
                        SQLiteDatabase db1 = dbh.ketNoiDBRead();
                        String sql = "UPDATE login SET password='"+newPassword+"' where username='"+username+"'";
                        db1.execSQL(sql);
                        Toast.makeText(getApplicationContext(), "Đổi mật khẩu thành công", Toast.LENGTH_LONG).show();

                        settings.edit().clear().commit();
                        Intent intent = new Intent(ChangePassword.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Mật khẩu cũ không đúng"+passwordOld+"-"+password, Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Log.d("Error", "onClick change error: "+e);
                }


            }
        });
    }
}