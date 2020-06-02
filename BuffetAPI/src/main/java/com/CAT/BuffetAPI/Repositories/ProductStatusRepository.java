package com.CAT.BuffetAPI.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.CAT.BuffetAPI.Entities.Product_status;


@RepositoryRestResource
public interface ProductStatusRepository extends JpaRepository<Product_status,String> {

}
