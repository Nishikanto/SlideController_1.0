package com.example.root.slidecontroller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by root on 10/1/16.
 */

public class DBhelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "slideController.db";
    private static final String TABLE_NAME = "ipadresses";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_IP = "ip";
    private static final String COLUMN_ACTIVE = "active";
    private SQLiteDatabase db;

    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table ipadresses " +
                "(id integer primary key, name text, ip text, active int)"
        );

        db.execSQL("insert into ipadresses(name, ip, active) values('Room 329','100.10.68.15',0)");
        db.execSQL("insert into ipadresses(name, ip, active) values('Room 330','100.10.68.16',0)");
        db.execSQL("insert into ipadresses(name, ip, active) values('Room 331','100.10.68.17',0)");
        db.execSQL("insert into ipadresses(name, ip, active) values('Room 332','100.10.68.18',0)");
        db.execSQL("insert into ipadresses(name, ip, active) values('Room 333','100.10.68.19',0)");
        db.execSQL("insert into ipadresses(name, ip, active) values('Room 334','192.168.2.100',0)");
        db.execSQL("insert into ipadresses(name, ip, active) values('SUST WIFI - NUSRAT','10.100.5.1',0)");
        db.execSQL("insert into ipadresses(name, ip, active) values('EMULATOR','10.0.3.2',1)");
        db.execSQL("insert into ipadresses(name, ip, active) values('New Phone','192.168.0.120',1)");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ipadresses");
        onCreate(db);
    }

    public boolean insertData(String name, String ip, int active){

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("ip", ip);
        contentValues.put("active", active);
        db.insert("ipadresses", null, contentValues);
        return true;
    }

    public boolean activateIp(int id){

        ContentValues contentValues = new ContentValues();
        contentValues.put("active", 1);
        db.update("ipadresses", contentValues, "id = ? ", new String[] {Integer.toString(id)} );
        return true;
    }

    public ArrayList<IpClass> getAllIps(){
        ArrayList<IpClass> array_list = new ArrayList<IpClass>();


        Cursor res =  db.rawQuery( "select * from ipadresses", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            IpClass ipClass = new IpClass();
            ipClass.setId(Integer.parseInt(res.getString(res.getColumnIndex(COLUMN_ID))));
            ipClass.setName(res.getString(res.getColumnIndex(COLUMN_NAME)));
            ipClass.setActive(Integer.parseInt(res.getString(res.getColumnIndex(COLUMN_ACTIVE))));
            ipClass.setIp(res.getString(res.getColumnIndex(COLUMN_IP)));
            array_list.add(ipClass);
            res.moveToNext();
        }
        return array_list;
    }

    public boolean deactivateAllIp(){

        db.execSQL("UPDATE ipadresses SET active=0");
        return true;
    }

    public String getActiveIp(){
        String activeIp = "0";
        Cursor res =  db.rawQuery( "select ip from ipadresses where active=1", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            activeIp = res.getString(res.getColumnIndex(COLUMN_IP));
            res.moveToNext();
        }
        return activeIp;
    }

}
