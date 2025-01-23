package application.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import application.dto.QuoteDTO;
import application.mappers.QuoteMapper;
import domain.entity.Client;
import domain.entity.Insurance;
import domain.entity.Quote;
import infra.repository.ClientRepository;
import infra.repository.InsuranceRepository;
import infra.repository.QuoteRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class QuoteService {

	@Autowired
	private QuoteRepository quoteRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private InsuranceRepository insuranceRepository;

	@Autowired
	private QuoteMapper quoteMapper;

	public Quote saveQuote(QuoteDTO quoteDTO) {

		Quote quote = quoteMapper.toEntity(quoteDTO);

		return quoteRepository.save(quote);
	}

	public QuoteDTO updateQuote(Long id, Map<String, Object> updateRequest) {
		Quote existingQuote = quoteRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Quote not found with id: " + id));

		updateQuoteFields(existingQuote, updateRequest);

		quoteRepository.save(existingQuote);
		return quoteMapper.toDTO(existingQuote);
	}
	
	private void updateQuoteFields(Quote existingQuote, Map<String, Object> updateRequest) {
		updateRequest.forEach((field, newValue) -> {
			switch (field.toLowerCase()) {
			case "quotedprice":
				existingQuote.setQuotedPrice((Double) newValue);
				break;
			case "client":
				Client client = clientRepository.findById((Long) newValue)
						.orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + newValue));
				existingQuote.setClient(client);
				break;
			case "insurance":
				Insurance insurance = insuranceRepository.findById((Long) newValue)
						.orElseThrow(() -> new EntityNotFoundException("Insurance not found with id: " + newValue));
				existingQuote.setInsurance(insurance);
				break;
			default:
				throw new IllegalArgumentException("Invalid field to update: " + field);
			}
		});
	}

	public void deleteQuoteById(Long id) {
		if (!quoteRepository.existsById(id)) {
			throw new EntityNotFoundException("Quote not found with id: " + id);
		}
		quoteRepository.deleteById(id);
	}

	public List<QuoteDTO> findAllQuotes() {
		return quoteRepository.findAll().stream().map(quoteMapper::toDTO).toList();
	}

	public Optional<QuoteDTO> findByIdQuotes(Long id) {
		return quoteRepository.findById(id).map(quoteMapper::toDTO);
	}
}
