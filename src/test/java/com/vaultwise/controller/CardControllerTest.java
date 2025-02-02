package com.vaultwise.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vaultwise.dto.CardRequest;
import com.vaultwise.model.Card;
import com.vaultwise.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CardControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardController cardController;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cardController).build();
    }

    @Test
    void testCreateCard() throws Exception {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setCardNumber("1234567890123456");
        cardRequest.setCardType("VISA");
        cardRequest.setExpirationDate(LocalDate.of(2026, 12, 31));
        cardRequest.setSecurityCode("123");
        cardRequest.setCardHolderName("John Doe");

        Card card = new Card();
        card.setCardNumber(cardRequest.getCardNumber());
        card.setCardType(cardRequest.getCardType());
        card.setExpirationDate(cardRequest.getExpirationDate());
        card.setSecurityCode(cardRequest.getSecurityCode());
        card.setCardHolderName(cardRequest.getCardHolderName());

        when(cardService.createCard(any(Card.class))).thenReturn(card);

        mockMvc.perform(post("/api/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cardRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNumber").value(card.getCardNumber()))
                .andExpect(jsonPath("$.cardType").value(card.getCardType()));
    }

    @Test
    void testGetAllCards() throws Exception {
        Card card = new Card();
        when(cardService.getAllCards()).thenReturn(Collections.singletonList(card));

        mockMvc.perform(get("/api/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    void testGetCardById() throws Exception {
        Card card = new Card();
        card.setId(1L);
        when(cardService.getCardById(1L)).thenReturn(Optional.of(card));

        mockMvc.perform(get("/api/cards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testUpdateCard() throws Exception {
        CardRequest updatedCardRequest = new CardRequest();
        updatedCardRequest.setCardNumber("9876543210987654");

        Card updatedCard = new Card();
        updatedCard.setId(1L);
        updatedCard.setCardNumber(updatedCardRequest.getCardNumber());

        when(cardService.updateCard(any(Long.class), any(Card.class))).thenReturn(updatedCard);

        mockMvc.perform(put("/api/cards/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCardRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNumber").value(updatedCard.getCardNumber()));
    }

    @Test
    void testDeleteCard() throws Exception {
        mockMvc.perform(delete("/api/cards/1"))
                .andExpect(status().isOk());
    }
}
