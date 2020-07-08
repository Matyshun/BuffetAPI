package com.CAT.BuffetAPI.Entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Service {
	@Id
	@GeneratedValue(generator="db-guid")
	@GenericGenerator(name="db-guid", strategy = "guid") 
	private String serv_id;	
	private String name;
	private int price;
	private String serv_desc;
	private int estimated_time;
	private String serv_status;
	private boolean is_recurring;
	private Date created_at;
	private Date updated_at;
	private boolean deleted;

	public String getServ_id() {
		return serv_id;
	}
	public void setServ_id(String serv_id) {
		this.serv_id = serv_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getServ_desc() {
		return serv_desc;
	}
	public void setServ_desc(String serv_desc) {
		this.serv_desc = serv_desc;
	}
	public int getEstimated_time() {
		return estimated_time;
	}
	public void setEstimated_time(int estimated_time) {
		this.estimated_time = estimated_time;
	}

	public Date getCreated_at() {
		return this.created_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Date getUpdated_at() {
		return this.updated_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public String getServ_status() {
		return serv_status;
	}
	public void setServ_status(String serv_status) {
		this.serv_status = serv_status;
	}
	public boolean isIs_recurring() {
		return is_recurring;
	}
	public void setIs_recurring(boolean is_recurring) {
		this.is_recurring = is_recurring;
	}
	
	


}
