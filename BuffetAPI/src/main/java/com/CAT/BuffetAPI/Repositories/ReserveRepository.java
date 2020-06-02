package com.CAT.BuffetAPI.Repositories;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.CAT.BuffetAPI.Entities.App_user;
import com.CAT.BuffetAPI.Entities.Publication;
import com.CAT.BuffetAPI.Entities.Reserve;

@RepositoryRestResource
public interface ReserveRepository extends JpaRepository<Reserve, String>{
	
	
	List<Reserve> getData(HashMap<String,Object> Conditions);
}
