package com.CAT.BuffetAPI.Services;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CAT.BuffetAPI.Entities.Reserve;
import com.CAT.BuffetAPI.Entities.Reserve_restriction;
import com.CAT.BuffetAPI.Entities.Status_reserve;
import com.CAT.BuffetAPI.Entities.Store_schedule;
import com.CAT.BuffetAPI.Repositories.ReserveRepository;
import com.CAT.BuffetAPI.Repositories.ReserveRestrictionRepository;
import com.CAT.BuffetAPI.Repositories.Status_reserveRepository;
import com.CAT.BuffetAPI.Repositories.StoreScheduleRepository;


@Service
public class ReservasService {


	@Autowired
	private ReserveRepository reserveRepo;
	@Autowired
	private ReserveRestrictionRepository reserveResRepo;
	@Autowired
	private Status_reserveRepository  statusRepo;
	@Autowired
	private StoreScheduleRepository scheduleRepo;

	//GetAll
	public List<Reserve> getAllReserve(HashMap<String, Object> data)
	{
		return reserveRepo.getData(data);
	}
	
	public List<Reserve_restriction> getAllReserveRestrictions(HashMap<String, Object> data)
	{
		return reserveResRepo.getData(data);
	}
	
	public List<Status_reserve> getAllStatusReserve()
	{
		return statusRepo.findAll();
	}
	
	public List<Store_schedule> getAllStoreSchedule()
	{
		return scheduleRepo.findAll();
	}
	
	//GetOne
	public Optional<Reserve> getOneReserve(String Id)
	{
		return reserveRepo.findById(Id);
	}
	public Optional<Reserve_restriction> getOneReserveRestriction(String Id)
	{
		return reserveResRepo.findById(Id);
	}
	public Optional<Status_reserve> getOneStatusReserve(String Id)
	{
		return statusRepo.findById(Id);
	}
	public Optional<Store_schedule> getOneSchedule(String Id)
	{
		return scheduleRepo.findById(Id);
	}
	
	//Update-save
	
	public void updateReserve(Reserve entity)
	{
		reserveRepo.save(entity);
	}
	public void updateReserveRestriction(Reserve_restriction entity)
	{
		reserveResRepo.save(entity);
	}
	public void updateStatusReserve(Status_reserve entity)
	{
		statusRepo.save(entity);
	}
	public void updateStoreSchedule(Store_schedule entity)
	{
		scheduleRepo.save(entity);
	}
	
	//Delete
	
	public void deleteReserve(Reserve entity)
	{
		reserveRepo.delete(entity);
	}
	public void deleteReserveRestriction(Reserve_restriction entity)
	{
		reserveResRepo.delete(entity);
	}
	public void deleteStatusReserve(Status_reserve entity)
	{
		statusRepo.delete(entity);
	}
	public void deleteStoreSchedule(Store_schedule entity)
	{
		scheduleRepo.delete(entity);
	}
}
