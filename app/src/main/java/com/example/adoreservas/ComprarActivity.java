package com.example.adoreservas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ComprarActivity extends AppCompatActivity {

    private EditText etId, etTotal;
    private Spinner spOrigen, spDestino;
    private Button btnFecha, btnHora, btnPagar, btnRegresar;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar);

        etId = findViewById(R.id.et_id);
        spOrigen = findViewById(R.id.sp_origen);
        spDestino = findViewById(R.id.sp_destino);
        btnFecha = findViewById(R.id.btn_fecha);
        btnHora = findViewById(R.id.btn_hora);
        etTotal = findViewById(R.id.et_total);
        btnPagar = findViewById(R.id.btn_pagar);
        btnRegresar = findViewById(R.id.btn_regresar);

        calendar = Calendar.getInstance();

        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ComprarActivity.this, dateSetListener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(ComprarActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), true).show();
            }
        });

        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String origen = spOrigen.getSelectedItem().toString();
                String destino = spDestino.getSelectedItem().toString();
                String fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());
                String hora = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.getTime());
                double total = Double.parseDouble(etTotal.getText().toString());

                DatabaseHelper db = new DatabaseHelper(ComprarActivity.this);
                db.addReserva(origen, destino, fecha, hora, total);

                Toast.makeText(ComprarActivity.this, "Reserva guardada", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ComprarActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComprarActivity.this, MenuActivity.class);
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
