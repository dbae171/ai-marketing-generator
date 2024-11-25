package com.ai.SpringAiDemo;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ChatService {
    private final ChatModel chatModel;
    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 2000; // 2 seconds

    public ChatService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String getResponse(String prompt) {
        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            try {
                OpenAiChatOptions options = OpenAiChatOptions.builder()
                        .withModel("gpt-4o")
                        .build();
                ChatResponse response = chatModel.call(new Prompt(prompt, options));
                return response.getResult().getOutput().getContent();
            } catch (Exception e) {
                if (e.getMessage().contains("insufficient_quota")) {
                    logger.error("Quota exceeded: ", e);
                    return "Sorry, you have exceeded your API quota. Please check your plan or try again later.";
                } else if (e.getMessage().contains("Too Many Requests")) {
                    logger.warn("Rate limit exceeded, retrying... attempt {}", attempt + 1);
                    attempt++;
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException interruptedException) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    logger.error("Error while calling the AI model: ", e);
                    return "Sorry, an error occurred while processing your request.";
                }
            }
        }
        return "Sorry, we were unable to process your request after several attempts. Please try again later.";
    }

    public String getResponseOptions(String prompt) {
        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            try {
                ChatResponse response = chatModel.call(
                        new Prompt(
                                prompt,
                                OpenAiChatOptions.builder()
                                        .withModel("gpt-4o")
                                        .withTemperature(0.4F)
                                        .build()
                        ));
                return response.getResult().getOutput().getContent();
            } catch (Exception e) {
                if (e.getMessage().contains("insufficient_quota")) {
                    logger.error("Quota exceeded: ", e);
                    return "Sorry, you have exceeded your API quota. Please check your plan or try again later.";
                } else if (e.getMessage().contains("Too Many Requests")) {
                    logger.warn("Rate limit exceeded, retrying... attempt {}", attempt + 1);
                    attempt++;
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException interruptedException) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    logger.error("Error while calling the AI model: ", e);
                    return "Sorry, an error occurred while processing your request.";
                }
            }
        }
        return "Sorry, we were unable to process your request after several attempts. Please try again later.";
    }
}
