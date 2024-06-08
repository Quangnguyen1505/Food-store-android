package com.hoc.androidlearning;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBhelperActivity extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "managefood.db";
    private static final int DATABASE_VERSION = 3;
    private static final String TABLE_FOOD = "FOODS";

    public DBhelperActivity(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqltext = "CREATE TABLE FOODS(FoodID INTEGER PRIMARY KEY, FoodName TEXT, Price FLOAT, Description TEXT, foodImage text);\n"
                + "INSERT INTO FOODS VALUES(1, 'Java', 9.99, 'sách về java', '');\n"
                + "INSERT INTO FOODS VALUES(2, 'Android', 19.00, 'Android cơ bản', '');\n"
                + "INSERT INTO FOODS VALUES(3, 'Học làm giàu', 0.99, 'sách đọc cho vui', '');\n"
                + "INSERT INTO FOODS VALUES(4, 'Tử điển Anh-Việt', 29.50, 'Từ điển 100.000 từ', '');\n"
                + "INSERT INTO FOODS VALUES(5, 'CNXH', 1, 'chuyện cổ tích', '');";

        for (String sql : sqltext.split("\n")) {
            try {
                db.execSQL(sql);
            } catch (Exception e) {
                Log.e("DBHelper", "onCreate: Error executing SQL: " + sql, e);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        onCreate(db);
    }

    Cursor initRecordFirstDB() {
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cs = db.rawQuery("SELECT * FROM FOODS", null);
            if (cs != null && cs.moveToNext()) {
                return cs;
            }
        } catch (Exception e) {
            Log.e("DBHelper", "initRecordFirstDB: Exception", e);
        }
        return null;
    }

    SQLiteDatabase ketNoiDBRead() {
        return getReadableDatabase();
    }

    SQLiteDatabase ketNoiDBWrite() {
        return getWritableDatabase();
    }
}
