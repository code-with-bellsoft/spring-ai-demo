package dev.cyberjar.backendspring.service;

import dev.cyberjar.backendspring.utils.CurrencyConverter;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AiChatService {

    private final ChatClient chatClient;

    private final String CHAT_PROMPT = """
                        Be concise. Answer in <=3 sentences unless asked otherwise.
                        If the user asks to convert money, extract amount/from/to and call the tool 'convertCurrency'.
                                - After the tool response, reply with one line like: "<amount> <FROM> = <converted> <TO>".
                                - Supported currencies: USD and EUR only.
                                - If currencies are unsupported or missing, ask a concise clarification.
            """;

    public AiChatService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public Flux<String> reply(String userMessage) {
        return chatClient
                .prompt()
                .system(CHAT_PROMPT)
                .user(userMessage)
                .tools(new CurrencyConverter())
                .stream()
                .content();
    }

}