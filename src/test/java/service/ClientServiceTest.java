package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import Mappers.ClientMapper;
import domain.Client;
import dto.ClientDTO;
import jakarta.validation.ConstraintViolationException;
import repository.ClientRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

	@InjectMocks
	ClientService service;

	@Mock
	ClientRepository repository;
	
	@Mock
    private ClientMapper clientMapper;

	ClientDTO clientDTO;

	Client client;
	
	ClientDTO clientInvalid;

	@BeforeEach
	public void setUpDTO() {
		clientDTO = new ClientDTO("Angelica", "Angelica@gmail.com", "111111111", "Rua das Cruzes 54");
	}

	@BeforeEach
	public void setUpEntity() {
		client = new Client(12345L, "Angelica", "Angelica@gmail.com", "111111111", "Rua das Cruzes 54");
	}
	
	@BeforeEach
	public void setUpDTOInvalid() {
		clientInvalid = new ClientDTO( null, null, "111111111", "Rua das Cruzes 54");
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
	    assertEquals(client.getName(), "Angelica");
	    assertEquals(client.getEmail(), "Angelica@gmail.com");
	    assertEquals(client.getPhone(), "111111111");
	    assertEquals(client.getAddress(), "Rua das Cruzes 54");

	    verify(repository).findById(client.getId());
	    verifyNoMoreInteractions(repository);
	}

	@Test
	void testFindClientByIdWithInvalidId() {
	}

	@Test
	void testFindClientByIdWhenClientDoesNotExist() {
	}

	@Test
	void testFindAllClientsWithExistingClients() {
	}

	@Test
	void testFindAllClientsWhenNoClientsExist() {
	}

	@Test
	void testUpdateClientWithValidFields() {
	}

	@Test
	void testUpdateClientWithInvalidFields() {
	}

	@Test
	void testUpdateClientWhenClientDoesNotExist() {
	}

	@Test
	void testUpdateClientWithPartialData() {
	}

	@Test
	void testDeleteClientWithValidId() {
	}

	@Test
	void testDeleteClientWhenClientDoesNotExist() {
	}

	@Test
	void testGetClientsOrderedByInsuranceValueWithValidPage() {
	}

	@Test
	void testGetClientsOrderedByInsuranceValueWithEmptyResult() {
	}

	@Test
	void testGetClientsOrderedByInsuranceValueWithPagination() {
	}

}
