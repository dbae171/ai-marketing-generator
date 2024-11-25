package com.ai.SpringAiDemo;

import org.springframework.ai.image.ImageResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class GenAIController {

    private final ChatService chatService;
    private final ImageService imageService;
    private final MarketingService marketingService;

    // Constructor with correct services
    public GenAIController(ChatService chatService, ImageService imageService, MarketingService marketingService) {
        this.chatService = chatService;
        this.imageService = imageService;
        this.marketingService = marketingService; // Corrected service
    }

    // Chat API
    @GetMapping("ask-ai")
    public String getResponse(@RequestParam String prompt){
        return chatService.getResponse(prompt);
    }

    @GetMapping("ask-ai-options")
    public String getResponseOptions(@RequestParam String prompt){
        return chatService.getResponseOptions(prompt);
    }

    // Image Generation API
    @GetMapping("generate-image")
    public List<String> generateImages(
            @RequestParam String prompt,
            @RequestParam(defaultValue = "hd") String quality,
            @RequestParam(defaultValue = "1") int n,
            @RequestParam(defaultValue = "1024") int width,
            @RequestParam(defaultValue = "1024") int height) throws IOException {

        ImageResponse imageResponse = imageService.generateImage(prompt, quality, n, width, height);

        // Streams to get URLs from ImageResponse
        return imageResponse.getResults().stream()
                .map(result -> result.getOutput().getUrl())
                .toList();
    }

    // Marketing Campaign Generation API
    @GetMapping("marketing-campaign")
    public String createMarketingCampaign(
            @RequestParam String industry,
            @RequestParam String targetAudience,
            @RequestParam(defaultValue = "any") String campaignType) {

        return marketingService.createMarketingCampaign(industry, targetAudience, campaignType);
    }
}
