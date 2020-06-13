package com.CAT.BuffetAPI.Entities;

public class PublicationExt extends Publication {
    
    private App_user user;
    private Public_status status;

    public App_user getUser() {
        return this.user;
    }

    public void setUser(App_user user) {
        this.user = user;
    }

    public Public_status getStatus() {
        return this.status;
    }

    public void setStatus(Public_status status) {
        this.status = status;
    }
    
}