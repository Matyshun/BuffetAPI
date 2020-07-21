package com.CAT.BuffetAPI.Repositories;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.CAT.BuffetAPI.Entities.Sale;
import com.CAT.BuffetAPI.Entities.Sale_provision;

@RepositoryRestResource
public interface SaleRepository extends JpaRepository<Sale, String>
{
	//vease impl para ver implementacion de filtros
	List<Sale> getData(HashMap< String, Object> data);

}
