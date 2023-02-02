package com.example.petswalking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;


    public DBOpenHelper(Context context){
        super(context,"db_pets_walking",null,1);
        db = getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS user_info(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT," +
                "password TEXT," +
                "email TEXT," +
                "phone TEXT," +
                "create_time DATETIME," +
                "update_time DATETIME)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS user_info");
        onCreate(db);
    }

    public Boolean insertData(String username,String password,String email,String phone,String create_time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("email", email);
        contentValues.put("phone", phone);
        contentValues.put("create_time", create_time);

        long result = db.insert("user_info", null, contentValues);

        if(result == -1){
            return false;
        }else {
            return true;
        }
    }

    /*
        This method used to check the username.
     */
    public Boolean checkUsername(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from user_info where username = ?", new String[] {username});

        if(cursor.getCount() > 0){
            return true;
        }else {
            return false;
        }
    }

    /*
        This method used to check the password.
     */
    public Boolean checkUsernamePassword(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from user_info where username = ? " +
                "and password = ?", new String[] {username, password});

        if(cursor.getCount() > 0){
            return true;
        }else {
            return false;
        }
    }

    public void delete(String username,String password){
        db.execSQL("DELETE FROM user_info WHERE username = AND password ="+username+password);
    }

    public Boolean updatePassword(String username, String password, String update_time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("password", password);
        contentValues.put("update_time", update_time);

        long result = db.update("user_info", contentValues, "username = ?", new String[]{username});

        if(result == -1){
            return false;
        }else {
            return true;
        }
    }

    public void updateEmail(String email){
        db.execSQL("UPDATE user_info SET email = ?, update_time = time",new Object[]{email});
    }

    public void updatePhone(String phone){
        db.execSQL("UPDATE user_info SET phone = ?, update_time = time",new Object[]{phone});
    }
}
