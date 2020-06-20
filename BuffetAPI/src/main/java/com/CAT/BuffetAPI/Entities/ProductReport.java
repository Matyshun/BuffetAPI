package com.CAT.BuffetAPI.Entities;

public class ProductReport extends Product {

	private int prod_n = 0;
	private int prod_total = 0;

	public int getProd_n() {
		return prod_n;
	}
	public void setProd_n(int prod_n) {
		this.prod_n = prod_n;
	}
	public int getProd_total() {
		return prod_total;
	}
	public void setProd_total(int prod_total) {
		this.prod_total = prod_total;
	}


}