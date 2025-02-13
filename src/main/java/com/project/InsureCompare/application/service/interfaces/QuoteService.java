package com.project.InsureCompare.application.service.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.project.InsureCompare.application.dto.QuoteDTO;
import com.project.InsureCompare.domain.entity.Quote;

public interface QuoteService {

	Quote saveQuote(QuoteDTO quoteDTO);
	QuoteDTO updateQuote(Long id, Map<String, Object> updateRequest);
	void updateQuoteFields(Quote existingQuote, Map<String, Object> updateRequest);
	void deleteQuoteById(Long id);
	List<QuoteDTO> findAllQuotes();
	Optional<QuoteDTO> findByIdQuotes(Long id);
}
