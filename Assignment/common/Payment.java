package common;

public class Payment {
    private String appointmentID;
    private double BilledAmount;
    private double PaidAmount;

    public Payment(String appointmentID) {
        if (FileUtil.findUserByUsername(appointmentID) != null){
            throw new IllegalArgumentException("Payment already exist for this appointmentID");
        }
        this.appointmentID = appointmentID;
        this.BilledAmount = 0.0;
        this.PaidAmount = 0.0;
    }

    public Payment(String appointmentID, String BilledAmount, String PaidAmount) {
        this.appointmentID = appointmentID;
        this.BilledAmount = Double.parseDouble(BilledAmount);
        this.PaidAmount = Double.parseDouble(PaidAmount);
    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public double getBilledAmount() {
        return BilledAmount;
    }

    public void setBilledAmount(double BilledAmount) {
        this.BilledAmount = BilledAmount;
    }

    public double getPaidAmount() {
        return PaidAmount;
    }

    public void setPaidAmount(double PaidAmount) {
        this.PaidAmount = PaidAmount;
    }
    
    
}
