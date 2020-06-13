package com.CAT.BuffetAPI.Entities;

public class ServiceExt extends Service {
    
    private Service_status status;

    public Service_status getStatus() {
        return this.status;
    }

    public void setStatus(Service_status status) {
        this.status = status;
    }

}