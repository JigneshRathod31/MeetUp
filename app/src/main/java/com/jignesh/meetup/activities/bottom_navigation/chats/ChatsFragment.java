package com.jignesh.meetup.activities.bottom_navigation.chats;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jignesh.meetup.R;
import com.jignesh.meetup.adapters.ChatUserAdapter;
import com.jignesh.meetup.databinding.FragmentChatsBinding;
import com.jignesh.meetup.models.ChatUserModel;
import com.jignesh.meetup.utilities.Constants;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatsFragment extends Fragment {

    RecyclerView rvChatUser;
    ArrayList<ChatUserModel> alChatUsers;
    ChatUserAdapter chatUserAdapter;

    ProgressBar progressBar;
    LinearLayout llEmptyChats;

    FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chats, container, false);

        rvChatUser = root.findViewById(R.id.rv_chat_user);
        progressBar = root.findViewById(R.id.progress_bar);
        llEmptyChats = root.findViewById(R.id.ll_empty_chats);

        db = FirebaseFirestore.getInstance();

        // Set Up Chats RecyclerView
        alChatUsers = new ArrayList<>();
        rvChatUser.setLayoutManager(new LinearLayoutManager(requireContext()));
        chatUserAdapter = new ChatUserAdapter(requireContext(), alChatUsers);
        rvChatUser.setAdapter(chatUserAdapter);

        loading(true);
        getChats();

//        isEmptyChats();

        return root;
    }

    private void getChats(){
        alChatUsers.clear();
        try {
            db.collection("chats")
                .whereArrayContains("ids", Constants.USER_ID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document: task.getResult()){
//                            int avatar = Integer.parseInt(document.get("receiverAvatar").toString());
                                try {
                                    String chatId = document.get("chatId").toString();
                                    int avatar = R.drawable.avatar_four;
                                    String receiverName = document.get("receiverName").toString();
                                    String senderName = document.get("senderName").toString();
                                    String senderId = document.get("senderId").toString();
                                    String lastMsg = document.get("lastMsg").toString();
                                    String time = document.get("lastMsgTime").toString();

                                    String name = senderName;
                                    if (senderId.equals(Constants.USER_ID)){
                                        name = receiverName;
                                    }

                                    alChatUsers.add(new ChatUserModel(chatId, avatar, name, lastMsg, time));
                                } catch (Exception e) {
                                    Toast.makeText(requireContext(), "ErrS: "+e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            chatUserAdapter.notifyDataSetChanged();
//                            isEmptyChats();
                            loading(false);
                        }else {
                            Toast.makeText(requireContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        } catch (Exception e) {
            Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loading(boolean isLoading){
        if (isLoading){
            progressBar.setVisibility(View.VISIBLE);
            rvChatUser.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            rvChatUser.setVisibility(View.VISIBLE);
        }
    }

    private void isEmptyChats(){
        if (alChatUsers.size() <= 0){
            llEmptyChats.setVisibility(View.VISIBLE);
        }else {
            llEmptyChats.setVisibility(View.GONE);
        }
    }
}