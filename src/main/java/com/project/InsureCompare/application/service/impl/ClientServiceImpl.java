package com.project.InsureCompare.application.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.InsureCompare.application.dto.ClientDTO;
import com.project.InsureCompare.application.mappers.ClientMapper;
import com.project.InsureCompare.application.service.interfaces.ClientService;
import com.project.InsureCompare.domain.entity.Client;
import com.project.InsureCompare.infra.repository.ClientRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ClientServiceImpl implements ClientService{

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private ClientMapper clientMapper;

	public Client saveClient(ClientDTO clientDTO) {

		Client client = clientMapper.toEntity(clientDTO);
		return clientRepository.save(client);
	}

	public Optional<ClientDTO> findClientById(Long id) {
		return clientRepository.findById(id).map(clientMapper::toDTO);
	}

	public Page<ClientDTO> getClientsOrderedByInsuranceValue(Pageable pageable) {
		return clientRepository.findClientesOrderedByTotalSeguroValue(pageable).map(clientMapper::toDTO);
	}

	public List<Client> findAllClients() {
		List<Client> allClients = clientRepository.findAll();
		return allClients;
	}

	public ClientDTO updateClient(Long id, Map<String, Object> updateRequest) {

		Client existingClient = clientRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + id));

		updateClientFields(existingClient, updateRequest);

		Client updatedClient = clientRepository.save(existingClient);
		return clientMapper.toDTO(updatedClient);
	}

	public void updateClientFields(Client existingClient, Map<String, Object> updateRequest) {
		updateRequest.forEach((field, newValue) -> {
			switch (field.toLowerCase()) {
			case "name":
				existingClient.setName((String) newValue);
				break;
			case "email":
				existingClient.setEmail((String) newValue);
				break;
			case "address":
				existingClient.setAddress((String) newValue);
				break;
			case "phone":
				existingClient.setPhone((String) newValue);
				break;
			default:
				throw new IllegalArgumentException("Invalid field to update: " + field);
			}
		});
	}

	public void deleteClient(Long id) {
		clientRepository.deleteById(id);
	}
}
