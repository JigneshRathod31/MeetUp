package com.jignesh.meetup.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jignesh.meetup.R;
import com.jignesh.meetup.adapters.ChatMessageAdapter;
import com.jignesh.meetup.models.ChatMessageModel;
import com.jignesh.meetup.utilities.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessagesActivity extends AppCompatActivity {

    RecyclerView rvChatMessage;
    ArrayList<ChatMessageModel> alChatMessageModel;
    ChatMessageAdapter chatMessageAdapter;

    EditText etMessage;
    ImageButton ibSendMessage;

    String chatId, lastMsg;
    FirebaseFirestore db;

    Toolbar toolbar;
    ImageView ivBackArrow;
    CircleImageView civProfileImg;
    TextView tvReceiverName;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_messages);

        rvChatMessage = findViewById(R.id.rv_chat_message);
        etMessage = findViewById(R.id.et_message);
        ibSendMessage = findViewById(R.id.ib_send_message);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivBackArrow = findViewById(R.id.iv_back_arrow);
        civProfileImg = findViewById(R.id.civ_profile_img);
        tvReceiverName = findViewById(R.id.tv_receiver_name);

        setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();

        rvChatMessage.setLayoutManager(new LinearLayoutManager(this));
        alChatMessageModel = new ArrayList<>();
        chatMessageAdapter = new ChatMessageAdapter(this, alChatMessageModel);
        rvChatMessage.setAdapter(chatMessageAdapter);

        try {
            chatId = getIntent().getStringExtra("chatId");
            lastMsg = getIntent().getStringExtra("lastMsg");
            tvReceiverName.setText(getIntent().getStringExtra("receiverName"));
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ibSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = etMessage.getText().toString().trim();

                Date currentDate = new Date();

                String time = Constants.TIME_FORMATTER.format(currentDate);
                String date = Constants.DATE_FORMATTER.format(currentDate);

                if (!message.isEmpty()){
                    alChatMessageModel.add(new ChatMessageModel(Constants.USER_ID, message, time, date));
                    chatMessageAdapter.notifyDataSetChanged();
                    etMessage.setText("");
                    rvChatMessage.scrollToPosition(alChatMessageModel.size()-1);
                    sendMsg(message);
                }
            }
        });

        if (!lastMsg.trim().isEmpty()){
            getChatMessages();
        }
    }

    private void getChatMessages(){
        db.collection("chats")
            .document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null){
                        Toast.makeText(ChatMessagesActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    alChatMessageModel.clear();
                    chatMessageAdapter.notifyDataSetChanged();

                    for (QueryDocumentSnapshot document: value){
                        String senderId = document.get("userId").toString();
                        String message = document.get("message").toString();
                        String time = document.get("time").toString();
                        String date = document.get("date").toString();

                        alChatMessageModel.add(new ChatMessageModel(senderId, message, time, date));
                    }
                    chatMessageAdapter.notifyDataSetChanged();
                    rvChatMessage.scrollToPosition(alChatMessageModel.size() - 1);
                }
            });
    }

    private void sendMsg(String msg){
        Date date = new Date();

        HashMap<String, Object> message = new HashMap<>();
        message.put("message", msg);
        message.put("userId", Constants.USER_ID);
        message.put("time", Constants.TIME_FORMATTER.format(date));
        message.put("date", Constants.DATE_FORMATTER.format(date));
        message.put("timestamp", String.valueOf(Calendar.getInstance().getTimeInMillis()));

        HashMap<String, Object> lastMsg = new HashMap<>();
        lastMsg.put("lastMsg", msg);
        lastMsg.put("lastMsgTime", Constants.TIME_FORMATTER.format(date));

        try {
            db.collection("chats")
                .document(chatId)
                .collection("messages")
                .add(message)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            db.collection("chats")
                                .document(chatId)
                                .update(lastMsg)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
//                                            Toast.makeText(ChatMessagesActivity.this, "Message sent successfully!", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(ChatMessagesActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        }else {
                            Toast.makeText(ChatMessagesActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}