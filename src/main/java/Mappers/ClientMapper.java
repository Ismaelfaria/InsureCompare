package Mappers;

import org.springframework.stereotype.Component;

import domain.Client;
import dto.ClientDTO;

@Component
public class ClientMapper {
		
	public Client toEntity(ClientDTO dto) {
        Client client = new Client(
        null,
        dto.name(),
        dto.email(),
        dto.phone(),
        dto.address());
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
