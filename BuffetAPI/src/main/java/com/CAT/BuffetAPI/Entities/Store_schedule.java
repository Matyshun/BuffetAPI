package com.CAT.BuffetAPI.Entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Store_schedule {
	@Id
	private String schedule_id;
	private Date start_hour;
	private Date start_lunch_hour;
	private Date end_lunch_hour;
	private Date end_hour;
	private Date updated_at;
	private Date created_at;
	private boolean deleted;

	public String getSchedule_id() {
		return schedule_id;
	}
	public void setSchedule_id(String schedule_id) {
		this.schedule_id = schedule_id;
	}
	public Date getStart_hour() {
		return start_hour;
	}
	public void setStart_hour(Date start_hour) {
		this.start_hour = start_hour;
	}
	public Date getStart_lunch_hour() {
		return start_lunch_hour;
	}
	public void setStart_lunch_hour(Date start_lunch_hour) {
		this.start_lunch_hour = start_lunch_hour;
	}
	public Date getEnd_lunch_hour() {
		return end_lunch_hour;
	}
	public void setEnd_lunch_hour(Date end_lunch_hour) {
		this.end_lunch_hour = end_lunch_hour;
	}
	public Date getEnd_hour() {
		return end_hour;
	}
	public void setEnd_hour(Date end_hour) {
		this.end_hour = end_hour;
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
