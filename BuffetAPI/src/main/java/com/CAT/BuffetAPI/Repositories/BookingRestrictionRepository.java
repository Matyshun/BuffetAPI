package com.CAT.BuffetAPI.Repositories;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.CAT.BuffetAPI.Entities.Booking_restriction;


@RepositoryRestResource
public interface BookingRestrictionRepository extends JpaRepository<Booking_restriction,String> {
	//vease impl para ver implementacion de filtros
	List<Booking_restriction> getData(HashMap<String,Object> Conditions);
}
