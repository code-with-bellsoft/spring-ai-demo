package dev.cyberjar.backendspring.utils;

import org.springframework.ai.tool.annotation.Tool;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyConverter {

    // Hardcoded demo rates
    private final double EUR_TO_USD = 1.08;
    private final double USD_TO_EUR = 0.93;


    @Tool(description = """
            If user asks for currency conversion, convert money between USD and EUR using a fixed demo rate.
            Arguments: amount (number), from ('USD'|'EUR'), to ('USD'|'EUR').
                                - After the tool response, reply with one line like: "<amount> <FROM> = <converted> <TO>".
                                - Supported currencies: USD and EUR only.
                                - If currencies are unsupported or missing, ask a concise clarification.
            """)
    public BigDecimal convert(BigDecimal amount, String from, String to) {
        if (amount == null || amount.signum() < 0) {
            throw new IllegalArgumentException("Amount must be non-negative");
        }

        double a = amount.doubleValue();
        double rate = pickRate(from, to);
        double result = a * rate;

        // round to 2 decimals and return BigDecimal
        return BigDecimal.valueOf(result).setScale(2, RoundingMode.HALF_UP);
    }

    private double pickRate(String from, String to) {
        String f = from.toUpperCase();
        String t = to.toUpperCase();
        if (f.equals("EUR") && t.equals("USD")) return EUR_TO_USD;
        if (f.equals("USD") && t.equals("EUR")) return USD_TO_EUR;
        throw new IllegalArgumentException("Unsupported pair: " + from + "->" + to);
    }

}