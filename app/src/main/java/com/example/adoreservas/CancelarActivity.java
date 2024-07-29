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

    private EditText etId;
    private TextView tvDetails;
    private Button btnBuscar, btnCancelar, btnRegresar;
    private DatabaseHelper dbHelper;
    private int reservaId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelar);

        etId = findViewById(R.id.et_id);
        tvDetails = findViewById(R.id.tv_details);
        btnBuscar = findViewById(R.id.btn_buscar);
        btnCancelar = findViewById(R.id.btn_cancelar);
        btnRegresar = findViewById(R.id.btn_regresar);
        dbHelper = new DatabaseHelper(this);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarReserva();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarCancelacion();
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
            double total = cursor.getDouble(cursor.getColumnIndex("total"));

            String details = "Origen: " + origen + "\nDestino: " + destino + "\nFecha: " + fecha + "\nHora: " + hora + "\nTotal: " + total;
            tvDetails.setText(details);
            tvDetails.setVisibility(View.VISIBLE);
            cursor.close();
        } else {
            Toast.makeText(this, "Reserva no encontrada", Toast.LENGTH_SHORT).show();
            tvDetails.setText("");
            tvDetails.setVisibility(View.GONE);
        }
    }

    private void confirmarCancelacion() {
        if (reservaId == -1) {
            Toast.makeText(this, "Busque una reserva primero", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Cancelar Reserva")
                .setMessage("¿Estás seguro de que deseas cancelar esta reserva?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        cancelarReserva();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void cancelarReserva() {
        int result = dbHelper.deleteReserva(reservaId);
        if (result > 0) {
            Toast.makeText(this, "Reserva cancelada con éxito", Toast.LENGTH_SHORT).show();
            tvDetails.setText("");
            tvDetails.setVisibility(View.GONE);
            reservaId = -1;
        } else {
            Toast.makeText(this, "Error al cancelar la reserva", Toast.LENGTH_SHORT).show();
        }
    }
}
