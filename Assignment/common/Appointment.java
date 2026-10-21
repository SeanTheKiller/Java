package common;

public class Appointment {
    String technician;
    String customer;
    String datetime; // format: DD/MM/YYY 24:00
    String serviceType; // normal/major service
    String status; //Completed/missed/incomplete

    public Appointment(String technician, String customer, String datetime, String serviceType) {
        this.technician = technician;
        this.customer = customer;
        this.datetime = datetime;
        this.serviceType = serviceType;
        this.status = "Incomplete";
    }

    public String getTechnician() {
        return technician;
    }

    public void setTechnician(String technician) {
        this.technician = technician;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}
