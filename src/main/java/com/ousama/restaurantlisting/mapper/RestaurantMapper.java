package com.ousama.restaurantlisting.mapper;

import org.mapstruct.Mapper;

import com.ousama.restaurantlisting.dto.RestaurantDTO;
import com.ousama.restaurantlisting.entity.Restaurant;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    Restaurant mapRestaurantDTOToRestaurant(RestaurantDTO restaurantDTO);

    RestaurantDTO mapRestaurantToRestaurantDTO(Restaurant restaurant);
}
