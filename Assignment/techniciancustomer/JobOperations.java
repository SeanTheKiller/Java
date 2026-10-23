package techniciancustomer;

import java.util.List;

// Interface OOP advanced concepts 
public interface JobOperations {
    List<String[]> getAssignedJobs(String technicianUsername);
    boolean updateJobRecord(String appointmentId, String newStatus, String newFeedback);
}