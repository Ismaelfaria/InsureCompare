package com.project.InsureCompare.application.mappers;

import org.springframework.stereotype.Component;

import com.project.InsureCompare.application.dto.QuoteDTO;
import com.project.InsureCompare.domain.entity.Client;
import com.project.InsureCompare.domain.entity.Insurance;
import com.project.InsureCompare.domain.entity.Quote;
import com.project.InsureCompare.infra.repository.ClientRepository;
import com.project.InsureCompare.infra.repository.InsuranceRepository;

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

		Client client = clientRepository.findById(dto.clientId())
				.orElseThrow(() -> new IllegalArgumentException("Client with ID " + dto.clientId() + " not found"));

		Insurance insurance = insuranceRepository.findById(dto.insuranceId()).orElseThrow(
				() -> new IllegalArgumentException("Insurance with ID " + dto.insuranceId() + " not found"));

		quote.setClient(client);
		quote.setInsurance(insurance);
		quote.setQuotedPrice(dto.quotedPrice());

		return quote;
	}

	public QuoteDTO toDTO(Quote quote) {
		return new QuoteDTO(quote.getClient().getId(), quote.getInsurance().getId(), quote.getQuotedPrice());
	}
}
