package com.example.web_soap_rpc.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.web_soap_rpc.models.Currency;

import jakarta.annotation.PostConstruct;

@Component
public class CurrencyRepository {

    private static final Map<String, Currency> currencies = new HashMap<>();

    @PostConstruct
    public void initData() {
        currencies.put("USD", new Currency("USD", "US Dollar"));
        currencies.put("EUR", new Currency("EUR", "Euro"));
        currencies.put("BRL", new Currency("BRL", "Brazilian Real"));
        currencies.put("GBP", new Currency("GBP", "British Pound"));
        currencies.put("JPY", new Currency("JPY", "Japanese Yen"));
    }

    public Optional<Currency> findCurrency(String currencyPrefix) {
        return Optional.ofNullable(currencies.get(currencyPrefix.toUpperCase()));
    }
}
