package com.CAT.BuffetAPI.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.CAT.BuffetAPI.Entities.Verificationtoken;

@RepositoryRestResource
public interface VerificationTokenRepository extends JpaRepository<Verificationtoken,String> {

	Verificationtoken findByToken(String token);

}
