package com.example.zerocarbon.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zerocarbon.R;

import java.util.List;

/**
 * Adapter class for managing and displaying a list of chat messages in a RecyclerView.
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private final List<ChatMessage> chatMessages;

    /**
     * Constructs a new ChatAdapter.
     *
     * @param chatMessages The list of {@link ChatMessage} objects to be displayed in the RecyclerView.
     */
    public ChatAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    /**
     * Inflates the layout for a single chat message item and creates a ViewHolder for it.
     *
     * @param parent   The parent ViewGroup into which the new view will be added.
     * @param viewType The type of the new view (not used here, as all items are the same type).
     * @return A new instance of {@link ChatViewHolder}.
     */
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    /**
     * Binds the data for a specific position in the RecyclerView to the provided ViewHolder.
     *
     * @param holder   The {@link ChatViewHolder} to bind data to.
     * @param position The position of the item in the data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);
        if (chatMessage.isUser()) {
            // If the message is from the user, show the user message view and hide the bot message view
            holder.userMessageTextView.setText(chatMessage.getMessage());
            holder.userMessageTextView.setVisibility(View.VISIBLE);
            holder.botMessageTextView.setVisibility(View.GONE);
        } else {
            // If the message is from the bot, show the bot message view and hide the user message view
            holder.botMessageTextView.setText(chatMessage.getMessage());
            holder.botMessageTextView.setVisibility(View.VISIBLE);
            holder.userMessageTextView.setVisibility(View.GONE);
        }
    }

    /**
     * Returns the total number of chat messages.
     *
     * @return The size of the {@link #chatMessages} list.
     */
    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    /**
     * ViewHolder class for holding the views for individual chat messages.
     */
    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView userMessageTextView;
        TextView botMessageTextView;

        /**
         * Constructs a new ChatViewHolder.
         *
         * @param itemView The view representing a single chat message item.
         */
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            userMessageTextView = itemView.findViewById(R.id.userMessageTextView);
            botMessageTextView = itemView.findViewById(R.id.botMessageTextView);
        }
    }
}
