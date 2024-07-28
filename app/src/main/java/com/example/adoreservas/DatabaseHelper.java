package com.example.adoreservas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "reservas.db";
    private static final int DATABASE_VERSION = 1;

    // Tablas y columnas
    private static final String TABLE_ORIGENES = "origenes";
    private static final String COLUMN_ORIGEN_ID = "id";
    private static final String COLUMN_ORIGEN_NAME = "nombre";

    private static final String TABLE_DESTINOS = "destinos";
    private static final String COLUMN_DESTINO_ID = "id";
    private static final String COLUMN_DESTINO_NAME = "nombre";

    private static final String TABLE_PRECIOS = "precios";
    private static final String COLUMN_PRECIO_ID = "id";
    private static final String COLUMN_PRECIO_ORIGEN_ID = "origen_id";
    private static final String COLUMN_PRECIO_DESTINO_ID = "destino_id";
    private static final String COLUMN_PRECIO_TOTAL = "total";

    private static final String TABLE_RESERVAS = "reservas";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ORIGEN = "origen";
    private static final String COLUMN_DESTINO = "destino";
    private static final String COLUMN_FECHA = "fecha";
    private static final String COLUMN_HORA = "hora";
    private static final String COLUMN_TOTAL = "total";

    // Creación de tablas
    private static final String CREATE_TABLE_ORIGENES =
            "CREATE TABLE " + TABLE_ORIGENES + " (" +
                    COLUMN_ORIGEN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ORIGEN_NAME + " TEXT);";

    private static final String CREATE_TABLE_DESTINOS =
            "CREATE TABLE " + TABLE_DESTINOS + " (" +
                    COLUMN_DESTINO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DESTINO_NAME + " TEXT);";

    private static final String CREATE_TABLE_PRECIOS =
            "CREATE TABLE " + TABLE_PRECIOS + " (" +
                    COLUMN_PRECIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_PRECIO_ORIGEN_ID + " INTEGER, " +
                    COLUMN_PRECIO_DESTINO_ID + " INTEGER, " +
                    COLUMN_PRECIO_TOTAL + " REAL, " +
                    "FOREIGN KEY(" + COLUMN_PRECIO_ORIGEN_ID + ") REFERENCES " + TABLE_ORIGENES + "(" + COLUMN_ORIGEN_ID + "), " +
                    "FOREIGN KEY(" + COLUMN_PRECIO_DESTINO_ID + ") REFERENCES " + TABLE_DESTINOS + "(" + COLUMN_DESTINO_ID + "));";

    private static final String CREATE_TABLE_RESERVAS =
            "CREATE TABLE " + TABLE_RESERVAS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ORIGEN + " TEXT, " +
                    COLUMN_DESTINO + " TEXT, " +
                    COLUMN_FECHA + " TEXT, " +
                    COLUMN_HORA + " TEXT, " +
                    COLUMN_TOTAL + " REAL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ORIGENES);
        db.execSQL(CREATE_TABLE_DESTINOS);
        db.execSQL(CREATE_TABLE_PRECIOS);
        db.execSQL(CREATE_TABLE_RESERVAS);

        // Insertar datos iniciales
        insertInitialData(db);
    }

    private void insertInitialData(SQLiteDatabase db) {
        // Datos de orígenes
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORIGEN_NAME, "Mérida");
        db.insert(TABLE_ORIGENES, null, values);
        values.put(COLUMN_ORIGEN_NAME, "Progreso");
        db.insert(TABLE_ORIGENES, null, values);
        values.put(COLUMN_ORIGEN_NAME, "Valladolid");
        db.insert(TABLE_ORIGENES, null, values);
        values.put(COLUMN_ORIGEN_NAME, "Tizimin");
        db.insert(TABLE_ORIGENES, null, values);
        values.put(COLUMN_ORIGEN_NAME, "Chicxulub");
        db.insert(TABLE_ORIGENES, null, values);

        // Datos de destinos
        values.clear();
        values.put(COLUMN_DESTINO_NAME, "Cancún");
        db.insert(TABLE_DESTINOS, null, values);
        values.put(COLUMN_DESTINO_NAME, "Playa del Carmen");
        db.insert(TABLE_DESTINOS, null, values);
        values.put(COLUMN_DESTINO_NAME, "Tulum");
        db.insert(TABLE_DESTINOS, null, values);
        values.put(COLUMN_DESTINO_NAME, "Chetumal");
        db.insert(TABLE_DESTINOS, null, values);
        values.put(COLUMN_DESTINO_NAME, "Holbox");
        db.insert(TABLE_DESTINOS, null, values);

        // Datos de precios
        values.clear();
        values.put(COLUMN_PRECIO_ORIGEN_ID, 1); // Mérida
        values.put(COLUMN_PRECIO_DESTINO_ID, 1); // Cancún
        values.put(COLUMN_PRECIO_TOTAL, 1200);
        db.insert(TABLE_PRECIOS, null, values);

        values.put(COLUMN_PRECIO_ORIGEN_ID, 1); // Mérida
        values.put(COLUMN_PRECIO_DESTINO_ID, 2); // Playa del Carmen
        values.put(COLUMN_PRECIO_TOTAL, 1400);
        db.insert(TABLE_PRECIOS, null, values);

        // Agrega el resto de los datos de precios...
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRECIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DESTINOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORIGENES);
        onCreate(db);
    }

    // Métodos para manejar datos
    public Cursor getAllOrigenes() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_ORIGENES, null, null, null, null, null, null);
    }

    public Cursor getAllDestinos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_DESTINOS, null, null, null, null, null, null);
    }

    public Cursor getPrecio(int origenId, int destinoId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PRECIOS, null, COLUMN_PRECIO_ORIGEN_ID + "=? AND " + COLUMN_PRECIO_DESTINO_ID + "=?",
                new String[]{String.valueOf(origenId), String.valueOf(destinoId)}, null, null, null);
    }

    // Métodos para agregar, consultar, actualizar y eliminar reservas
    public long addReserva(String origen, String destino, String fecha, String hora, double total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORIGEN, origen);
        values.put(COLUMN_DESTINO, destino);
        values.put(COLUMN_FECHA, fecha);
        values.put(COLUMN_HORA, hora);
        values.put(COLUMN_TOTAL, total);
        return db.insert(TABLE_RESERVAS, null, values);
    }

    public Cursor getReserva(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_RESERVAS, null, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
    }

    public Cursor getAllReservas() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_RESERVAS, null, null, null, null, null, null);
    }

    public int updateReserva(int id, String origen, String destino, String fecha, String hora) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORIGEN, origen);
        values.put(COLUMN_DESTINO, destino);
        values.put(COLUMN_FECHA, fecha);
        values.put(COLUMN_HORA, hora);
        return db.update(TABLE_RESERVAS, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteReserva(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RESERVAS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }
}
