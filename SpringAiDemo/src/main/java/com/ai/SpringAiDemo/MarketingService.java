package com.ai.SpringAiDemo;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MarketingService {
    private final ChatModel chatModel;

    public MarketingService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String createMarketingCampaign(String industry, String targetAudience, String campaignType) {
        // Replace the recipe prompt with a marketing prompt
        var template = """
                I want to create a marketing campaign for the {industry} industry.
                The target audience is {targetAudience}.
                The type of campaign I need is {campaignType}.
                Please generate a detailed marketing slogan or content idea tailored for this campaign.
                """;

        PromptTemplate promptTemplate = new PromptTemplate(template);
        Map<String, Object> params = Map.of(
                "industry", industry,
                "targetAudience", targetAudience,
                "campaignType", campaignType
        );

        Prompt prompt = promptTemplate.create(params);
        return chatModel.call(prompt).getResult().getOutput().getContent();
    }
}
