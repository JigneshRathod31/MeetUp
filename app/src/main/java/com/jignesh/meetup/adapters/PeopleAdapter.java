package com.jignesh.meetup.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jignesh.meetup.R;
import com.jignesh.meetup.activities.ChatMessagesActivity;
import com.jignesh.meetup.models.PeopleModel;
import com.jignesh.meetup.utilities.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {

    public static final int PERFECT_MATCH = 1;
    public static final int SPORT_MATCH = 2;
    public static final int OTHER_MATCH = 3;

    Context context;
    ArrayList<PeopleModel> alPeople;
    int matchType;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PeopleAdapter(Context context, ArrayList<PeopleModel> alPeople, int matchType) {
        this.context = context;
        this.alPeople = alPeople;
        this.matchType = matchType;
    }

    @NonNull
    @Override
    public PeopleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (matchType == PERFECT_MATCH){
            view = LayoutInflater.from(context).inflate(R.layout.people_perfect_match_item_layout, parent, false);
        }else if(matchType == SPORT_MATCH){
            view = LayoutInflater.from(context).inflate(R.layout.people_sport_match_item_layout, parent, false);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.people_other_item_layout, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            if (matchType == PERFECT_MATCH){
                holder.btnMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String chatId = addChat(position);

                        Intent intent = new Intent(context, ChatMessagesActivity.class);
                        intent.putExtra("chatId", chatId);
                        intent.putExtra("lastMsg", "");
                        intent.putExtra("receiverName", alPeople.get(position).name);
                        context.startActivity(intent);
                    }
                });
            }

            holder.civProfileImg.setImageResource(alPeople.get(position).profileImg);
            holder.tvUserName.setText(alPeople.get(position).name);
            holder.tvUserInterest.setText(alPeople.get(position).languages.toString());

            holder.llRootUserItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String chatId = addChat(position);

                    Intent intent = new Intent(context, ChatMessagesActivity.class);
                    intent.putExtra("chatId", chatId);
                    intent.putExtra("lastMsg", "");
                    intent.putExtra("receiverName", alPeople.get(position).name);
                    context.startActivity(intent);
                }
            });
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return alPeople.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llRootUserItem;
        CircleImageView civProfileImg;
        TextView tvUserName;
        TextView tvUserInterest;
        Button btnMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            llRootUserItem = itemView.findViewById(R.id.ll_root_user_item);
            civProfileImg = itemView.findViewById(R.id.iv_profile_img);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvUserInterest = itemView.findViewById(R.id.tv_user_interests);

            if (matchType == PERFECT_MATCH){
                btnMessage = itemView.findViewById(R.id.btn_message);
            }
        }
    }

    public String addChat(int position){
        String[] ids = {Constants.USER_ID, alPeople.get(position).uid};
        Arrays.sort(ids);
        String chatId = ids[0] + "-" + ids[1];

        HashMap<String, Object> chat = new HashMap<>();
        chat.put("chatId", chatId);
        chat.put("ids", Arrays.asList(Constants.USER_ID, alPeople.get(position).uid));
        chat.put("lastMsg", "");
        chat.put("lastMsgTime", "");
        chat.put("receiverAvatar", R.drawable.avatar_two);
        chat.put("receiverName", alPeople.get(position).name);
        chat.put("receiverId", alPeople.get(position).uid);
        chat.put("senderAvatar", R.drawable.avatar_four);
        chat.put("senderName", "Jignesh Rathod");
        chat.put("senderId", Constants.USER_ID);

        db.collection("chats")
            .document(chatId)
            .set(chat)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        alPeople.remove(position);
                        notifyDataSetChanged();
                    }else {
                        Toast.makeText(context, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        return chatId;
    }
}
