package dev.cyberjar.backendspring.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AiTranslationService {

    private final ChatClient chatClient;

    private final String TRANSLATION_PROMPT = """
                You are a professional translator.
                - Translate the user's text to %s.
                - Preserve meaning, tone, names, and formatting when sensible.
                - Return ONLY the translated text, no preface, no quotes
            """;


    public AiTranslationService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public Flux<String> translate(String targetLanguage, String text) {

        String system = TRANSLATION_PROMPT.formatted(targetLanguage);

        return chatClient
                .prompt()
                .system(system)
                .user(text)
                .stream()
                .content();
    }

}
