package com.vaultwise.controller;

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
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CardControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CardService cardService;

    @InjectMocks
    private CardController cardController;

    private Card card;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cardController).build();

        // Initialize card object for testing
        card = new Card();
        card.setId(1L);
        card.setCardNumber("1234567890123456");
        card.setCardType("Visa");
        card.setCardHolderName("John Doe");
        card.setSecurityCode("123");
    }

    @Test
    void testCreateCard() throws Exception {
        // Prepare Card object to send in the test
        Card card = new Card();
        card.setCardNumber("1234567890123456");
        card.setCardType("Visa");
        card.setCardHolderName("John Doe");
        card.setSecurityCode("123");
        card.setExpirationDate(LocalDate.of(2025, 12, 31));

        // Mocking the service layer
        when(cardService.createCard(any(Card.class))).thenReturn(card);

        // Perform the test
        mockMvc.perform(post("/api/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cardNumber\":\"1234567890123456\", \"cardType\":\"Visa\", \"cardHolderName\":\"John Doe\", \"securityCode\":\"123\", \"expirationDate\":\"2025-12-31\"}"))
                .andExpect(status().isOk())  // Expect 200 OK status
                .andExpect(jsonPath("$.cardNumber").value("1234567890123456"))
                .andExpect(jsonPath("$.cardType").value("Visa"))
                .andExpect(jsonPath("$.cardHolderName").value("John Doe"));
    }




    @Test
    void testGetAllCards() throws Exception {
        when(cardService.getAllCards()).thenReturn(List.of(card));

        mockMvc.perform(get("/api/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cardNumber").value("1234567890123456"));
    }

    @Test
    void testGetCardById() throws Exception {
        when(cardService.getCardById(1L)).thenReturn(Optional.of(card));

        mockMvc.perform(get("/api/cards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNumber").value("1234567890123456"));
    }

    @Test
    void testUpdateCard() throws Exception {
        when(cardService.updateCard(eq(1L), any(Card.class))).thenReturn(card);

        mockMvc.perform(put("/api/cards/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cardNumber\":\"1234567890123456\", \"cardType\":\"Visa\", \"cardHolderName\":\"John Doe\", \"securityCode\":\"123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardNumber").value("1234567890123456"));
    }

    @Test
    void testDeleteCard() throws Exception {
        Long cardId = 1L;

        // Perform the delete request
        mockMvc.perform(delete("/api/cards/{id}", cardId))
                .andExpect(status().isOk());  // Expect 200 OK since the controller doesn't explicitly return 204

        // Verify that the deleteCard method was called once with the correct cardId
        verify(cardService, times(1)).deleteCard(cardId);
    }

}
