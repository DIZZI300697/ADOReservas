package com.example.adoreservas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CambiarActivity extends AppCompatActivity {

    private EditText etId, etFecha, etHora;
    private Spinner spOrigen, spDestino;
    private Button btnBuscar, btnFecha, btnHora, btnActualizar, btnRegresar;
    private Calendar calendar;
    private String[] origenes = {"Mérida", "Progreso", "Valladolid", "Tizimín", "Ticul"};
    private String[] destinos = {"Celestún", "Izamal", "Tekax", "Motul", "Peto"};
    private int reservaId = -1;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar);

        etId = findViewById(R.id.et_id);
        etFecha = findViewById(R.id.et_fecha);
        etHora = findViewById(R.id.et_hora);
        spOrigen = findViewById(R.id.sp_origen);
        spDestino = findViewById(R.id.sp_destino);
        btnBuscar = findViewById(R.id.btn_buscar);
        btnFecha = findViewById(R.id.btn_fecha);
        btnHora = findViewById(R.id.btn_hora);
        btnActualizar = findViewById(R.id.btn_actualizar);
        btnRegresar = findViewById(R.id.btn_regresar);
        calendar = Calendar.getInstance();
        dbHelper = new DatabaseHelper(this);

        ArrayAdapter<String> adapterOrigen = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, origenes);
        adapterOrigen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOrigen.setAdapter(adapterOrigen);

        ArrayAdapter<String> adapterDestino = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, destinos);
        adapterDestino.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDestino.setAdapter(adapterDestino);

        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CambiarActivity.this, dateSetListener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(CambiarActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), true).show();
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarReserva();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarReserva();
            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CambiarActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void buscarReserva() {
        String idStr = etId.getText().toString().trim();
        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingrese una ID válida", Toast.LENGTH_SHORT).show();
            return;
        }

        reservaId = Integer.parseInt(idStr);
        Cursor cursor = dbHelper.getReserva(reservaId);

        if (cursor != null && cursor.moveToFirst()) {
            String origen = cursor.getString(cursor.getColumnIndex("origen"));
            String destino = cursor.getString(cursor.getColumnIndex("destino"));
            String fecha = cursor.getString(cursor.getColumnIndex("fecha"));
            String hora = cursor.getString(cursor.getColumnIndex("hora"));

            spOrigen.setSelection(getIndex(spOrigen, origen));
            spDestino.setSelection(getIndex(spDestino, destino));
            etFecha.setText(fecha);
            etHora.setText(hora);
            cursor.close();
        } else {
            Toast.makeText(this, "Reserva no encontrada", Toast.LENGTH_SHORT).show();
        }
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    private void actualizarReserva() {
        if (reservaId == -1) {
            Toast.makeText(this, "Busque una reserva primero", Toast.LENGTH_SHORT).show();
            return;
        }

        String origen = spOrigen.getSelectedItem().toString();
        String destino = spDestino.getSelectedItem().toString();
        String fecha = etFecha.getText().toString();
        String hora = etHora.getText().toString();

        if (fecha.isEmpty() || hora.isEmpty()) {
            Toast.makeText(this, "Ingrese fecha y hora válidas", Toast.LENGTH_SHORT).show();
            return;
        }

        int result = dbHelper.updateReserva(reservaId, origen, destino, fecha, hora);
        if (result > 0) {
            Toast.makeText(this, "Reserva actualizada con éxito", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CambiarActivity.this, MenuActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error al actualizar la reserva", Toast.LENGTH_SHORT).show();
        }
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            etFecha.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime()));
        }
    };

    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            etHora.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.getTime()));
        }
    };
}
