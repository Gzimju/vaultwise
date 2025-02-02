package com.vaultwise.service;

import com.vaultwise.model.Card;
import com.vaultwise.model.User;
import com.vaultwise.repository.CardRepository;
import com.vaultwise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    // Create a new card
    public Card createCard(Card card) {
        return cardRepository.save(card);
    }

    // Get all cards
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    // Get a card by ID
    public Optional<Card> getCardById(Long id) {
        return cardRepository.findById(id);
    }

    // Update an existing card
    public Card updateCard(Long id, Card updatedCard) {
        if (cardRepository.existsById(id)) {
            updatedCard.setId(id);
            return cardRepository.save(updatedCard);
        }
        return null;
    }

    // Delete a card
    public void deleteCard(Long id) {
        cardRepository.deleteById(id);
    }
}
