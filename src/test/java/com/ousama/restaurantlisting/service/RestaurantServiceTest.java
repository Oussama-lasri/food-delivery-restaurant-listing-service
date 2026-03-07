package com.ousama.restaurantlisting.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ousama.restaurantlisting.dto.RestaurantDTO;
import com.ousama.restaurantlisting.entity.Restaurant;
import com.ousama.restaurantlisting.mapper.RestaurantMapper;
import com.ousama.restaurantlisting.repository.RestaurantRepository;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {

	@InjectMocks
	RestaurantService restaurantService;
	
	@Mock
	RestaurantRepository repository;
	@Mock
	RestaurantMapper restaurantMapper;
	
//	 @BeforeEach
//	    public void setUp() {
//	        MockitoAnnotations.openMocks(this); //in order for Mock and InjectMocks annotations to take effect, you need to call MockitoAnnotations.openMocks(this);
//	    }
	 
	 @Test
	 public void testFindAllRestaurants() {
		 List<Restaurant> mockRestaurants = Arrays.asList(
	                new Restaurant(1, "Restaurant 1", "Address 1", "city 1", "Desc 1"),
	                new Restaurant(2, "Restaurant 2", "Address 2", "city 2", "Desc 2")
	        );
		 
		 when(repository.findAll()).thenReturn(mockRestaurants);
		 
		 List<RestaurantDTO> restaurantDTOList = restaurantService.findAllRestaurants();
		 
		 // Verify the result
	        assertEquals(mockRestaurants.size(), restaurantDTOList.size());
	        for (int i = 0; i < mockRestaurants.size(); i++) {
	            RestaurantDTO expectedDTO = restaurantMapper.mapRestaurantToRestaurantDTO(mockRestaurants.get(i));
	            assertEquals(expectedDTO, restaurantDTOList.get(i));
	        }

	        // Verify that the repository method was called
	        verify(repository, times(1)).findAll();
	 }
	 
	 	@Test
	    public void testAddRestaurantInDB() {
	        // Create a mock restaurants to be saved
	 		// // Arrange
	 		 RestaurantDTO mockRestaurantDTO = new RestaurantDTO(1, "Restaurant 1", "Address 1", "city 1", "Desc 1");
	         Restaurant mockRestaurant = new Restaurant(1, "Restaurant 1", "Address 1", "city 1", "Desc 1");
	         Restaurant savedRestaurant = new Restaurant(1, "Restaurant 1", "Address 1", "city 1", "Desc 1");
	         RestaurantDTO savedRestaurantDTO = new RestaurantDTO(1, "Restaurant 1", "Address 1", "city 1", "Desc 1");
	         
	         when(restaurantMapper.mapRestaurantDTOToRestaurant(mockRestaurantDTO)).thenReturn(mockRestaurant);
	         when(repository.save(mockRestaurant)).thenReturn(savedRestaurant);
	         when(restaurantMapper.mapRestaurantToRestaurantDTO(savedRestaurant)).thenReturn(savedRestaurantDTO);
	         
	         // Act
	         RestaurantDTO result = restaurantService.addRestaurantInDB(mockRestaurantDTO);
	         
	         // Assert
	         assertNotNull(result);
	         assertEquals(1, result.getId());
	         assertEquals("Restaurant 1", result.getName());
	         assertEquals("Address 1", result.getAddress());
	         
	         verify(restaurantMapper, times(1)).mapRestaurantDTOToRestaurant(mockRestaurantDTO);
	         verify(repository, times(1)).save(mockRestaurant);
	         verify(restaurantMapper, times(1)).mapRestaurantToRestaurantDTO(savedRestaurant);
	    
	    }
	 	
	    @Test
	    public void testFetchRestaurantById_ExistingId() {
	        // Create a mock restaurant ID
	    	 // Arrange
	        Integer mockRestaurantId = 1;
	        Restaurant mockRestaurant = new Restaurant(1, "Restaurant 1", "Address 1", "city 1", "Desc 1");
	        RestaurantDTO mockRestaurantDTO = new RestaurantDTO(1, "Restaurant 1", "Address 1", "city 1", "Desc 1");
	        
	        when(repository.findById(mockRestaurantId)).thenReturn(Optional.of(mockRestaurant));
	        when(restaurantMapper.mapRestaurantToRestaurantDTO(mockRestaurant)).thenReturn(mockRestaurantDTO);
	        
	        // Act
	        ResponseEntity<RestaurantDTO> response = restaurantService.fetchRestaurantById(mockRestaurantId);
	        
	        // Assert
	        assertNotNull(response);
	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertNotNull(response.getBody());
	        assertEquals(1, response.getBody().getId());
	        assertEquals("Restaurant 1", response.getBody().getName());
	        assertEquals("Address 1", response.getBody().getAddress());
	        
	        verify(repository, times(1)).findById(mockRestaurantId);
	        verify(restaurantMapper, times(1)).mapRestaurantToRestaurantDTO(mockRestaurant);
	    }

	    @Test
	    public void testFetchRestaurantById_NonExistingId() {
	        // Create a mock non-existing restaurant ID
	        Integer mockRestaurantId = 1;

	        // Mock the repository behavior
	        when(repository.findById(mockRestaurantId)).thenReturn(Optional.empty());

	        // Call the service method
	        ResponseEntity<RestaurantDTO> response = restaurantService.fetchRestaurantById(mockRestaurantId);

	        // Verify the response
	        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	        assertEquals(null, response.getBody());

	        // Verify that the repository method was called
	        verify(repository, times(1)).findById(mockRestaurantId);
	    }
}
