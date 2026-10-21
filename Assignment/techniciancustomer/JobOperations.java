package techniciancustomer;

import java.util.List;

// Interface fulfilling APU OOP advanced concepts requirement
public interface JobOperations {
    List<String[]> getAssignedJobs(String technicianUsername);
    boolean updateJobRecord(String appointmentId, String newStatus, String newFeedback);
}