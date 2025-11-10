package com.example.web_soap_rpc.services;

// import com.example.currencyservice.model.Currency;
import org.springframework.stereotype.Service;

import com.example.web_soap_rpc.models.Currency;
import com.example.web_soap_rpc.repositories.CurrencyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    public Currency getCurrencyByPrefix(String prefix) {
        return currencyRepository.findCurrency(prefix)
                .orElseThrow(() -> new RuntimeException("Currency not found for Prefix: " + prefix));
    }
}
