package manager;

import common.Appointment;
import common.Comment;
import common.FileUtil;
import common.Payment;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class ReportFunctions {

    private ReportFunctions() {
    }

    public static ReportData loadReport() {
        List<Appointment> appointments = FileUtil.loadAllAppointments();
        FileUtil.loadAllPayments();

        Map<String, Payment> paymentsByAppointment = new LinkedHashMap<>();
        for (Payment payment : FileUtil.allPayment) {
            paymentsByAppointment.put(payment.getAppointmentID(), payment);
        }

        ReportData report = new ReportData();
        for (Appointment appointment : appointments) {
            report.totalAppointments++;
            increment(report.appointmentsByStatus, appointment.getStatus());
            increment(report.appointmentsByService, appointment.getServiceType());

            String technician = appointment.getTechnician();
            if (!technician.isEmpty()) {
                StaffReport staff = report.staffReports.get(technician);
                if (staff == null) {
                    staff = new StaffReport(technician);
                    report.staffReports.put(technician, staff);
                }
                staff.assigned++;
                if ("Completed".equalsIgnoreCase(appointment.getStatus())) {
                    staff.completed++;
                } else if ("In Progress".equalsIgnoreCase(appointment.getStatus())) {
                    staff.inProgress++;
                } else if ("Pending".equalsIgnoreCase(appointment.getStatus())) {
                    staff.pending++;
                }
            }

            Payment payment = paymentsByAppointment.get(appointment.getAppointmentId());
            if (payment != null) {
                report.totalBilled += payment.getBilledAmount();
                report.totalPaid += payment.getPaidAmount();
                if (payment.getPaidAmount() >= payment.getBilledAmount()
                        && payment.getBilledAmount() > 0) {
                    report.fullyPaid++;
                } else if (payment.getPaidAmount() > 0) {
                    report.partiallyPaid++;
                } else {
                    report.unpaid++;
                }
            }
        }

        return report;
    }

    public static List<FeedbackRow> loadFeedbackRows(List<Appointment> appointments) {
        List<FeedbackRow> rows = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (!appointment.getCustomerComment().isEmpty()) {
                rows.add(new FeedbackRow(appointment.getAppointmentId(),
                        appointment.getCustomerName(), "Customer feedback",
                        appointment.getCustomerComment()));
            }
            if (!appointment.getTechnicianFeedback().isEmpty()) {
                rows.add(new FeedbackRow(appointment.getAppointmentId(),
                        appointment.getCustomerName(), "Technician feedback",
                        appointment.getTechnicianFeedback()));
            }
        }

        if (dataFileExists("feedback.txt")) {
            List<String> feedbackLines = FileUtil.read("feedback.txt");
            for (int i = 1; i < feedbackLines.size(); i++) {
                String line = feedbackLines.get(i).trim();
                if (!line.isEmpty()) {
                    rows.add(new FeedbackRow("-", "-", "Feedback", line));
                }
            }
        }

        if (dataFileExists("comments.txt")) {
            List<String> commentLines = FileUtil.read("comments.txt");
            for (int i = 1; i < commentLines.size(); i++) {
                Comment comment = Comment.fromFileString(commentLines.get(i));
                if (comment != null) {
                    rows.add(new FeedbackRow(comment.getAppointmentId(),
                            comment.getCustomerUsername(),
                            comment.getTargetRole() + ": " + comment.getTargetUsername(),
                            comment.getText()));
                }
            }
        }
        return rows;
    }

    private static boolean dataFileExists(String fileName) {
        return Files.isRegularFile(Paths.get(System.getProperty("user.dir"), "data", fileName));
    }

    private static void increment(Map<String, Integer> values, String key) {
        Integer count = values.get(key);
        values.put(key, count == null ? 1 : count + 1);
    }

    public static final class ReportData {
        private int totalAppointments;
        private double totalBilled;
        private double totalPaid;
        private int fullyPaid;
        private int partiallyPaid;
        private int unpaid;
        private final Map<String, Integer> appointmentsByStatus = new LinkedHashMap<>();
        private final Map<String, Integer> appointmentsByService = new LinkedHashMap<>();
        private final Map<String, StaffReport> staffReports = new LinkedHashMap<>();

        public int getTotalAppointments() {
            return totalAppointments;
        }

        public double getTotalBilled() {
            return totalBilled;
        }

        public double getTotalPaid() {
            return totalPaid;
        }

        public double getOutstanding() {
            return totalBilled - totalPaid;
        }

        public int getFullyPaid() {
            return fullyPaid;
        }

        public int getPartiallyPaid() {
            return partiallyPaid;
        }

        public int getUnpaid() {
            return unpaid;
        }

        public Map<String, Integer> getAppointmentsByStatus() {
            return appointmentsByStatus;
        }

        public Map<String, Integer> getAppointmentsByService() {
            return appointmentsByService;
        }

        public Map<String, StaffReport> getStaffReports() {
            return staffReports;
        }

    }

    public static final class StaffReport {
        private final String technician;
        private int assigned;
        private int completed;
        private int inProgress;
        private int pending;

        private StaffReport(String technician) {
            this.technician = technician;
        }

        public String getTechnician() {
            return technician;
        }

        public int getAssigned() {
            return assigned;
        }

        public int getCompleted() {
            return completed;
        }

        public int getInProgress() {
            return inProgress;
        }

        public int getPending() {
            return pending;
        }

        public double getCompletionRate() {
            return assigned == 0 ? 0 : completed * 100.0 / assigned;
        }
    }

    public static final class FeedbackRow {
        private final String appointmentId;
        private final String customer;
        private final String source;
        private final String text;

        private FeedbackRow(String appointmentId, String customer, String source, String text) {
            this.appointmentId = appointmentId;
            this.customer = customer;
            this.source = source;
            this.text = text;
        }

        public String getAppointmentId() {
            return appointmentId;
        }

        public String getCustomer() {
            return customer;
        }

        public String getSource() {
            return source;
        }

        public String getText() {
            return text;
        }
    }
}
