package com.project.InsureCompare.application.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.InsureCompare.application.dto.QuoteDTO;
import com.project.InsureCompare.application.mappers.QuoteMapper;
import com.project.InsureCompare.application.service.interfaces.QuoteService;
import com.project.InsureCompare.application.service.validation.QuoteValidator;
import com.project.InsureCompare.domain.entity.Quote;
import com.project.InsureCompare.infra.repository.QuoteRepository;
import com.project.InsureCompare.util.EntityUpdater;

import jakarta.persistence.EntityNotFoundException;

@Service
public class QuoteServiceImpl implements QuoteService {

	@Autowired
	private QuoteRepository quoteRepository;

	@Autowired
	private QuoteMapper quoteMapper;

	@Autowired
	private EntityUpdater entityUpdater;

	@Autowired
	private QuoteValidator quoteValidator;

	public Quote saveQuote(QuoteDTO quoteDTO) {

		Quote quote = quoteMapper.toEntity(quoteDTO);

		return quoteRepository.save(quote);
	}

	public QuoteDTO updateQuote(Long id, Map<String, Object> updateRequest) {
		Optional<Quote> existingQuoteOptional = quoteRepository.findById(id);

		Quote existingQuote = existingQuoteOptional.get();
		entityUpdater.updateEntityFields(existingQuote, updateRequest);

		quoteValidator.validateAndUpdateRelationships(existingQuote, updateRequest);

		Quote updatedQuote = quoteRepository.save(existingQuote);
		return quoteMapper.toDTO(updatedQuote);
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
		Optional<Quote> existingQuoteOptional = quoteRepository.findById(id);

		if (existingQuoteOptional.isEmpty()) {
			throw new EntityNotFoundException("Client not found with id: " + id);
		}
		return existingQuoteOptional.map(quoteMapper::toDTO);
	}
}
