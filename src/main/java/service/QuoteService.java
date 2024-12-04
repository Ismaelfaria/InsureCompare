package service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import domain.Client;
import domain.Insurance;
import domain.Quote;
import jakarta.persistence.EntityNotFoundException;
import repository.ClientRepository;
import repository.InsuranceRepository;
import repository.QuoteRepository;

@Service
public class QuoteService {
	
		@Autowired
	 	private QuoteRepository quoteRepository;
		
		@Autowired
	 	private ClientRepository clientRepository;
		
		@Autowired
	 	private InsuranceRepository insuranceRepository;

	    public List<Quote> getQuotesByClient(Client client) {
	        return quoteRepository.findByClient(client);
	    }

	    public Quote saveQuote(Quote quote) {
	        return quoteRepository.save(quote);
	    }

	    public Quote updateQuote(Long id, Map<String, Object> updateRequest) {
	        Quote existingQuote = quoteRepository.findById(id)
	                .orElseThrow(() -> new EntityNotFoundException("Quote not found with id: " + id));

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

	        return quoteRepository.save(existingQuote);
	    }
	    
	    public void deleteQuoteById(Long id) {
	        if (!quoteRepository.existsById(id)) {
	            throw new EntityNotFoundException("Quote not found with id: " + id);
	        }
	        quoteRepository.deleteById(id);
	    }

	    public List<Quote> getAllQuotes() {
	        return quoteRepository.findAll();
	    }
}
