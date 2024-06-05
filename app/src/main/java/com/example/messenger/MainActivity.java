package com.example.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button btn_authorization, btn_registration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_authorization = findViewById(R.id.btn_authorization);
        btn_authorization.setOnClickListener(v->{
            Intent intent = new Intent(this, Authorization.class);
            startActivity(intent);
        });

        btn_registration = findViewById(R.id.btn_registration);
        btn_registration.setOnClickListener(v->{
            Intent intent = new Intent(this, Registration.class);
            startActivity(intent);
        });
    }
}