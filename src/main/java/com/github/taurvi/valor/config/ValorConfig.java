package com.github.taurvi.valor.config;

public interface ValorConfig {
    String getBotToken();

    String getMongoClientAddress();

    int getMongoClientPort();

    String getMongoDatabase();
}
