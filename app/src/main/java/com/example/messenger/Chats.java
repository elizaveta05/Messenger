package com.example.messenger;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chats extends AppCompatActivity {

    private ImageButton btn_profile, btn_add;
    private RecyclerView recyclerView;
    private ChatsAdapter adapter;
    private List<Chat> chatList;
    private FirebaseUser currentUser;
    private String senderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Разрешить использование функции EdgeToEdge
        EdgeToEdge.enable(this);

        // Установить макет для этой активности
        setContentView(R.layout.activity_chats);

        // Применить настройки краевых областей для области содержимого 'main'
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Получить текущего пользователя из Firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        Gson gson = new Gson();

        senderId= gson.toJson(currentUser.getUid().toString());

        // Настроить кнопку для перехода на профиль
        btn_profile = findViewById(R.id.btn_profile);
        btn_profile.setOnClickListener(v->{
            Intent intent = new Intent(Chats.this, Profile.class);
            startActivity(intent);
            // Убрать анимацию перехода
            overridePendingTransition(0, 0);
        });

        // Настроить кнопку для добавления чата
        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(v->{
            Intent intent = new Intent(Chats.this, add_chats.class);
            startActivity(intent);
            // Убрать анимацию перехода
            overridePendingTransition(0, 0);
        });

        // Настроить RecyclerView для отображения списка чатов
        recyclerView = findViewById(R.id.allContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatList = new ArrayList<>();

        if(senderId != null) {
            // Установить соединение по WebSocket
            connectWebSocket(senderId);
        }

    }

    // Метод для установки соединения по WebSocket
    private void connectWebSocket(String senderId) {
        RetrofitService retrofitService = new RetrofitService();
        Api api = retrofitService.getRetrofit().create(Api.class);
        api.getAllChatsForUser(senderId).enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Request successful: " + response.body().toString());
                    chatList = response.body();
                    adapter.notifyDataSetChanged();
                    setupRecyclerView();
                } else {
                    Log.e(TAG, "Request failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Chat>> call, Throwable throwable) {
                Log.e(TAG, "Request failed: " + throwable.getMessage());
            }
        });
    }
    private void setupRecyclerView() {
        adapter = new ChatsAdapter(this, chatList, chat -> {
            Intent intent = new Intent(Chats.this, PersonalChat.class);
            //intent.putExtra("selectedUser", user);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

}