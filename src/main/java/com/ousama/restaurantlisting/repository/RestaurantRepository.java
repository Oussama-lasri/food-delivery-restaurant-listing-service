package com.ousama.restaurantlisting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ousama.restaurantlisting.entity.Restaurant;


@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

}
