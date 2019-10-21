package com.dell.mediaplayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {
    static final String DB_NAME="media";
    static final String TABLE_NAME="mediapaths";
    static final String ID="id";
    static final String PATH="paths";

    String sql="CREATE TABLE " +TABLE_NAME+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+PATH+" TEXT)";
    public SQLiteHelper(@Nullable Context context) {
        super(context, DB_NAME+"", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public Cursor readALL(){
        SQLiteDatabase sql= getReadableDatabase();
        return sql.query(TABLE_NAME,null,null,null,null,null,null);
    }
    public boolean insert(String path){
        SQLiteDatabase sql=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(PATH,path);
        long value=sql.insert(TABLE_NAME,null,contentValues);
        if(value==-1) return false;
        else return true;
    }
}
