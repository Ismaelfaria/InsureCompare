package Mappers;

import org.springframework.stereotype.Component;

import domain.Client;
import dto.ClientDTO;

@Component
public class ClientMapper {
		
	public Client toEntity(ClientDTO dto) {
        Client client = new Client();
        client.setName(dto.name());
        client.setEmail(dto.email());
        client.setPhone(dto.phone());
        client.setAddress(dto.address());
        return client;
    }

    public ClientDTO toDTO(Client entity) {
        return new ClientDTO(
            entity.getName(),
            entity.getEmail(),
            entity.getPhone(),
            entity.getAddress()
        );
    }
	
}
