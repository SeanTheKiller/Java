package common;

import java.util.List;

public class Payment {
    private String appointmentID;
    private double BilledAmount;
    private double PaidAmount;
    private String counterStaff;
    private String paymentMethod;

    public Payment(String appointmentID) {
        if (FileUtil.findPaymentByID(appointmentID) != null){
            throw new IllegalArgumentException("Payment already exist for this appointmentID");
        }
        this.appointmentID = appointmentID;
        this.BilledAmount = 0.0;
        this.PaidAmount = 0.0;
        this.counterStaff = "";
        this.paymentMethod = "";
    }

    public Payment(String appointmentID, String BilledAmount, String PaidAmount) {
        this.appointmentID = appointmentID;
        this.BilledAmount = Double.parseDouble(BilledAmount);
        this.PaidAmount = Double.parseDouble(PaidAmount);
        this.counterStaff = "";
        this.paymentMethod = "";
    }

    public Payment(String appointmentID, String BilledAmount, String PaidAmount, String counterStaff, String paymentMethod) {
        this.appointmentID = appointmentID;
        this.BilledAmount = Double.parseDouble(BilledAmount);
        this.PaidAmount = Double.parseDouble(PaidAmount);
        this.counterStaff = counterStaff;
        this.paymentMethod = paymentMethod;
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

    public String getCounterStaff() {
        return counterStaff;
    }

    public void setCounterStaff(String counterStaff) {
        this.counterStaff = counterStaff;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    // alias: some code refers to the id in camelCase
    public String getAppointmentId() {
        return appointmentID;
    }

    // alias of getPaymentMethod
    public String getMethod() {
        return paymentMethod;
    }

    // payments.txt has no date column; derive it from the linked appointment's datetime.
    // returns "" if the appointment cannot be found
    public String getPaymentDate() {
        return findAppointmentColumn(appointmentID, 4).replace("T", " ");
    }

    // appointments.txt links appointmentId -> customerName (which stores the customer's username).
    // returns "" if the appointment cannot be found
    public String getCustomerUsername() {
        return findAppointmentColumn(appointmentID, 1);
    }

    // searches appointments.txt WITHOUT relying on FileUtil.allAppointments being loaded
    private static String findAppointmentColumn(String targetId, int column) {
        if (targetId == null || targetId.isEmpty()) {
            return "";
        }
        List<String> lines = FileUtil.read("appointments.txt");
        for (int i = 1; i < lines.size(); i++) {   // i = 1 skips the header line
            String line = lines.get(i);
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] p = line.split("\\|", -1);
            if (p.length >= 9 && p[0].equalsIgnoreCase(targetId)) {
                return p[column];
            }
        }
        return "";
    }

    // builds a Payment from one payments.txt line:
    // appointmentId|billedAmount|paidAmount|counterStaff|paymentMethod
    // (also accepts the 3-column legacy format). returns null if invalid
    public static Payment fromFileString(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 3) {
            return null;
        }
        try {
            if (parts.length >= 5) {
                return new Payment(parts[0], parts[1], parts[2], parts[3], parts[4]);
            }
            return new Payment(parts[0], parts[1], parts[2]);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }


}
