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

import com.example.planetze.chat.ChatAdapter;
import com.example.planetze.chat.ChatMessage;
import com.example.planetze.chat.EcoAgent;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EcoAgentFragment extends Fragment {

    private RecyclerView chatRecyclerView;
    private EditText chatInputEditText;
    private MaterialButton sendButton;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;

    private EcoAgent ecoAgent;

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

        ecoAgent = new EcoAgent();

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

        // Send the message to bot for a real response
        simulateBotResponse(message);
    }

    private void simulateBotResponse(String userMessage) {
        ecoAgent.askAgent(userMessage, new EcoAgent.Callback() {
            @Override
            public void onSuccess(String response) {
                // Ensure the UI updates happen on the main thread
                requireActivity().runOnUiThread(() -> {
                    // This method is called when a response is successfully received from Gemini
                    chatMessages.add(new ChatMessage(response, false));
                    chatAdapter.notifyItemInserted(chatMessages.size() - 1);

                    // Scroll to the latest message
                    chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
                });
            }

            @Override
            public void onError(Throwable throwable) {
                // Ensure the UI update (Toast) happens on the main thread
                requireActivity().runOnUiThread(() -> {
                    // Handle any errors that occur during the request
                    Toast.makeText(getContext(), "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
