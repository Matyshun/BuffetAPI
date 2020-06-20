package com.CAT.BuffetAPI.Entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaleReport{

	public List<SaleExt> sale_list = new ArrayList<SaleExt>();
	public List<ProductReport> prod_sold_list= new ArrayList<ProductReport>();
	public List<ServiceReport> serv_sold_list= new ArrayList<ServiceReport>();
	private int total = 0;
	private Date date_start;
	private Date date_end;

	public Date getDate_start() {
		return date_start;
	}
	public void setDate_start(Date date_start) {
		this.date_start = date_start;
	}
	public Date getDate_end() {
		return date_end;
	}
	public void setDate_end(Date date_end) {
		this.date_end = date_end;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	

}