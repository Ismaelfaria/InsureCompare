package app.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.dto.ClientDTO;
import app.mappers.ClientMapper;
import domain.entity.Client;
import infra.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ClientService {

	@Autowired
    private ClientRepository clientRepository;
	
	@Autowired
    private ClientMapper clientMapper;

    public Client saveClient(ClientDTO clientDTO) {
    	
    	Client client = clientMapper.toEntity(clientDTO);
        return clientRepository.save(client);
    }
    
    public Optional<ClientDTO> findClientById(Long id) {
        return clientRepository.findById(id)
                               .map(clientMapper::toDTO);
    }
    
    public List<ClientDTO> findAllClients() {
        List<Client> allClients = clientRepository.findAll();
        return allClients.stream()
                         .map(clientMapper::toDTO)
                         .toList();
    }

    public ClientDTO updateClient(Long id, Map<String, Object> updateRequest) {

    	Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + id));

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
        
        Client updatedClient = clientRepository.save(existingClient);
        return clientMapper.toDTO(updatedClient);
    }
    
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    public Page<ClientDTO> getClientsOrderedByInsuranceValue(Pageable pageable) {
        return clientRepository.findClientesOrderedByTotalSeguroValue(pageable)
        		.map(clientMapper::toDTO);
    }
}
