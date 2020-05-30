package com.CAT.BuffetAPI.Services;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CAT.BuffetAPI.Entities.Sale;
import com.CAT.BuffetAPI.Entities.Sale_provision;
import com.CAT.BuffetAPI.Entities.Sale_status;
import com.CAT.BuffetAPI.Repositories.SaleRepository;
import com.CAT.BuffetAPI.Repositories.Sale_provisionRepository;
import com.CAT.BuffetAPI.Repositories.Sale_statusRepository;

@Service
public class SaleService {

	@Autowired
	SaleRepository saleRepo;
	@Autowired
	Sale_provisionRepository saleProvRepo;
	@Autowired
	Sale_statusRepository statusRepo;
	
	public List<Sale> getAllSale(HashMap< String, Object> data)
	{
		return saleRepo.getData(data);
	}
	
	public void updateSale(Sale sale)
	{
		saleRepo.save(sale);
	}
	
	public void deleteSale(Sale sale)
	{
		saleRepo.delete(sale);
	}
	
	public Optional<Sale> getOneSale(String id) {
		return saleRepo.findById(id);
	}
	
	public List<Sale_provision> getAllProvision()
	{
		return saleProvRepo.findAll();
	}
	
	public void updateSaleProvision(Sale_provision prov)
	{
		 saleProvRepo.save(prov);
	}
	
	public void deleteSaleProvision(Sale_provision prov)
	{
		saleProvRepo.delete(prov);
	}
	
	public Optional<Sale_provision> getOneProvision(String id) {
		return saleProvRepo.findById(id);
	}
	
	
	public List<Sale_status> getAllStatus()
	{
		return statusRepo.findAll();
	}
}
