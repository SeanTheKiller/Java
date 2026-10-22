package common;

/**
 * A comment a customer leaves about a technician or counter staff member
 * for one of their appointments. Stored in data/comments.txt:
 *
 *   commentId|appointmentId|customerUsername|targetRole|targetUsername|comment|date
 *   CMT001|APT012|mike|Technician|sean|Very friendly and explained everything|2026-10-22
 *
 * Kept in "common" because the manager (view all comments) and the
 * technician (check comments on their jobs) also need to read it.
 */
public class Comment {

    private String commentId;
    private String appointmentId;
    private String customerUsername;
    private String targetRole;       // "Technician" or "Counter"
    private String targetUsername;
    private String text;
    private String date;             // yyyy-MM-dd

    public Comment(String commentId, String appointmentId, String customerUsername,
                   String targetRole, String targetUsername, String text, String date) {
        String[] fields = {commentId, appointmentId, customerUsername, targetRole, targetUsername, text, date};
        for (String f : fields) {
            if (f == null || f.trim().isEmpty()) {
                throw new IllegalArgumentException("Comment field is empty");
            }
            if (f.contains("|")) {
                throw new IllegalArgumentException("Comment cannot contain '|'");
            }
        }
        this.commentId = commentId.trim();
        this.appointmentId = appointmentId.trim();
        this.customerUsername = customerUsername.trim();
        this.targetRole = targetRole.trim();
        this.targetUsername = targetUsername.trim();
        this.text = text.trim();
        this.date = date.trim();
    }

    public static Comment fromFileString(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length < 7) {
            return null;
        }
        try {
            return new Comment(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
        } catch (IllegalArgumentException ex) {
            System.out.println("Skipping invalid comment record: " + line);
            return null;
        }
    }

    public String toFileString() {
        return commentId + "|" + appointmentId + "|" + customerUsername + "|" + targetRole + "|"
                + targetUsername + "|" + text + "|" + date;
    }

    public String getCommentId()        { return commentId; }
    public String getAppointmentId()    { return appointmentId; }
    public String getCustomerUsername() { return customerUsername; }
    public String getTargetRole()       { return targetRole; }
    public String getTargetUsername()   { return targetUsername; }
    public String getText()             { return text; }
    public String getDate()             { return date; }
}
