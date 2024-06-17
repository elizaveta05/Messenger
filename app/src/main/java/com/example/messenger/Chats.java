
package com.example.messenger;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Chats extends AppCompatActivity {

    private ImageButton btn_profile, btn_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chats);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_profile = findViewById(R.id.btn_profile);
        btn_profile.setOnClickListener(v->{
            Intent intent = new Intent(Chats.this, Profile.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // Убрать анимацию перехода
        });

        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(v->{
            Intent intent = new Intent(Chats.this, add_chats.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // Убрать анимацию перехода
        });
    }
}