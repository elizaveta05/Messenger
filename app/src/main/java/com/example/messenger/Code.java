package com.example.messenger;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Code extends AppCompatActivity {

    EditText number1, number2, number3, number4, number5, number6;
    TextView tv_back;
    String phoneNumber;
    Button btn_autho;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_code);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tv_back.setOnClickListener(v -> {
            finish();
        });
        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
        number3 = findViewById(R.id.number3);
        number4 = findViewById(R.id.number4);
        number5 = findViewById(R.id.number5);
        number6 = findViewById(R.id.number6);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            phoneNumber = extras.getString("phoneNumber");
        } else {
            Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
            finish();
        }
        number1.requestFocus();
        setEditTextAutoAdvance(number1, number2);
        setEditTextAutoAdvance(number2, number3);
        setEditTextAutoAdvance(number3, number4);
        setEditTextAutoAdvance(number4, number5);
        setEditTextAutoAdvance(number5, number6);

        btn_autho = findViewById(R.id.btn_autho);
        btn_autho.setOnClickListener(v -> {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,
                    60L,
                    TimeUnit.SECONDS,
                    this,
                    mCallbacks
            );

        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // Обработка ошибок верификации номера телефона
                Toast.makeText(Code.this, "Ошибка верификации номера телефона: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Некорректный формат номера телефона
                    Toast.makeText(Code.this, "Некорректный формат номера телефона", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // Превышение лимита запросов на верификацию
                    Toast.makeText(Code.this, "Превышен лимит запросов на верификацию. Попробуйте позже", Toast.LENGTH_SHORT).show();
                } else {
                    // Другие типы ошибок
                    Toast.makeText(Code.this, "Произошла ошибка верификации номера телефона", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(@NonNull String verification, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                Toast.makeText(Code.this, "Код верификации отправлен", Toast.LENGTH_SHORT).show();
                btn_autho.setOnClickListener(v -> {
                    String inputCode = number1.getText().toString() + number2.getText().toString() + number3.getText().toString() +
                            number4.getText().toString() + number5.getText().toString() + number6.getText().toString();

                    if (TextUtils.isEmpty(inputCode)) {
                        Toast.makeText(Code.this, "Введите полученный код", Toast.LENGTH_SHORT).show();
                    } else {
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification, inputCode);
                        signInWithPhoneAuthCredential(credential);
                    }
                });
            }
        };
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(Code.this, "Аутентификация успешна", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Не удалось получить информацию о текущем пользователе", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Ошибка аутентификации с помощью SMS", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void setEditTextAutoAdvance(final EditText currentEditText, final EditText nextEditText) {
        currentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    nextEditText.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}