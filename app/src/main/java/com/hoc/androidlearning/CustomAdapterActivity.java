package com.hoc.androidlearning;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

import model.Food;

public class CustomAdapterActivity extends ArrayAdapter<Food> {
    private final Activity context;
    private final ArrayList<Food> arrfood;

    public CustomAdapterActivity(Activity context, ArrayList<Food> arrfood) {
        super(context, R.layout.activity_custom_adapter, arrfood);
        this.context = context;
        this.arrfood = arrfood;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.activity_custom_adapter, null, true);

        Food food = arrfood.get(position);

        TextView txtmamon = rowView.findViewById(R.id.txtid);
        ImageView imgbook = (ImageView) rowView.findViewById(R.id.imganhsv);
        TextView txttenmon = rowView.findViewById(R.id.txttenmon);
        TextView txtgia = rowView.findViewById(R.id.txtgia);

        txtmamon.setText(arrfood.get(position).getFoodId());
        txttenmon.setText(arrfood.get(position).getFoodname());
        txtgia.setText(""+arrfood.get(position).getFoodprice());
        //imgbook.setImageBitmap(StringToBitMap(food.getImage()));
        imgbook.setImageBitmap(StringToBitMap(arrfood.get(position).getImage()));
        Button btndetails = rowView.findViewById(R.id.btndetails);
        Button btndelete = rowView.findViewById(R.id.btndelete);


        btndetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, DetailsActivity.class);
                in.putExtra("ObjectDetails", food);
                context.startActivityForResult(in, 201);
                //context.startActivity(in);
            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogConfirm(food, position);
            }
        });

        return rowView;
    }

    private void showDialogConfirm(Food food, int position) {
        AlertDialog.Builder b = new AlertDialog.Builder(context);
        b.setTitle("Xác nhận");
        b.setMessage("Bạn có đồng ý xóa mục " + food.getFoodname() + " này không?");
        b.setPositiveButton("Đồng ý", (dialog, id) -> deleteBook(food.getFoodId(), position));
        b.setNegativeButton("Không đồng ý", (dialog, id) -> dialog.cancel());
        AlertDialog al = b.create();
        al.show();
    }
    private void deleteBook(String foodId, int position) {
        try {
            String sql = "DELETE FROM FOODS WHERE FoodID = '" + foodId + "'";
            SQLiteDatabase db = context.openOrCreateDatabase("managefood.db", MODE_PRIVATE, null);
            db.execSQL(sql);
            arrfood.remove(position); // Cập nhật danh sách
            notifyDataSetChanged(); // Thông báo cho adapter để cập nhật giao diện
            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, "Không thành công", Toast.LENGTH_LONG).show();
        }
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
