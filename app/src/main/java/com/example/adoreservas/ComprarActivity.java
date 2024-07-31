package com.example.adoreservas;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ComprarActivity extends AppCompatActivity {

    private String usuario;
    private EditText etFecha, etHora;
    private Spinner spOrigen, spDestino;
    private Button btnFecha, btnHora, btnPagar, btnRegresar;
    private TextView tvTotal;
    private Calendar calendar;
    private String[] origenes = {"Mérida", "Progreso", "Valladolid", "Tizimín", "Ticul"};
    private String[] destinos = {"Celestún", "Izamal", "Tekax", "Motul", "Peto"};
    private double[][] precios = {
            {100, 80, 120, 70, 150},
            {110, 90, 130, 80, 160},
            {200, 180, 220, 170, 250},
            {210, 190, 230, 180, 260},
            {140, 120, 160, 110, 170}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar);

        usuario = getIntent().getStringExtra("usuario");

        etFecha = findViewById(R.id.et_fecha);
        etHora = findViewById(R.id.et_hora);
        spOrigen = findViewById(R.id.sp_origen);
        spDestino = findViewById(R.id.sp_destino);
        btnFecha = findViewById(R.id.btn_fecha);
        btnHora = findViewById(R.id.btn_hora);
        btnPagar = findViewById(R.id.btn_pagar);
        btnRegresar = findViewById(R.id.btn_regresar);
        tvTotal = findViewById(R.id.tv_total);
        calendar = Calendar.getInstance();

        ArrayAdapter<String> adapterOrigen = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, origenes);
        adapterOrigen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOrigen.setAdapter(adapterOrigen);

        ArrayAdapter<String> adapterDestino = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, destinos);
        adapterDestino.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDestino.setAdapter(adapterDestino);

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
                int origenPos = spOrigen.getSelectedItemPosition();
                int destinoPos = spDestino.getSelectedItemPosition();
                double total = precios[origenPos][destinoPos];
                String fecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.getTime());
                String hora = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.getTime());

                DatabaseHelper db = new DatabaseHelper(ComprarActivity.this);
                long id = db.addReserva(origenes[origenPos], destinos[destinoPos], fecha, hora, total);
                if (id > 0) {
                    Toast.makeText(ComprarActivity.this, "Reserva realizada con éxito", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ComprarActivity.this, MenuActivity.class);
                    intent.putExtra("usuario", usuario);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ComprarActivity.this, "Error al realizar la reserva", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComprarActivity.this, MenuActivity.class);
                intent.putExtra("usuario", usuario);
                startActivity(intent);
                finish();
            }
        });

        spOrigen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calcularTotal();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spDestino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calcularTotal();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void calcularTotal() {
        int origenPos = spOrigen.getSelectedItemPosition();
        int destinoPos = spDestino.getSelectedItemPosition();
        double total = precios[origenPos][destinoPos];
        tvTotal.setText("Total: $" + total);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            etFecha.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.getTime()));
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
