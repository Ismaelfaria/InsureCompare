package app.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import app.dto.QuoteDTO;
import app.mappers.QuoteMapper;
import domain.entity.Client;
import domain.entity.Insurance;
import domain.entity.Quote;
import infra.repository.ClientRepository;
import infra.repository.InsuranceRepository;
import infra.repository.QuoteRepository;
import jakarta.persistence.EntityNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuoteServiceTest {

	@Mock
	private QuoteRepository quoteRepository;

	@Mock
	private ClientRepository clientRepository;

	@Mock
	private InsuranceRepository insuranceRepository;

	@Mock
	private QuoteMapper quoteMapper;

	@InjectMocks
	private QuoteService quoteService;

	static Quote quote;
	static QuoteDTO quoteDTO;
	static Long invalidId;

	@BeforeAll
	public static void setUpEntities() {
		Client client = new Client(1L, "John Doe", "john.doe@gmail.com", "123456789", "123 Main St");
		Insurance insurance = new Insurance(1L, "Health Insurance", 500.0);

		quote = new Quote(1L, client, insurance, 1000.0);
		quoteDTO = new QuoteDTO(quote.getClient().getId(), quote.getInsurance().getId(), quote.getQuotedPrice());
		invalidId = 999L;
	}

	@Test
	void testSaveQuote_ValidDTO_SavesAndReturnsQuote() {
		when(quoteMapper.toEntity(quoteDTO)).thenReturn(quote);
		when(quoteRepository.save(quote)).thenReturn(quote);

		Quote result = quoteService.saveQuote(quoteDTO);

		assertNotNull(result);
		assertEquals(quote.getQuotedPrice(), result.getQuotedPrice());
	}

	@Test
	void testUpdateQuote_ValidId_UpdatesAndReturnsQuote() {
		Map<String, Object> updateRequest = new HashMap<>();
		updateRequest.put("quotedprice", 1200.0);

		when(quoteRepository.findById(quote.getId())).thenReturn(Optional.of(quote));
		when(quoteRepository.save(quote)).thenReturn(quote);
		when(quoteMapper.toDTO(quote)).thenReturn(quoteDTO);

		QuoteDTO result = quoteService.updateQuote(quote.getId(), updateRequest);

		assertNotNull(result);
		assertEquals(1200.0, quote.getQuotedPrice());
	}

	@Test
	void testUpdateQuote_InvalidField_ThrowsIllegalArgumentException() {
		Map<String, Object> updateRequest = new HashMap<>();
		updateRequest.put("invalidField", "someValue");

		EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
			quoteService.updateQuote(quote.getId(), updateRequest);
		});
		assertEquals("Quote not found with id: " + quote.getId(), thrown.getMessage());
	}

	@Test
	void testDeleteQuoteById_ValidId_DeletesQuote() {
		when(quoteRepository.existsById(quote.getId())).thenReturn(true);
		doNothing().when(quoteRepository).deleteById(quote.getId());

		quoteService.deleteQuoteById(quote.getId());

		verify(quoteRepository, times(1)).deleteById(quote.getId());
	}

	@Test
	void testDeleteQuoteById_InvalidIdThrowsEntityNotFoundException() {
		when(quoteRepository.existsById(invalidId)).thenReturn(false);

		EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
			quoteService.deleteQuoteById(invalidId);
		});
		assertEquals("Quote not found with id: " + invalidId, thrown.getMessage());
	}

	@Test
	void testFindAllQuotes_ReturnsListOfQuotes() {
		when(quoteRepository.findAll()).thenReturn(List.of(quote));
		when(quoteMapper.toDTO(quote)).thenReturn(quoteDTO);

		List<QuoteDTO> result = quoteService.findAllQuotes();

		assertNotNull(result);
		assertFalse(result.isEmpty());
	}

	@Test
	void testFindByIdQuotes_ValidIdReturnsQuote() {
		when(quoteRepository.findById(quote.getId())).thenReturn(Optional.of(quote));
		when(quoteMapper.toDTO(quote)).thenReturn(quoteDTO);

		Optional<QuoteDTO> result = quoteService.findByIdQuotes(quote.getId());

		assertTrue(result.isPresent());
		assertEquals(quoteDTO, result.get());
	}

	@Test
	void testFindByIdQuotes_InvalidIdReturnsEmpty() {
		when(quoteRepository.findById(invalidId)).thenReturn(Optional.empty());

		Optional<QuoteDTO> result = quoteService.findByIdQuotes(invalidId);

		assertFalse(result.isPresent());
	}
}
