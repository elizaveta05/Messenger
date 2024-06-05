package com.example.messenger;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
public class Authorization extends AppCompatActivity {

    private Spinner spinner;
    private EditText et_number, et_phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_authorization);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spinner = findViewById(R.id.spinner);
        et_number = findViewById(R.id.et_number);
        et_phone = findViewById(R.id.et_phone);

        List<String> countries = new ArrayList<>();
        countries.add("Выберите страну");
        countries.add("Россия");
        countries.add("США");
        countries.add("Китай");
        countries.add("Бразилия");
        countries.add("Германия");
        countries.add("Индия");
        countries.add("Австралия");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCountry = countries.get(position);
                String countryCode = getCountryCode(selectedCountry);
                et_number.setText("+" + countryCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        Button btn_autho = findViewById(R.id.btn_autho);
        btn_autho.setOnClickListener(v -> {
            if (spinner.getSelectedItemPosition() != 0) {
                if (et_number != null && et_phone != null) {
                    String selectedCountry = spinner.getSelectedItem().toString();
                    String countryCode = getCountryCode(selectedCountry);

                    String phone = et_phone.getText().toString().replaceAll("[^\\d]", ""); // Оставляем только цифры
                    if (phone.length() == 10) {
                        String phone_number = "+" + countryCode + phone;


                    } else {
                        Toast.makeText(getApplicationContext(), "Номер телефона должен содержать 10 цифр", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Заполните поле номера телефона", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Выберите страну", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getCountryCode(String country) {
        switch (country) {
            case "Россия":
                return "7";
            case "США":
                return "1";
            case "Китай":
                return "86";
            case "Бразилия":
                return "55";
            case "Германия":
                return "49";
            case "Индия":
                return "91";
            case "Австралия":
                return "61";
            default:
                return "";
        }
    }
}