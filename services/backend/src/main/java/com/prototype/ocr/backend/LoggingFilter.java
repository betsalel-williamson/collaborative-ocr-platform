package com.prototype.ocr.backend;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.time.Instant;

final class LoggingFilter extends Filter {
    private final String name;

    LoggingFilter(String name) {
        this.name = name;
    }

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        Instant start = Instant.now();
        try {
            chain.doFilter(exchange);
        } finally {
            System.out.printf(
                    "[%s] %s %s -> %d (%d ms)%n",
                    name,
                    exchange.getRequestMethod(),
                    exchange.getRequestURI(),
                    exchange.getResponseCode(),
                    java.time.Duration.between(start, Instant.now()).toMillis());
        }
    }

    @Override
    public String description() {
        return "Console logging filter";
    }
}

