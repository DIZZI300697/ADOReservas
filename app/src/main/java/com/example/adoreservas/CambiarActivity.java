package com.example.adoreservas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
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

    private EditText etIdCambiar;
    private Spinner spNuevoOrigen, spNuevoDestino;
    private Button btnNuevaFecha, btnNuevaHora, btnActualizar, btnRegresar;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar);

        etIdCambiar = findViewById(R.id.et_id_cambiar);
        spNuevoOrigen = findViewById(R.id.sp_nuevo_origen);
        spNuevoDestino = findViewById(R.id.sp_nuevo_destino);
        btnNuevaFecha = findViewById(R.id.btn_nueva_fecha);
        btnNuevaHora = findViewById(R.id.btn_nueva_hora);
        btnActualizar = findViewById(R.id.btn_actualizar);
        btnRegresar = findViewById(R.id.btn_regresar);

        calendar = Calendar.getInstance();

        btnNuevaFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CambiarActivity.this, dateSetListener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnNuevaHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(CambiarActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), true).show();
            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(etIdCambiar.getText().toString());
                String nuevoOrigen = spNuevoOrigen.getSelectedItem().toString();
                String nuevoDestino = spNuevoDestino.getSelectedItem().toString();
                String nuevaFecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
                String nuevaHora = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.getTime());

                DatabaseHelper db = new DatabaseHelper(CambiarActivity.this);
                Cursor cursor = db.getReserva(id);

                if (cursor != null && cursor.moveToFirst()) {
                    db.updateReserva(id, nuevoOrigen, nuevoDestino, nuevaFecha, nuevaHora);
                    Toast.makeText(CambiarActivity.this, "Reserva actualizada", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CambiarActivity.this, MenuActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(CambiarActivity.this, "No se encontró la reserva", Toast.LENGTH_SHORT).show();
                }
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

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            // Update date in the interface
        }
    };

    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            // Update time in the interface
        }
    };
}
