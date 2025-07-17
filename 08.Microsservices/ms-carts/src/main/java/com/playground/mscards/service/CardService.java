package com.playground.mscards.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.playground.mscards.entity.Card;
import com.playground.mscards.repository.CardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;

    public Card save(Card cart) {
        return cardRepository.save(cart);
    }

    public List<Card> getByIncomeGreatherThane(Double value) {
        return cardRepository.findByIncomeGreaterThanEqual(value);
    }
    
}
