package app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.project.InsureCompare.application.dto.ClientDTO;
import com.project.InsureCompare.application.mappers.ClientMapper;
import com.project.InsureCompare.application.service.ClientService;
import com.project.InsureCompare.domain.entity.Client;
import com.project.InsureCompare.infra.repository.ClientRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

	@InjectMocks
	ClientService service;

	@Mock
	ClientRepository repository;

	@Mock
	private ClientMapper clientMapper;

	static ClientDTO clientDTO;
	static Client client;
	static ClientDTO clientInvalid;

	@BeforeAll
	public static void setUpEntities() {
		client = new Client(1L, "test", "test@gmail.com", "111111111", "Rua das Cruzes 54");
		clientDTO = new ClientDTO(client.getName(), client.getEmail(), client.getPhone(), client.getAddress());
		clientInvalid = new ClientDTO(null, null, client.getPhone(), client.getAddress());
	}

	@Test
	void testSaveClientWithValidData() {
		when(clientMapper.toEntity(clientDTO)).thenReturn(client);
		when(repository.save(client)).thenReturn(client);

		Client savedClient = service.saveClient(clientDTO);

		assertNotNull(savedClient);
		assertEquals(client.getName(), savedClient.getName());
		assertEquals(client.getEmail(), savedClient.getEmail());
		assertEquals(client.getAddress(), savedClient.getAddress());

		verify(repository).save(client);
		verify(clientMapper).toEntity(clientDTO);
		verify(repository).save(client);
		verifyNoMoreInteractions(clientMapper, repository);
	}

	@Test
	void testSaveClientWhenRepositoryFails() {
		when(clientMapper.toEntity(clientDTO)).thenReturn(client);
		when(repository.save(client)).thenThrow(new RuntimeException("Database error"));

		RuntimeException thrown = assertThrows(RuntimeException.class, () -> service.saveClient(clientDTO));

		assertEquals("Database error", thrown.getMessage());

		verify(repository, times(1)).save(any(Client.class));
	}

	@Test
	void testFindClientByIdWithValidId() {
		when(clientMapper.toDTO(client)).thenReturn(clientDTO);
		when(repository.findById(client.getId())).thenReturn(Optional.of(client));

		Optional<ClientDTO> result = service.findClientById(client.getId());

		assertNotNull(result);
		assertEquals(client.getName(), "test");
		assertEquals(client.getEmail(), "test@gmail.com");
		assertEquals(client.getPhone(), "111111111");
		assertEquals(client.getAddress(), "Rua das Cruzes 54");

		verify(repository).findById(client.getId());
		verifyNoMoreInteractions(repository);
	}

	@Test
	void testFindClientByIdWhenClientDoesNotExist() {
		long invalidId = 999L;
		when(repository.findById(invalidId)).thenReturn(Optional.empty());

		Optional<ClientDTO> result = service.findClientById(invalidId);

		assertTrue(result.isEmpty());

		verify(repository).findById(invalidId);
		verify(repository, times(1)).findById(invalidId);
	}

	@Test
	void testFindAllClientsWithExistingClients() {
		List<Client> testClientAll = new ArrayList<Client>();
		testClientAll.add(client);

		when(clientMapper.toDTO(client)).thenReturn(clientDTO);
		when(repository.findAll()).thenReturn(testClientAll);

		List<ClientDTO> result = service.findAllClients();

		assertEquals(1, result.size());
		assertEquals("test", result.get(0).name());

		verify(repository, times(1)).findAll();
	}

	@Test
	void testFindAllClientsWhenNoClientsExist() {
		when(repository.findAll()).thenReturn(new ArrayList<>());

		List<ClientDTO> result = service.findAllClients();

		assertTrue(result.isEmpty());
		assertEquals(0, result.size());

		verify(repository, times(1)).findAll();
	}

	@Test
	void testUpdateClientWithValidFields() {
		Map<String, Object> updateRequest = new HashMap<>();
		updateRequest.put("name", "New Name");

		when(repository.findById(client.getId())).thenReturn(Optional.of(client));
		when(repository.save(client)).thenReturn(client);
		when(clientMapper.toDTO(client)).thenReturn(clientDTO);

		ClientDTO result = service.updateClient(client.getId(), updateRequest);

		assertNotNull(result);
		assertEquals("New Name", client.getName());

		verify(repository, times(1)).findById(client.getId());
		verify(repository, times(1)).save(client);
		verify(clientMapper, times(1)).toDTO(client);
	}

	@Test
	void testUpdateClientWithInvalidFields() {
		Map<String, Object> updateRequest = new HashMap<>();
		updateRequest.put("invalidField", "Invalid Value");

		when(repository.findById(client.getId())).thenReturn(Optional.of(client));

		assertThrows(IllegalArgumentException.class, () -> {
			service.updateClient(client.getId(), updateRequest);
		});

		verify(repository, times(1)).findById(client.getId());
		verify(repository, never()).save(any(Client.class));
	}

	@Test
	void testUpdateClientWhenClientDoesNotExist() {
		Long invalidClientId = 999L;

		Map<String, Object> updateRequest = new HashMap<>();
		updateRequest.put("name", "New Name");

		when(repository.findById(invalidClientId)).thenReturn(Optional.empty());

		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
			service.updateClient(invalidClientId, updateRequest);
		});

		assertEquals("Client not found with id: " + invalidClientId, exception.getMessage());

		verify(repository, times(1)).findById(invalidClientId);
		verify(repository, never()).save(any(Client.class));
	}

	@Test
	void testDeleteClientWithValidId() {
		doNothing().when(repository).deleteById(client.getId());

		service.deleteClient(client.getId());

		verify(repository, times(1)).deleteById(client.getId());
	}

	@Test
	void testDeleteClientWhenClientDoesNotExist() {
		Long invalidClientId = 999L;

		doThrow(new EntityNotFoundException("Client not found with id: " + invalidClientId)).when(repository)
				.deleteById(invalidClientId);

		assertThrows(EntityNotFoundException.class, () -> service.deleteClient(invalidClientId));

		verify(repository, times(1)).deleteById(invalidClientId);
	}

	@Test
	void testGetClientsOrderedByInsuranceValueWithValidPage() {
		Pageable pageable = PageRequest.of(0, 5, Sort.by("insuranceValue").descending());

		Client client1 = new Client(1L, "test1", "test1@gmail.com", "111111111", "Rua das Cruzes 54");
		Client client2 = new Client(2L, "test2", "test2@gmail.com", "222222222", "Rua das Cruzes 54");
		List<Client> clients = Arrays.asList(client1, client2);
		Page<Client> clientPage = new PageImpl<>(clients, pageable, clients.size());

		when(repository.findClientesOrderedByTotalSeguroValue(pageable)).thenReturn(clientPage);
		when(clientMapper.toDTO(any(Client.class))).thenAnswer(invocation -> {
			Client client = invocation.getArgument(0);
			return new ClientDTO(client.getName(), client.getEmail(), client.getPhone(), client.getAddress());
		});

		Page<ClientDTO> result = service.getClientsOrderedByInsuranceValue(pageable);

		assertNotNull(result);
		assertEquals(2, result.getContent().size());
		assertEquals("test1", result.getContent().get(0).name());
		assertEquals("111111111", result.getContent().get(0).phone());

		verify(repository, times(1)).findClientesOrderedByTotalSeguroValue(pageable);
	}

	@Test
	void testGetClientsOrderedByInsuranceValueWithEmptyResult() {
		Pageable pageable = PageRequest.of(0, 5, Sort.by("insuranceValue").descending());

		Page<Client> emptyPage = new PageImpl<>(new ArrayList<>(), pageable, 0);

		when(repository.findClientesOrderedByTotalSeguroValue(pageable)).thenReturn(emptyPage);

		Page<ClientDTO> result = service.getClientsOrderedByInsuranceValue(pageable);

		assertNotNull(result);
		assertTrue(result.getContent().isEmpty());
		assertEquals(0, result.getTotalElements());

		verify(repository, times(1)).findClientesOrderedByTotalSeguroValue(pageable);
	}

	@Test
	void testGetClientsOrderedByInsuranceValueWithPagination() {
		Pageable pageable = PageRequest.of(0, 2, Sort.by("insuranceValue").descending());

		Client client1 = new Client(1L, "test1", "test1@gmail.com", "111111111", "Rua das Cruzes 54");
		Client client2 = new Client(2L, "test2", "test2@gmail.com", "22222222", "Rua das Cruzes 54");

		List<Client> clients = Arrays.asList(client1, client2);
		Page<Client> clientPage = new PageImpl<>(clients, pageable, clients.size());

		when(repository.findClientesOrderedByTotalSeguroValue(pageable)).thenReturn(clientPage);
		when(clientMapper.toDTO(client1))
				.thenReturn(new ClientDTO("test1", "test1@gmail.com", "111111111", "Rua das Cruzes 54"));
		when(clientMapper.toDTO(client2))
				.thenReturn(new ClientDTO("test2", "test2@gmail.com", "22222222", "Rua das Cruzes 54"));

		Page<ClientDTO> result = service.getClientsOrderedByInsuranceValue(pageable);

		assertNotNull(result);
		assertEquals(2, result.getTotalElements());
		assertEquals(2, result.getContent().size());
		assertEquals("test1", result.getContent().get(0).name());
		assertEquals("test1@gmail.com", result.getContent().get(0).email());
		assertEquals("test2", result.getContent().get(1).name());
		assertEquals("test2@gmail.com", result.getContent().get(1).email());

		verify(repository, times(1)).findClientesOrderedByTotalSeguroValue(pageable);
		verify(clientMapper, times(1)).toDTO(client1);
		verify(clientMapper, times(1)).toDTO(client2);
	}
}
