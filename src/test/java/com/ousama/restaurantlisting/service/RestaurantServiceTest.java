package com.ousama.restaurantlisting.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;


import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ousama.restaurantlisting.dto.RestaurantDTO;
import com.ousama.restaurantlisting.entity.Restaurant;
import com.ousama.restaurantlisting.mapper.RestaurantMapper;
import com.ousama.restaurantlisting.repository.RestaurantRepository;

public class RestaurantServiceTest {

	@InjectMocks
	RestaurantService restaurantService;
	
	@Mock
	RestaurantRepository repository;
	@Mock
	RestaurantMapper restaurantMapper;
	
	 @BeforeEach
	    public void setUp() {
	        MockitoAnnotations.openMocks(this); //in order for Mock and InjectMocks annotations to take effect, you need to call MockitoAnnotations.openMocks(this);
	    }
	 
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
}
