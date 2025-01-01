package service;

import org.junit.jupiter.api.Test;

class ClientServiceTest {

	
	@Test
	void testSaveClientWithValidData() {}
	@Test
	void testSaveClientWithInvalidData() {}
	@Test
	void testSaveClientWhenRepositoryFails() {}
	
	@Test
	void testFindClientByIdWithValidId() {}
	@Test
	void testFindClientByIdWithInvalidId() {}
	@Test
	void testFindClientByIdWhenClientDoesNotExist() {}
	
	@Test
	void testFindAllClientsWithExistingClients() {}
	@Test
	void testFindAllClientsWhenNoClientsExist() {}
	
	@Test
	void testUpdateClientWithValidFields() {}
	@Test
	void testUpdateClientWithInvalidFields() {}
	@Test
	void testUpdateClientWhenClientDoesNotExist() {}
	@Test
	void testUpdateClientWithPartialData() {}
	
	@Test
	void testDeleteClientWithValidId() {}
	@Test
	void testDeleteClientWhenClientDoesNotExist() {}
	@Test
	void testGetClientsOrderedByInsuranceValueWithValidPage() {}
	@Test
	void testGetClientsOrderedByInsuranceValueWithEmptyResult() {}
	@Test
	void testGetClientsOrderedByInsuranceValueWithPagination() {}
	
	

}
