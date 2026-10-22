package customer;

import common.Appointment;
import common.Comment;
import common.Payment;
import java.util.List;

/**
 * Interface: a "contract" listing everything a customer is allowed to do.
 * CustomerFunctions must implement every method declared here.
 */
public interface CustomerOperations {

    List<Appointment> getServiceHistory();          // all my appointments

    List<Payment> getPaymentHistory();              // all my payments

    List<Appointment> getAppointmentsWithFeedback(); // my appointments that have technician feedback

    List<Comment> getMyComments();                  // comments I already left

    void addComment(String appointmentId, String targetRole, String targetUsername, String text);
}
