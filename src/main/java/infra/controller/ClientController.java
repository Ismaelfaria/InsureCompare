package infra.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.dto.ClientDTO;
import app.service.ClientService;
import domain.entity.Client;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/ClientController")
public class ClientController {

	@Autowired
	private ClientService clientService;

	@PostMapping
	public ResponseEntity<ClientDTO> saveClient(ClientDTO clientDTO) {
		Client savedClient = clientService.saveClient(clientDTO);
		ClientDTO savedClientDTO = clientService.findClientById(savedClient.getId()).orElse(null);
		return new ResponseEntity<>(savedClientDTO, HttpStatus.CREATED);
	}

	@GetMapping("/clients/{id}")
	public ResponseEntity<ClientDTO> findClientById(Long id) {
		Optional<ClientDTO> clientDTO = clientService.findClientById(id);
		return clientDTO.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@GetMapping
	public ResponseEntity<List<ClientDTO>> findAllClients() {
		List<ClientDTO> clients = clientService.findAllClients();
		return ResponseEntity.ok(clients);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id,
			@RequestBody Map<String, Object> updateRequest) {
		try {
			ClientDTO updatedClient = clientService.updateClient(id, updateRequest);
			return ResponseEntity.ok(updatedClient);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
		clientService.deleteClient(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/ordered-by-insurance")
	public ResponseEntity<Page<ClientDTO>> getClientsOrderedByInsuranceValue(Pageable pageable) {
		Page<ClientDTO> clientsPage = clientService.getClientsOrderedByInsuranceValue(pageable);
		return ResponseEntity.ok(clientsPage);
	}
}
