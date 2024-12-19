package utilities;

public class GeminiApiClient {
    private String apiKey;

    public GeminiApiClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public String sendMessage(String message) throws Exception {



        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            // Process and return the response body
            return response.body().string();

        } else {
            throw new Exception("Gemini API request failed: " + response.code());
        }
    }
}
