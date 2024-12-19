package com.example.planetze;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private final List<ChatMessage> chatMessages;

    public ChatAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);
        if (chatMessage.isUser()) {
            holder.userMessageTextView.setText(chatMessage.getMessage());
            holder.userMessageTextView.setVisibility(View.VISIBLE);
            holder.botMessageTextView.setVisibility(View.GONE);
        } else {
            holder.botMessageTextView.setText(chatMessage.getMessage());
            holder.botMessageTextView.setVisibility(View.VISIBLE);
            holder.userMessageTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView userMessageTextView;
        TextView botMessageTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            userMessageTextView = itemView.findViewById(R.id.userMessageTextView);
            botMessageTextView = itemView.findViewById(R.id.botMessageTextView);
        }
    }
}
