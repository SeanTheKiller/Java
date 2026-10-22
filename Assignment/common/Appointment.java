package common;

public class Appointment {
    String appointmentId;
    String customerName;
    String carPlate;
    String serviceType;
    String datetime; // ISO format: yyyy-MM-dd'T'HH:mm
    String technician;
    String status; // Pending / In Progress / Completed
    String customerComment;
    String technicianFeedback;

    public static int idCounter = 0;

    public Appointment(String appointmentId, String customerName, String carPlate, String serviceType,
                       String datetime, String technician, String status, String customerComment, String technicianFeedback) {
        this.appointmentId = appointmentId.trim();
        this.customerName = customerName.trim();
        this.carPlate = carPlate.trim();
        this.serviceType = serviceType.trim();
        this.datetime = datetime.trim();
        this.technician = technician.trim();
        this.status = status.trim();
        this.customerComment = customerComment.trim();
        this.technicianFeedback = technicianFeedback.trim();
        syncIdCounter(this.appointmentId);
    }

    public Appointment(String customerName, String carPlate, String serviceType,
                       String datetime, String technician, String customerComment) {
        this(generateAppointmentId(), customerName, carPlate, serviceType,
             datetime, technician, "Pending", customerComment, "");
    }

    public static String generateAppointmentId() {
        idCounter++;
        return String.format("APT%03d", idCounter);
    }

    public static void syncIdCounter(String existingId) {
        if (existingId == null) return;
        String id = existingId.trim().toUpperCase();
        if (id.startsWith("APT")) {
            try {
                int num = Integer.parseInt(id.substring(3));
                if (num > idCounter) idCounter = num;
            } catch (NumberFormatException e) {}
        }
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
        syncIdCounter(appointmentId);
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getTechnician() {
        return technician;
    }

    public void setTechnician(String technician) {
        this.technician = technician;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerComment() {
        return customerComment;
    }

    public void setCustomerComment(String customerComment) {
        this.customerComment = customerComment;
    }

    public String getTechnicianFeedback() {
        return technicianFeedback;
    }

    public void setTechnicianFeedback(String technicianFeedback) {
        this.technicianFeedback = technicianFeedback;
    }

    public String toFileString() {
        return appointmentId + "|" + customerName + "|" + carPlate + "|" + serviceType + "|"
             + datetime + "|" + technician + "|" + status + "|" + customerComment + "|" + technicianFeedback;
    }
}
