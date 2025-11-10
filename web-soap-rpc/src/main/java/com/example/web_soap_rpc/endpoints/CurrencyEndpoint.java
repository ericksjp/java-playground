package com.example.web_soap_rpc.endpoints;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.example.web_soap_rpc.gen.currencies.GetCurrencyByPrefixRequest;
import com.example.web_soap_rpc.gen.currencies.GetCurrencyByPrefixResponse;
import com.example.web_soap_rpc.gen.currencies.WsCurrency;
import com.example.web_soap_rpc.models.Currency;
import com.example.web_soap_rpc.services.CurrencyService;

import lombok.RequiredArgsConstructor;

@Endpoint
@RequiredArgsConstructor
public class CurrencyEndpoint {
    private static final String NAMESPACE_URI = "http://example.com/web-soap-rpc/gen/currencies";
    private final CurrencyService currencyService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getCurrencyByPrefixRequest")
    @ResponsePayload
    public GetCurrencyByPrefixResponse getCurrencyByPrefix(@RequestPayload GetCurrencyByPrefixRequest request) {
        Currency currency = currencyService.getCurrencyByPrefix(request.getPrefix());
        GetCurrencyByPrefixResponse response = new GetCurrencyByPrefixResponse();
        response.setCurrency(mapToWsCurrency(currency));
        return response;
    }

    private WsCurrency mapToWsCurrency(Currency currency) {
        WsCurrency wsCurrency = new WsCurrency();
        wsCurrency.setPrefix(currency.getPrefix());
        wsCurrency.setName(currency.getName());
        return wsCurrency;
    }
}
