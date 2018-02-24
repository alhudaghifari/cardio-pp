package com.urbannightdev.cardiopp.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.urbannightdev.cardiopp.model.SaranKesehatanModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ghifar on 21/02/18.
 */

public class ChatManager {

    public static final String PREFS_NAME = "CHATMESSAGEMODEL";
    public static final String CHAT = "chat_taleus";

    public ChatManager() {
        super();
    }

    // This four methods are used for maintaining Chat.
    public void saveChat(Context context, List<SaranKesehatanModel> Chat) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonChat = gson.toJson(Chat);

        editor.putString(CHAT, jsonChat);

        editor.commit();
    }

    /**
     * Use this to add chat
     * @param context
     * @param chatMessageModel
     */
    public void addChat(Context context, SaranKesehatanModel chatMessageModel) {
        List<SaranKesehatanModel> Chat = getChat(context);
        if (Chat == null)
            Chat = new ArrayList<SaranKesehatanModel>();
        Chat.add(chatMessageModel);
        saveChat(context, Chat);
    }

    /**
     * use this to remove specific chat
     * @param context
     * @param chatMessageModel
     */
    public void removeChat(Context context, SaranKesehatanModel chatMessageModel) {
        ArrayList<SaranKesehatanModel> Chat = getChat(context);
        if (Chat != null) {
            Chat.remove(chatMessageModel);
            saveChat(context, Chat);
        }
    }

    public ArrayList<SaranKesehatanModel> getChat(Context context) {
        SharedPreferences settings;
        List<SaranKesehatanModel> Chat;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(CHAT)) {
            String jsonChat = settings.getString(CHAT, null);
            Gson gson = new Gson();
            SaranKesehatanModel[] ChatItems = gson.fromJson(jsonChat,
                    SaranKesehatanModel[].class);

            Chat = Arrays.asList(ChatItems);
            Chat = new ArrayList<SaranKesehatanModel>(Chat);
        } else
            return null;

        return (ArrayList<SaranKesehatanModel>) Chat;
    }

}
