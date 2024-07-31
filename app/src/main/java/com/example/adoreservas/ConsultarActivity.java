package com.example.adoreservas;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConsultarActivity extends AppCompatActivity {

    private EditText etIdConsultar;
    private Button btnConsultar, btnConsultarTodo, btnRegresar;
    private TextView tvResultados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar);

        etIdConsultar = findViewById(R.id.et_id_consultar);
        btnConsultar = findViewById(R.id.btn_consultar);
        btnConsultarTodo = findViewById(R.id.btn_consultar_todo);
        btnRegresar = findViewById(R.id.btn_regresar);
        tvResultados = findViewById(R.id.tv_resultados);

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idText = etIdConsultar.getText().toString();
                if (idText.isEmpty()) {
                    Toast.makeText(ConsultarActivity.this, "Proporcione ID", Toast.LENGTH_SHORT).show();
                    return;
                }

                int id = Integer.parseInt(idText);
                DatabaseHelper db = new DatabaseHelper(ConsultarActivity.this);
                Cursor cursor = db.getReserva(id);

                if (cursor != null && cursor.moveToFirst()) {
                    String origen = cursor.getString(cursor.getColumnIndexOrThrow("origen"));
                    String destino = cursor.getString(cursor.getColumnIndexOrThrow("destino"));
                    String fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"));
                    String hora = cursor.getString(cursor.getColumnIndexOrThrow("hora"));
                    double total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));

                    String resultado = "ID: " + id + "\nOrigen: " + origen + "\nDestino: " + destino +
                            "\nFecha: " + fecha + "\nHora: " + hora + "\nTotal: " + total;
                    tvResultados.setText(resultado);
                } else {
                    Toast.makeText(ConsultarActivity.this, "No se encontró la reserva", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnConsultarTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper(ConsultarActivity.this);
                Cursor cursor = db.getAllReservas();
                StringBuilder resultados = new StringBuilder();

                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    String origen = cursor.getString(cursor.getColumnIndexOrThrow("origen"));
                    String destino = cursor.getString(cursor.getColumnIndexOrThrow("destino"));
                    String fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"));
                    String hora = cursor.getString(cursor.getColumnIndexOrThrow("hora"));
                    double total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));

                    resultados.append("ID: ").append(id).append("\nOrigen: ").append(origen)
                            .append("\nDestino: ").append(destino).append("\nFecha: ").append(fecha)
                            .append("\nHora: ").append(hora).append("\nTotal: ").append(total).append("\n\n");
                }

                tvResultados.setText(resultados.toString());
            }
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConsultarActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
