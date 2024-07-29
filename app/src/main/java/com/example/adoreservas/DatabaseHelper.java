package com.example.adoreservas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reservas.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_RESERVAS = "reservas";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ORIGEN = "origen";
    private static final String COLUMN_DESTINO = "destino";
    private static final String COLUMN_FECHA = "fecha";
    private static final String COLUMN_HORA = "hora";
    private static final String COLUMN_TOTAL = "total";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_RESERVAS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ORIGEN + " TEXT, " +
                COLUMN_DESTINO + " TEXT, " +
                COLUMN_FECHA + " TEXT, " +
                COLUMN_HORA + " TEXT, " +
                COLUMN_TOTAL + " REAL)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVAS);
        onCreate(db);
    }

    public long addReserva(String origen, String destino, String fecha, String hora, double total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORIGEN, origen);
        values.put(COLUMN_DESTINO, destino);
        values.put(COLUMN_FECHA, fecha);
        values.put(COLUMN_HORA, hora);
        values.put(COLUMN_TOTAL, total);

        long id = db.insert(TABLE_RESERVAS, null, values);
        db.close();
        return id;
    }

    public Cursor getReserva(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RESERVAS, null, COLUMN_ID + "=?", new String[]{String.valueOf(id)},
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getAllReservas() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RESERVAS, null);
    }

    public int updateReserva(int id, String origen, String destino, String fecha, String hora) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORIGEN, origen);
        values.put(COLUMN_DESTINO, destino);
        values.put(COLUMN_FECHA, fecha);
        values.put(COLUMN_HORA, hora);

        int result = db.update(TABLE_RESERVAS, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result;
    }

    public int deleteReserva(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_RESERVAS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result;
    }
}
