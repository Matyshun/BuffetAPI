package com.CAT.BuffetAPI.Repositories;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.CAT.BuffetAPI.Entities.Sale;
import com.CAT.BuffetAPI.Entities.Sale_provision;

@RepositoryRestResource
public interface Sale_provisionRepository extends JpaRepository<Sale_provision, String>
{

	//List<Sale> getData(HashMap< String, Object> data);

}
