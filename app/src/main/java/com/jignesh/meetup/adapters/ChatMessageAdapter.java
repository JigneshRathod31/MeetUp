package com.jignesh.meetup.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jignesh.meetup.R;
import com.jignesh.meetup.models.ChatMessageModel;
import com.jignesh.meetup.utilities.Constants;

import java.util.ArrayList;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_SENT = 1;
    public static final int MSG_TYPE_RECEIVED = 0;

    Context context;
    ArrayList<ChatMessageModel> alChatMessageModel;

    public ChatMessageAdapter(Context context, ArrayList<ChatMessageModel> alChatMessageModel) {
        this.context = context;
        this.alChatMessageModel = alChatMessageModel;
    }

    @NonNull
    @Override
    public ChatMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_SENT){
            view = LayoutInflater.from(context).inflate(R.layout.sent_message_item_layout, parent, false);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.received_message_item_layout, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageAdapter.ViewHolder holder, int position) {
        holder.tvMessage.setText(alChatMessageModel.get(position).message);
        holder.tvTime.setText(alChatMessageModel.get(position).time);
    }

    @Override
    public int getItemCount() {
        return alChatMessageModel.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (alChatMessageModel.get(position).senderId.equals(Constants.USER_ID)){
            return MSG_TYPE_SENT;
        }else {
            return MSG_TYPE_RECEIVED;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvMessage, tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMessage = itemView.findViewById(R.id.tv_message);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}
