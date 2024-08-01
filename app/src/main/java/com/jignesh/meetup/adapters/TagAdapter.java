package com.jignesh.meetup.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jignesh.meetup.R;

import java.util.ArrayList;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    Context context;
    ArrayList<String> alTag;

    public TagAdapter(Context context, ArrayList<String> alTag) {
        this.context = context;
        this.alTag = alTag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tag_item_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvTagName.setText(alTag.get(position));

        holder.ivClearTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alTag.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return alTag.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTagName;
        ImageView ivClearTag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTagName = itemView.findViewById(R.id.tv_tag_name);
            ivClearTag = itemView.findViewById(R.id.iv_clear_tag);
        }
    }
}
