package com.CAT.BuffetAPI.Repositories;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.CAT.BuffetAPI.Entities.Reserve_restriction;


@RepositoryRestResource
public interface ReserveRestrictionRepository extends JpaRepository<Reserve_restriction,String> {
	List<Reserve_restriction> getData(HashMap<String,Object> Conditions);
}
