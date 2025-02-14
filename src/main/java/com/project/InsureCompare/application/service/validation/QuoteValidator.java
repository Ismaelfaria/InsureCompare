package com.project.InsureCompare.application.service.validation;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.stereotype.Component;
import java.util.function.Consumer;
import com.project.InsureCompare.domain.entity.Quote;
import com.project.InsureCompare.infra.repository.ClientRepository;
import com.project.InsureCompare.infra.repository.InsuranceRepository;

import jakarta.persistence.EntityNotFoundException;

@Component
public class QuoteValidator {
	private final ClientRepository clientRepository;
	private final InsuranceRepository insuranceRepository;

	public QuoteValidator(ClientRepository clientRepository, InsuranceRepository insuranceRepository) {
		this.clientRepository = clientRepository;
		this.insuranceRepository = insuranceRepository;
	}

	public <T> void updateRelationship(Map<String, Object> updateRequest, String fieldName, Long id,
			Function<Long, Optional<T>> repositoryMethod, Consumer<T> setterMethod) {
		if (updateRequest.containsKey(fieldName)) {
			Optional<T> relatedEntityOpt = repositoryMethod.apply(id);
			if (relatedEntityOpt.isPresent()) {
				setterMethod.accept(relatedEntityOpt.get());
			} else {
				throw new EntityNotFoundException(fieldName + " not found with id: " + id);
			}
		}
	}

	public void validateAndUpdateRelationships(Quote quote, Map<String, Object> updateRequest) {
		updateRelationship(updateRequest, "client", (Long) updateRequest.get("client"), clientRepository::findById,
				quote::setClient);
		updateRelationship(updateRequest, "insurance", (Long) updateRequest.get("insurance"),
				insuranceRepository::findById, quote::setInsurance);
	}
}
