package com.project.InsureCompare.application.service.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.InsureCompare.application.dto.ClientDTO;
import com.project.InsureCompare.domain.entity.Client;

public interface ClientService {

	Client saveClient(ClientDTO clientDTO);
	Optional<ClientDTO> findClientById(Long id);
	Page<ClientDTO> getClientsOrderedByInsuranceValue(Pageable pageable);
	List<Client> findAllClients();
	ClientDTO updateClient(Long id, Map<String, Object> updateRequest);
	void deleteClient(Long id);
	
}
