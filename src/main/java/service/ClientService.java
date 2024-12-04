package service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import domain.Client;
import jakarta.persistence.EntityNotFoundException;
import repository.ClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ClientService {

	@Autowired
    private ClientRepository clientRepository;

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public Optional<Client> findClientById(Long id) {
        return clientRepository.findById(id);
    }
    
    public List<Client> findAllClient(Long id) {
        List<Client> allClient =  clientRepository.findAll();
        return allClient;
    }

    public Client updateClient(Long id, Map<String, Object> updateRequest) {

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

        return clientRepository.save(existingClient);
    }
    
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    public Page<Client> getClientsOrderedByInsuranceValue(Pageable pageable) {
        return clientRepository.findClientesOrderedByTotalSeguroValue(pageable);
    }
}
