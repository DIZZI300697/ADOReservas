package com.example.adoreservas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private Button btnVender, btnConsultar, btnCambiar, btnCancelar, btnSalir;
    private TextView tvUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnVender = findViewById(R.id.btn_comprar);
        btnConsultar = findViewById(R.id.btn_consultar);
        btnCambiar = findViewById(R.id.btn_cambiar);
        btnCancelar = findViewById(R.id.btn_cancelar);
        btnSalir = findViewById(R.id.btn_salir);
        tvUsuario = findViewById(R.id.tv_usuario);

        String usuario = getIntent().getStringExtra("usuario");
        tvUsuario.setText("Hola, " + usuario);

        btnVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ComprarActivity.class);
                startActivity(intent);
            }
        });

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ConsultarActivity.class);
                startActivity(intent);
            }
        });

        btnCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, CambiarActivity.class);
                startActivity(intent);
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, CancelarActivity.class);
                startActivity(intent);
            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
