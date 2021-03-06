package com.CAT.BuffetAPI.Repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.CAT.BuffetAPI.Entities.App_user;
import com.CAT.BuffetAPI.Entities.Booking;
import com.CAT.BuffetAPI.Entities.Booking_restriction;

public class BookingRestrictionRepositoryImpl {

	@PersistenceContext
	private EntityManager em;
	
	
	public List<Booking_restriction> getData(HashMap<String, Object> conditions)
	{
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Booking_restriction> query= cb.createQuery(Booking_restriction.class);
		Root<Booking_restriction> root = query.from(Booking_restriction.class);
		List<Predicate> predicates = new ArrayList<>();
		conditions.forEach((field,value) ->
		{
			switch (field)
			{
				
				case "serv_id":
					predicates.add(cb.like(root.get(field),"%"+(String)value+"%"));
					break;
				case "deleted":
					if(value.equals("true"))
					{
					predicates.add(cb.equal(root.get("deleted"), 1));
					break;
					}
					else
					{
						predicates.add(cb.equal(root.get("deleted"), 0));
					}	
			}
			
		});
		
		query.select(root).where(predicates.toArray(new Predicate[predicates.size()]));
		return em.createQuery(query).getResultList(); 		
	}
}
