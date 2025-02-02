package com.vaultwise.service;

import com.vaultwise.model.Card;
import com.vaultwise.model.User;
import com.vaultwise.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardService cardService;

    private Card card;
    private Long cardId;

    @BeforeEach
    void setUp() {
        // Initialize a test card before each test
        cardId = 1L;
        card = new Card();
        card.setId(cardId);
        card.setCardNumber("1234567890123456");
        card.setCardType("VISA");
        card.setExpirationDate(LocalDate.of(2026, 12, 31));
        card.setSecurityCode("123");
        card.setCardHolderName("John Doe");
    }

    @Test
    void testCreateCard() {
        // Mock the repository save method
        when(cardRepository.save(any(Card.class))).thenReturn(card);

        // Call the service method
        Card createdCard = cardService.createCard(card);

        // Assertions
        assertNotNull(createdCard);
        assertEquals("1234567890123456", createdCard.getCardNumber());
        assertEquals("VISA", createdCard.getCardType());
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    void testGetCardById() {
        // Mock the repository findById method
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        // Call the service method
        Optional<Card> foundCard = cardService.getCardById(cardId);

        // Assertions
        assertTrue(foundCard.isPresent());
        assertEquals(cardId, foundCard.get().getId());
        verify(cardRepository, times(1)).findById(cardId);
    }

    @Test
    void testUpdateCard() {
        // Mock the repository to simulate card creation
        Card card = new Card();
        card.setCardNumber("1234567812345678");
        card.setCardType("Visa");
        card.setExpirationDate(LocalDate.of(2025, 12, 31));
        card.setSecurityCode("123");
        card.setCardHolderName("John Doe");

        // Mock saving the card
        when(cardRepository.save(any(Card.class))).thenReturn(card);
        card = cardService.createCard(card);  // Create the card first

        // Now simulate the update by changing the card details
        Card updatedCard = new Card();
        updatedCard.setCardNumber("9876543210987654");
        updatedCard.setCardType("MasterCard");
        updatedCard.setExpirationDate(LocalDate.of(2028, 6, 30));
        updatedCard.setSecurityCode("456");
        updatedCard.setCardHolderName("Jane Doe");

        // Mock card existence check and update
        when(cardRepository.existsById(card.getId())).thenReturn(true);
        when(cardRepository.save(any(Card.class))).thenReturn(updatedCard);

        // Call the service to update the card
        Card result = cardService.updateCard(card.getId(), updatedCard);

        // Assertions
        assertNotNull(result, "Updated card should not be null");
        assertEquals("9876543210987654", result.getCardNumber(), "Card number should be updated");
        assertEquals("MasterCard", result.getCardType(), "Card type should be updated");
        assertEquals(LocalDate.of(2028, 6, 30), result.getExpirationDate(), "Expiration date should be updated");
        assertEquals("456", result.getSecurityCode(), "Security code should be updated");
        assertEquals("Jane Doe", result.getCardHolderName(), "Card holder name should be updated");

        // Verify that save was called once with the updated card
        verify(cardRepository, times(1)).save(updatedCard);
    }

    @Test
    void testDeleteCard() {
        // Mock the repository deleteById method
        doNothing().when(cardRepository).deleteById(cardId);

        // Call the service method
        cardService.deleteCard(cardId);

        // Assertions
        verify(cardRepository, times(1)).deleteById(cardId);
    }
}
