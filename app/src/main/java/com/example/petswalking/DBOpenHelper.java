package com.example.petswalking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
    /**
     * 声明一个AndroidSDK自带的数据库变量db
     */
    private SQLiteDatabase db;

    /**
     * 写一个这个类的构造函数，参数为上下文context，所谓上下文就是这个类所在包的路径
     * 指明上下文，数据库名，工厂默认空值，版本号默认从1开始
     * super(context,"db_pets_walking",null,1);
     * 把数据库设置成可写入状态，除非内存已满，那时候会自动设置为只读模式
     * 不过，以现如今的内存容量，估计一辈子也见不到几次内存占满的状态
     * db = getReadableDatabase();
     * @param context
     */
    public DBOpenHelper(Context context){
        super(context,"db_pets_walking",null,1);
        db = getReadableDatabase();
    }

    /**
     * 重写两个必须要重写的方法，因为class DBOpenHelper extends SQLiteOpenHelper
     * 而这两个方法是 abstract 类 SQLiteOpenHelper 中声明的 abstract 方法
     * 所以必须在子类 DBOpenHelper 中重写 abstract 方法
     * 想想也是，为啥规定这么死必须重写？
     * 因为，一个数据库表，首先是要被创建的，然后免不了是要进行增删改操作的
     * 所以就有onCreate()、onUpgrade()两个方法
     * @param db
     */
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
    /**
     * 接下来写自定义的增删改查方法
     * 这些方法，写在这里归写在这里，以后不一定都用
     * add()
     * delete()
     * update()
     * getAllData()
     */
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
