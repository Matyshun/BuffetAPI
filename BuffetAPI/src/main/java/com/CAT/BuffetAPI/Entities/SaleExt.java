package com.CAT.BuffetAPI.Entities;

public class SaleExt extends Sale {
    
    private Sale_status status;
    private App_user user;
    private App_user cashier;
    private App_user seller;
	
    public Sale_status getStatus() {
		return status;
	}
	public void setStatus(Sale_status status) {
		this.status = status;
	}
	public App_user getUser() {
		return user;
	}
	public void setUser(App_user user) {
		this.user = user;
	}
	public App_user getCashier() {
		return cashier;
	}
	public void setCashier(App_user cashier) {
		this.cashier = cashier;
	}
	public App_user getSeller() {
		return seller;
	}
	public void setSeller(App_user seller) {
		this.seller = seller;
	}
    
    

}