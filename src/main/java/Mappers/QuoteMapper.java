package Mappers;

import org.springframework.stereotype.Component;

import domain.Client;
import domain.Insurance;
import domain.Quote;
import dto.QuoteDTO;
import repository.ClientRepository;
import repository.InsuranceRepository;

@Component
public class QuoteMapper {

	 private final ClientRepository clientRepository;
	    private final InsuranceRepository insuranceRepository;

	    public QuoteMapper(ClientRepository clientRepository, InsuranceRepository insuranceRepository) {
	        this.clientRepository = clientRepository;
	        this.insuranceRepository = insuranceRepository;
	    }

	    public Quote toEntity(QuoteDTO dto) {
	        Quote quote = new Quote();

	        Client client = clientRepository.findById(dto.customerId())
	                .orElseThrow(() -> new IllegalArgumentException("Client with ID " + dto.customerId() + " not found"));

	        Insurance insurance = insuranceRepository.findById(dto.insuranceId())
	                .orElseThrow(() -> new IllegalArgumentException("Insurance with ID " + dto.insuranceId() + " not found"));

	        quote.setClient(client);
	        quote.setInsurance(insurance);
	        quote.setQuotedPrice(dto.quotedPrice());

	        return quote;
	    }

	    public QuoteDTO toDTO(Quote quote) {
	        return new QuoteDTO(
	                quote.getClient().getId(),
	                quote.getInsurance().getId(),
	                quote.getQuotedPrice()
	        );
	    }
	
}
