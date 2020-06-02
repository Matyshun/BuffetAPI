package com.CAT.BuffetAPI.Entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Reserve {
	@Id
	private String reserve_id;
    private String serv_id;
    private String appuser_id;
    private Date start_date_hour;
    private Date end_date_hour;
    private String status_reserve_id;
    private Date updated_at;
    private Date created_at;
    private boolean deleted;
	
    public String getReserve_id() {
		return reserve_id;
	}
	public void setReserve_id(String reserve_id) {
		this.reserve_id = reserve_id;
	}
	public String getServ_id() {
		return serv_id;
	}
	public void setServ_id(String serv_id) {
		this.serv_id = serv_id;
	}
	public String getAppuser_id() {
		return appuser_id;
	}
	public void setAppuser_id(String appuser_id) {
		this.appuser_id = appuser_id;
	}
	public Date getStart_date_hour() {
		return start_date_hour;
	}
	public void setStart_date_hour(Date start_date_hour) {
		this.start_date_hour = start_date_hour;
	}
	public Date getEnd_date_hour() {
		return end_date_hour;
	}
	public void setEnd_date_hour(Date end_date_hour) {
		this.end_date_hour = end_date_hour;
	}
	public String getStatus_reserve_id() {
		return status_reserve_id;
	}
	public void setStatus_reserve_id(String status_reserve_id) {
		this.status_reserve_id = status_reserve_id;
	}
	public Date getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
    
    
}
