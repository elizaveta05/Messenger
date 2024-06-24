package com.example.messenger;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messenger.reotrfit.Api;
import com.example.messenger.reotrfit.RetrofitService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalChat extends AppCompatActivity {
    private Users selectedUser;
    private EditText etMessage;
    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private FirebaseUser currentUser;

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

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        selectedUser = getIntent().getParcelableExtra("selectedUser");
        etMessage = findViewById(R.id.et_messege);
        recyclerView = findViewById(R.id.recycler_view);

        if (selectedUser != null) {
            TextView tvName = findViewById(R.id.tv_name);
            tvName.setText(selectedUser.getLogin());
        }

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, currentUser.getUid());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageButton btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(v -> {

        });

        ImageButton btnSend = findViewById(R.id.btn_send);
        btnSend.setOnClickListener(v -> {
            String messageContent = etMessage.getText().toString().trim();
            if (!messageContent.isEmpty()) {
                sendMessageToServer(messageContent, selectedUser.getUserId());  // Отправка сообщения на сервер
                etMessage.setText(""); // Очищаем поле ввода после отправки
            }
        });

        connectWebSocket();
    }
    // Метод для установки соединения по WebSocket
    private void connectWebSocket() {
        RetrofitService retrofitService = new RetrofitService();
        Api api = retrofitService.getRetrofit().create(Api.class);
        api.getAllMessage(currentUser.getUid(), selectedUser.getUserId()).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful()) {
                    messageList.clear();
                    messageList.addAll(response.body());
                    messageAdapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "Request failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable throwable) {
                Log.e(TAG, "Request failed: " + throwable.getMessage());
            }
        });
    }
    private void sendMessageToServer(String message, String recipientId) {
        // Создание экземпляра RetrofitService
        RetrofitService retrofitService = new RetrofitService();
        // Получение экземпляра Api через Retrofit
        Api api = retrofitService.getRetrofit().create(Api.class);
        // Отправка сообщения на сервер с использованием Retrofit
        api.sendMessage(currentUser.getUid(), recipientId, message).enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                // Обработка успешного ответа от сервера и наличия данных в теле ответа
                if (response.isSuccessful() && response.body() != null) {
                    // Получение отправленного сообщения из ответа сервера (предполагая, что сервер возвращает отправленное сообщение)
                    Message sentMessage = response.body().get(0);
                    // Добавление отправленного сообщения в messageList
                    messageList.add(sentMessage);
                    // Уведомление адаптера о добавлении нового сообщения
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    // Плавная прокрутка к новому сообщению в RecyclerView
                    recyclerView.smoothScrollToPosition(messageList.size() - 1);
                } else {
                    // Логирование сообщения об ошибке в случае неуспешного ответа от сервера
                    Log.e(TAG, "Не удалось выполнить запрос: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable throwable) {
                // Логирование сообщения об ошибке в случае сбоя запроса
                Log.e(TAG, "Не удалось выполнить запрос: " + throwable.getMessage());
            }
        });
    }
}
