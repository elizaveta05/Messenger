package com.example.messenger.reotrfit;

import com.example.messenger.Chat;
import com.example.messenger.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface Api {
    @GET("/getAllChatsForUser/{senderId}")
    Call<List<Chat>> getAllChatsForUser(@Path("senderId") String senderId);

    @POST("/chat/{senderId}/{recipientId}/{message}")
    Call<List<Message>> sendMessage(@Path("senderId") String senderId, @Path("recipientId") String recipientId, @Body String message);

    @GET("/fetchAllMessage/{senderId}/{recipientId}")
    Call<List<Message>> getAllMessage(@Path("senderId") String senderId, @Path("recipientId") String recipientId);
}