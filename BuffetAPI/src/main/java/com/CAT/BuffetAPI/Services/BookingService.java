package com.CAT.BuffetAPI.Services;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CAT.BuffetAPI.Entities.Booking;
import com.CAT.BuffetAPI.Entities.Booking_restriction;
import com.CAT.BuffetAPI.Entities.Status_booking;
import com.CAT.BuffetAPI.Entities.Store_schedule;
import com.CAT.BuffetAPI.Repositories.BookingRepository;
import com.CAT.BuffetAPI.Repositories.BookingRestrictionRepository;
import com.CAT.BuffetAPI.Repositories.Status_bookingRepository;
import com.CAT.BuffetAPI.Repositories.StoreScheduleRepository;


@Service
public class BookingService {


	@Autowired
	private BookingRepository bookingRepo;
	@Autowired
	private BookingRestrictionRepository bookingResRepo;
	@Autowired
	private Status_bookingRepository  statusRepo;
	@Autowired
	private StoreScheduleRepository scheduleRepo;

	//GetAll
	public List<Booking> getAllBooking(HashMap<String, Object> data)
	{
		return bookingRepo.getData(data);
	}
	
	public List<Booking_restriction> getAllBookingRestrictions(HashMap<String, Object> data)
	{
		return bookingResRepo.getData(data);
	}
	
	public List<Status_booking> getAllStatusBooking()
	{
		return statusRepo.findAll();
	}
	
	public List<Store_schedule> getAllStoreSchedule()
	{
		return scheduleRepo.findAll();
	}
	
	//GetOne
	public Optional<Booking> getOneBooking(String Id)
	{
		return bookingRepo.findById(Id);
	}
	public Optional<Booking_restriction> getOneBookingRestriction(String Id)
	{
		return bookingResRepo.findById(Id);
	}
	public Optional<Status_booking> getOneStatusBooking(String Id)
	{
		return statusRepo.findById(Id);
	}
	public Optional<Store_schedule> getOneSchedule(String Id)
	{
		return scheduleRepo.findById(Id);
	}
	
	//Update-save
	
	public void updateBooking(Booking entity)
	{
		bookingRepo.save(entity);
	}
	public void updateBookingRestriction(Booking_restriction entity)
	{
		bookingResRepo.save(entity);
	}
	public void updateStatusBooking(Status_booking entity)
	{
		statusRepo.save(entity);
	}
	public void updateStoreSchedule(Store_schedule entity)
	{
		scheduleRepo.save(entity);
	}
	
	//Delete
	
	public void deleteBooking(Booking entity)
	{
		bookingRepo.delete(entity);
	}
	public void deleteBookingRestriction(Booking_restriction entity)
	{
		bookingResRepo.delete(entity);
	}
	public void deleteStatusBooking(Status_booking entity)
	{
		statusRepo.delete(entity);
	}
	public void deleteStoreSchedule(Store_schedule entity)
	{
		scheduleRepo.delete(entity);
	}
}
