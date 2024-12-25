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
    }

    /**
     * Asynchronously processes a user query and generates a response using the EcoAgent.
     *
     * @param text     The input query or text to send to the agent.
     * @param callback A callback to handle the success or failure of the response generation.
     */
    public void askAgent(String text, Callback callback) {
        GenerativeModelFutures model = GenerativeModelFutures.from(agent);

        // Build the content to be processed by the model
        Content content = new Content.Builder()
                .addText(text)
                .build();
        Executor executor = Executors.newSingleThreadExecutor();

        // Use a single-threaded executor for handling asynchronous callbacks
        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

        // Add a callback to handle the response asynchronously
        Futures.addCallback(response, new FutureCallback<>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();
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
