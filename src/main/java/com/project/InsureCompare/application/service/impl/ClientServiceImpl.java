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
import com.project.InsureCompare.util.EntityUpdater;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private ClientMapper clientMapper;

	@Autowired
	private EntityUpdater entityUpdater;

	public Client saveClient(ClientDTO clientDTO) {

		Client client = clientMapper.toEntity(clientDTO);
		return clientRepository.save(client);
	}

	public Optional<ClientDTO> findClientById(Long id) {
		Optional<Client> clientOptional = clientRepository.findById(id);

		if (clientOptional.isEmpty()) {
			throw new EntityNotFoundException("Client not found with id: " + id);
		}

		return clientOptional.map(clientMapper::toDTO);
	}

	public Page<ClientDTO> getClientsOrderedByInsuranceValue(Pageable pageable) {
		return clientRepository.findClientesOrderedByTotalSeguroValue(pageable).map(clientMapper::toDTO);
	}

	public List<Client> findAllClients() {
		List<Client> allClients = clientRepository.findAll();
		return allClients;
	}

	public ClientDTO updateClient(Long id, Map<String, Object> updateRequest) {
		Optional<Client> existingClientOptional = clientRepository.findById(id);
		
		Client existingClient = existingClientOptional.get();
        entityUpdater.updateEntityFields(existingClient, updateRequest);
		Client updatedClient = clientRepository.save(existingClient);
		
		return clientMapper.toDTO(updatedClient);
	}

	public void deleteClient(Long id) {
		clientRepository.deleteById(id);
	}
}
