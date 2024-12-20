package com.example.planetze.chat;

import com.example.planetze.BuildConfig;
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

public class Gemini {
    private GenerativeModel gemini;

    public Gemini() {
        GenerationConfig.Builder configBuilder = new GenerationConfig.Builder();
        configBuilder.temperature = 0.15f;
        configBuilder.topK = 32;
        configBuilder.topP = 1f;
        configBuilder.maxOutputTokens = 2048;

        ArrayList<SafetySetting> safetySettings = new ArrayList<>();
        safetySettings.add(new SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH));
        safetySettings.add(new SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.ONLY_HIGH));
        safetySettings.add(new SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.ONLY_HIGH));
        safetySettings.add(new SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.ONLY_HIGH));

        this.gemini = new GenerativeModel(
                "gemini-1.5-flash-001",
                BuildConfig.geminiApiKey,
                configBuilder.build(),
                safetySettings
        );
    }

    public void askGemini(String text, Callback callback) {
        GenerativeModelFutures model = GenerativeModelFutures.from(gemini);

        Content content = new Content.Builder()
                .addText(text)
                .build();
        Executor executor = Executors.newSingleThreadExecutor();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

        Futures.addCallback(response, new FutureCallback<>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String resultText = result.getText();
                callback.onSuccess(resultText);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                callback.onError(t);
            }
        }, executor);
    }

    public interface Callback {
        void onSuccess(String response);
        void onError(Throwable throwable);
    }
}
