package com.github.taurvi.valor.config;

public class BaseConfig implements ValorConfig {
    @Override
    public String getBotToken() {
        return "";
    }

    @Override
    public String getMongoClientAddress() {
        return "";
    }

    @Override
    public int getMongoClientPort() {
        return 0;
    }

    @Override
    public String getMongoDatabase() {
        return "";
    }

}
