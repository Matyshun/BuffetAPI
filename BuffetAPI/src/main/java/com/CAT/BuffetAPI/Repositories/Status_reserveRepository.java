package com.CAT.BuffetAPI.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.CAT.BuffetAPI.Entities.Product_status;
import com.CAT.BuffetAPI.Entities.Sale_status;
import com.CAT.BuffetAPI.Entities.Status_reserve;


@RepositoryRestResource
public interface Status_reserveRepository extends JpaRepository<Status_reserve,String> {

}
