package com.example.adoreservas;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CancelarActivity extends AppCompatActivity {

    private EditText etIdCancelar;
    private Button btnCancelar, btnRegresar;
    private TextView tvDetalles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelar);

        etIdCancelar = findViewById(R.id.et_id_cancelar);
        btnCancelar = findViewById(R.id.btn_cancelar);
        btnRegresar = findViewById(R.id.btn_regresar);
        tvDetalles = findViewById(R.id.tv_detalles);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int id = Integer.parseInt(etIdCancelar.getText().toString());
                DatabaseHelper db = new DatabaseHelper(CancelarActivity.this);
                Cursor cursor = db.getReserva(id);

                if (cursor != null && cursor.moveToFirst()) {
                    String origen = cursor.getString(cursor.getColumnIndexOrThrow("origen"));
                    String destino = cursor.getString(cursor.getColumnIndexOrThrow("destino"));
                    String fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"));
                    String hora = cursor.getString(cursor.getColumnIndexOrThrow("hora"));
                    double total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));

                    String detalles = "ID: " + id + "\nOrigen: " + origen + "\nDestino: " + destino +
                            "\nFecha: " + fecha + "\nHora: " + hora + "\nTotal: " + total;
                    tvDetalles.setText(detalles);

                    new AlertDialog.Builder(CancelarActivity.this)
                            .setTitle("Confirmar cancelación")
                            .setMessage("¿Estás seguro de que quieres cancelar este viaje?")
                            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.deleteReserva(id);
                                    Toast.makeText(CancelarActivity.this, "Reserva cancelada", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CancelarActivity.this, MenuActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                } else {
                    Toast.makeText(CancelarActivity.this, "No se encontró la reserva", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CancelarActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
