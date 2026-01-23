package com.ousama.restaurantlisting.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ousama.restaurantlisting.dto.RestaurantDTO;
import com.ousama.restaurantlisting.entity.Restaurant;
import com.ousama.restaurantlisting.mapper.RestaurantMapper;
import com.ousama.restaurantlisting.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

//    @Autowired
//    public RestaurantService(RestaurantRepository restaurantRepository,
//                             RestaurantMapper restaurantMapper) {
//        this.restaurantRepository = restaurantRepository;
//        this.restaurantMapper = restaurantMapper;
//    }

    public List<RestaurantDTO> findAllRestaurants() {
        return restaurantRepository.findAll()
                .stream()
                .map(restaurantMapper::mapRestaurantToRestaurantDTO)
                .collect(Collectors.toList());
    }

    public RestaurantDTO addRestaurantInDB(RestaurantDTO restaurantDTO) {
        Restaurant restaurant =
                restaurantMapper.mapRestaurantDTOToRestaurant(restaurantDTO);

        Restaurant saved = restaurantRepository.save(restaurant);

        return restaurantMapper.mapRestaurantToRestaurantDTO(saved);
    }

    public ResponseEntity<RestaurantDTO> fetchRestaurantById(Integer id) {
        return restaurantRepository.findById(id)
        		.map(restaurant -> restaurantMapper.mapRestaurantToRestaurantDTO(restaurant))
//                .map(restaurantMapper::mapRestaurantToRestaurantDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
