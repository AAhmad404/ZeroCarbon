package com.example.zerocarbon.chat;

import androidx.annotation.NonNull;

import com.example.zerocarbon.BuildConfig;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.BlockThreshold;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.ai.client.generativeai.type.GenerationConfig;
import com.google.ai.client.generativeai.type.HarmCategory;
import com.google.ai.client.generativeai.type.SafetySetting;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * A class representing the EcoAgent, an AI-powered generative model for handling natural language queries.
 */
public class EcoAgent {

    /**
     * Callback interface to handle the results or errors from the agent's response generation.
     */
    public interface Callback {
        void onSuccess(String response);

        void onError(Throwable throwable);
    }

    private GenerativeModel agent;
    private final List<ChatMessage> history;

    /**
     * Constructs an instance of EcoAgent with predefined configuration for the generative model.
     */
    public EcoAgent() {
        GenerationConfig.Builder configBuilder = new GenerationConfig.Builder();
        configBuilder.temperature = 0.2f;
        configBuilder.topK = 10;
        configBuilder.topP = 0.95f;
        configBuilder.maxOutputTokens = 2048;

        // Define safety settings to filter harmful content
        ArrayList<SafetySetting> safetySettings = new ArrayList<>();
        safetySettings.add(new SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH));
        safetySettings.add(new SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.ONLY_HIGH));
        safetySettings.add(new SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.ONLY_HIGH));
        safetySettings.add(new SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.ONLY_HIGH));

        // Initialize the generative model with the configuration and safety settings
        this.agent = new GenerativeModel(
                "gemini-1.5-flash-001",
                BuildConfig.geminiApiKey,
                configBuilder.build(),
                safetySettings
        );

        this.history = new LinkedList<>();
    }

    /**
     * Stores a new chat message in the history, ensuring that the history doesn't exceed 10
     * messages.
     *
     * @param text   The content of the chat message to be stored.
     * @param isUser A boolean indicating whether the message was sent by the user (true)
     *               or the system (false).
     */
    private void storeHistory(String text, boolean isUser) {
        // Add the new message to the history
        if (history.size() >= 10) {
            history.remove(0); // Remove the oldest message if history exceeds 10 messages
        }
        history.add(new ChatMessage(text, isUser));
    }

    /**
     * Constructs a formatted string representing the conversation history for use in context creation.
     * The context includes the series of messages exchanged between the user and the agent,
     * with each message labeled as either "User" or "Agent".
     *
     * @return A {@link Content} object containing the formatted conversation history.
     */
    private Content constructContext() {
        StringBuilder contextBuilder = new StringBuilder();
        for (ChatMessage message : history) {
            // Separate the messages based on the sender (user or agent)
            if (message.isUser()) {
                contextBuilder.append("User:\n");
            } else {
                contextBuilder.append("Agent:\n");
            }
            contextBuilder.append(message.getMessage()).append("\n");
        }
        return new Content.Builder().addText(contextBuilder.toString()).build();
    }

    /**
     * Asynchronously processes a user query and generates a response using the EcoAgent.
     *
     * @param text     The input query or text to send to the agent.
     * @param callback A callback to handle the success or failure of the response generation.
     */
    public void askAgent(String text, Callback callback) {
        GenerativeModelFutures model = GenerativeModelFutures.from(agent);

        storeHistory(text, true);

        Executor executor = Executors.newSingleThreadExecutor();

        Content context = constructContext();
        ListenableFuture<GenerateContentResponse> response = model.generateContent(context);

        // Add a callback to handle the response asynchronously
        Futures.addCallback(response, new FutureCallback<>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();
                storeHistory(resultText, false);
                callback.onSuccess(resultText);
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                t.printStackTrace();
                callback.onError(t);
            }
        }, executor);
    }
}
