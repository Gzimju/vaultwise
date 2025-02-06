package com.vaultwise.service;

import com.vaultwise.model.Card;
import com.vaultwise.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
        when(cardRepository.save(any(Card.class))).thenReturn(card);

        Card createdCard = cardService.createCard(card);

        assertNotNull(createdCard);
        assertEquals("1234567890123456", createdCard.getCardNumber());
        assertEquals("VISA", createdCard.getCardType());
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    void testGetCardById() {
        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));

        Optional<Card> foundCard = cardService.getCardById(cardId);

        assertTrue(foundCard.isPresent());
        assertEquals(cardId, foundCard.get().getId());
        verify(cardRepository, times(1)).findById(cardId);
    }

    @Test
    void testGetAllCards() {
        List<Card> cardList = new ArrayList<>();
        cardList.add(card);
        when(cardRepository.findAll()).thenReturn(cardList);

        List<Card> allCards = cardService.getAllCards();

        assertNotNull(allCards);
        assertEquals(1, allCards.size());
        assertEquals("1234567890123456", allCards.get(0).getCardNumber());
        verify(cardRepository, times(1)).findAll();
    }

    @Test
    void testUpdateCard() {
        Card updatedCard = new Card();
        updatedCard.setCardNumber("9876543210987654");
        updatedCard.setCardType("MasterCard");
        updatedCard.setExpirationDate(LocalDate.of(2028, 6, 30));
        updatedCard.setSecurityCode("456");
        updatedCard.setCardHolderName("Jane Doe");

        when(cardRepository.existsById(cardId)).thenReturn(true);
        when(cardRepository.save(any(Card.class))).thenReturn(updatedCard);

        Card result = cardService.updateCard(cardId, updatedCard);

        assertNotNull(result);
        assertEquals("9876543210987654", result.getCardNumber());
        assertEquals("MasterCard", result.getCardType());
        assertEquals(LocalDate.of(2028, 6, 30), result.getExpirationDate());
        assertEquals("456", result.getSecurityCode());
        assertEquals("Jane Doe", result.getCardHolderName());

        verify(cardRepository, times(1)).save(updatedCard);
    }

    @Test
    void testDeleteCard() {
        doNothing().when(cardRepository).deleteById(cardId);

        cardService.deleteCard(cardId);

        verify(cardRepository, times(1)).deleteById(cardId);
    }
}
