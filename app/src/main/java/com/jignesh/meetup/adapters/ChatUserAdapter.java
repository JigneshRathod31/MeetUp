package com.jignesh.meetup.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jignesh.meetup.R;
import com.jignesh.meetup.activities.ChatMessagesActivity;
import com.jignesh.meetup.models.ChatUserModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.ViewHolder> {

    Context context;
    ArrayList<ChatUserModel> alChatUsers;

    public ChatUserAdapter(Context context, ArrayList<ChatUserModel> alChatUsers) {
        this.context = context;
        this.alChatUsers = alChatUsers;
    }

    @NonNull
    @Override
    public ChatUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_user_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatUserAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.civProfileImg.setImageResource(alChatUsers.get(position).profileImg);
        holder.tvUserName.setText(alChatUsers.get(position).name);
        holder.tvLastMsg.setText(alChatUsers.get(position).lastMsg);
        holder.tvTime.setText(alChatUsers.get(position).time);

        holder.llRootUserItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentManager fm = ((MainActivity)context).getSupportFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.replace(R.id.nav_host_fragment_activity_main, new ChatMessagesFragment());
//                ft.addToBackStack(null);
//                ft.commit();

                try {
                    String chatId = alChatUsers.get(position).chatId;
                    String lastMsg = alChatUsers.get(position).lastMsg;
                    String receiverName = alChatUsers.get(position).name;

                    Intent intent = new Intent(context, ChatMessagesActivity.class);
                    intent.putExtra("chatId", chatId);
                    intent.putExtra("lastMsg", lastMsg);
                    intent.putExtra("receiverName", receiverName);
                    context.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return alChatUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llRootUserItem;
        CircleImageView civProfileImg;
        TextView tvUserName, tvLastMsg, tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            llRootUserItem = itemView.findViewById(R.id.ll_root_user_item);
            civProfileImg = itemView.findViewById(R.id.civ_profile_img);
            tvUserName = itemView.findViewById(R.id.tv_user_name);
            tvLastMsg = itemView.findViewById(R.id.tv_last_msg);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}
