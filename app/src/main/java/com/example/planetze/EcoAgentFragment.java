package com.example.planetze;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class EcoAgentFragment extends Fragment {

    private RecyclerView chatRecyclerView;
    private EditText chatInputEditText;
    private MaterialButton sendButton;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eco_agent, container, false);

        // Initialize UI components
        chatRecyclerView = view.findViewById(R.id.chatRecyclerView);
        chatInputEditText = view.findViewById(R.id.chatInputEditText);
        sendButton = view.findViewById(R.id.sendButton);

        // Initialize chat messages list and adapter
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatMessages);

        // Set up RecyclerView
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatRecyclerView.setAdapter(chatAdapter);

        // Set up Send Button click listener
        sendButton.setOnClickListener(v -> sendMessage());

        return view;
    }

    private void sendMessage() {
        // Get the message from the input box
        String message = chatInputEditText.getText().toString().trim();

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(getContext(), "Ask me something", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add the user's message to the chat list
        chatMessages.add(new ChatMessage(message, true));
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);

        // Clear the input box
        chatInputEditText.setText("");

        // Simulate bot response (replace this with real chatbot logic)
        simulateBotResponse(message);
    }

    private void simulateBotResponse(String userMessage) {
        // Simple bot response logic
        String botResponse = "You said: " + userMessage;
        chatMessages.add(new ChatMessage(botResponse, false));
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);

        // Scroll to the latest message
        chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
    }
}
