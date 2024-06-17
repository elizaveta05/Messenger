package com.example.messenger;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PersonalChat extends AppCompatActivity {
    private Users selectedUser;
    private EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_personal_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        selectedUser = getIntent().getParcelableExtra("selectedUser");
        etMessage = findViewById(R.id.et_messege);

        if (selectedUser != null) {
            TextView tvName = findViewById(R.id.tv_name);
            tvName.setText(selectedUser.getLogin());
        }

        ImageButton btnSend = findViewById(R.id.btn_send);
        btnSend.setOnClickListener(v -> {
            String message = etMessage.getText().toString().trim();
        });
    }
}