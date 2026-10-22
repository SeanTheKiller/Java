package customer;

import common.Appointment;
import common.Comment;
import common.FileUtil;
import common.Payment;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * All the "logic" for the customer role: reading the text files and
 * picking out only the records that belong to the logged-in customer.
 * The GUI (CustomerFrame) calls these methods; it never reads files itself.
 *
 * NOTE: appointments are linked to a customer through the customerName
 * column of appointments.txt, which must hold the customer's USERNAME.
 */
public class CustomerFunctions implements CustomerOperations {

    private static final int MAX_COMMENT_LENGTH = 200;

    private final String username;   // the logged-in customer

    public CustomerFunctions(String username) {
        this.username = username;
    }

    // ------------------------------------------------------------------
    // Loading files (static so other roles, e.g. manager, can reuse them)
    // ------------------------------------------------------------------

    public static List<Appointment> loadAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        List<String> lines = FileUtil.read("appointments.txt");
        for (int i = 1; i < lines.size(); i++) {          // i = 1 skips the header line
            String line = lines.get(i);
            if (line.trim().isEmpty()) continue;
            // -1 keeps empty columns at the end (e.g. a blank technicianFeedback)
            String[] p = line.split("\\|", -1);
            if (p.length >= 9) {
                list.add(new Appointment(p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7], p[8]));
            }
        }
        return list;
    }

    public static List<Payment> loadAllPayments() {
        List<Payment> list = new ArrayList<>();
        List<String> lines = FileUtil.read("payments.txt");
        for (int i = 1; i < lines.size(); i++) {
            if (lines.get(i).trim().isEmpty()) continue;
            Payment p = Payment.fromFileString(lines.get(i));
            if (p != null) list.add(p);
        }
        return list;
    }

    public static List<Comment> loadAllComments() {
        List<Comment> list = new ArrayList<>();
        List<String> lines = FileUtil.read("comments.txt");
        for (int i = 1; i < lines.size(); i++) {
            if (lines.get(i).trim().isEmpty()) continue;
            Comment c = Comment.fromFileString(lines.get(i));
            if (c != null) list.add(c);
        }
        return list;
    }

    // ------------------------------------------------------------------
    // CustomerOperations methods
    // ------------------------------------------------------------------

    @Override
    public List<Appointment> getServiceHistory() {
        List<Appointment> mine = new ArrayList<>();
        for (Appointment a : loadAllAppointments()) {
            if (a.getCustomerName().equalsIgnoreCase(username)) {
                mine.add(a);
            }
        }
        return mine;
    }

    @Override
    public List<Payment> getPaymentHistory() {
        List<Payment> mine = new ArrayList<>();
        for (Payment p : loadAllPayments()) {
            if (p.getCustomerUsername().equalsIgnoreCase(username)) {
                mine.add(p);
            }
        }
        return mine;
    }

    @Override
    public List<Appointment> getAppointmentsWithFeedback() {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : getServiceHistory()) {
            if (!a.getTechnicianFeedback().isEmpty()) {
                result.add(a);
            }
        }
        return result;
    }

    @Override
    public List<Comment> getMyComments() {
        List<Comment> mine = new ArrayList<>();
        for (Comment c : loadAllComments()) {
            if (c.getCustomerUsername().equalsIgnoreCase(username)) {
                mine.add(c);
            }
        }
        return mine;
    }

    // Only completed appointments can be commented on
    public List<Appointment> getCompletedAppointments() {
        List<Appointment> result = new ArrayList<>();
        for (Appointment a : getServiceHistory()) {
            if (a.getStatus().equalsIgnoreCase("Completed")) {
                result.add(a);
            }
        }
        return result;
    }

    // Total amount paid - used for the summary label in the Payments tab
    public double getTotalPaid() {
        double total = 0;
        for (Payment p : getPaymentHistory()) {
            total += p.getAmount();
        }
        return total;
    }

    // The counter staff "involved" in an appointment = whoever collected its payment.
    // Returns null if the appointment has not been paid yet.
    public String getCounterStaffFor(String appointmentId) {
        for (Payment p : loadAllPayments()) {
            if (p.getAppointmentId().equalsIgnoreCase(appointmentId)) {
                return p.getCounterStaff();
            }
        }
        return null;
    }

    @Override
    public void addComment(String appointmentId, String targetRole, String targetUsername, String text) {
        // ---------- input validation ----------
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment cannot be empty");
        }
        text = text.trim().replace("\n", " ");  // one record must stay on one line
        if (text.contains("|")) {
            throw new IllegalArgumentException("Comment cannot contain '|'");
        }
        if (text.length() > MAX_COMMENT_LENGTH) {
            throw new IllegalArgumentException("Comment must be " + MAX_COMMENT_LENGTH + " characters or less");
        }

        // the appointment must belong to this customer AND be completed
        Appointment target = null;
        for (Appointment a : getCompletedAppointments()) {
            if (a.getAppointmentId().equalsIgnoreCase(appointmentId)) {
                target = a;
            }
        }
        if (target == null) {
            throw new IllegalArgumentException("You can only comment on your own completed appointments");
        }

        // the staff member must actually have been involved in that appointment
        if (targetRole.equals("Technician")) {
            if (!target.getTechnician().equalsIgnoreCase(targetUsername)) {
                throw new IllegalArgumentException("That technician did not handle this appointment");
            }
        } else if (targetRole.equals("Counter")) {
            String staff = getCounterStaffFor(appointmentId);
            if (staff == null || !staff.equalsIgnoreCase(targetUsername)) {
                throw new IllegalArgumentException("That counter staff did not handle this appointment");
            }
        } else {
            throw new IllegalArgumentException("Unknown staff role: " + targetRole);
        }

        // only one comment per staff member per appointment
        List<Comment> all = loadAllComments();
        for (Comment c : all) {
            if (c.getAppointmentId().equalsIgnoreCase(appointmentId)
                    && c.getCustomerUsername().equalsIgnoreCase(username)
                    && c.getTargetUsername().equalsIgnoreCase(targetUsername)) {
                throw new IllegalArgumentException("You already commented on " + targetUsername
                        + " for " + appointmentId);
            }
        }

        // ---------- create and save ----------
        String newId = String.format("CMT%03d", all.size() + 1);
        Comment c = new Comment(newId, target.getAppointmentId(), username, targetRole,
                targetUsername, text, LocalDate.now().toString());
        all.add(c);

        List<String> lines = new ArrayList<>();
        lines.add("commentId|appointmentId|customerUsername|targetRole|targetUsername|comment|date");
        for (Comment each : all) {
            lines.add(each.toFileString());
        }
        FileUtil.write("comments.txt", lines);
    }
}
