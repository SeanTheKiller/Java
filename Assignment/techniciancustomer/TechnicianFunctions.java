package techniciancustomer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class TechnicianFunctions {

    public static File getDataFile(String fileName) {
        File file = new File("Assignment/data/" + fileName);
        if (file.exists()) {
            return file;
        }
        return new File("data/" + fileName);
    }

    // 1. Read all appointments for technician (9 columns)
    public static List<String[]> getJobsForTechnician(String technicianUsername) {
        List<String[]> jobs = new ArrayList<>();
        File file = getDataFile("appointments.txt");

        if (!file.exists()) {
            return jobs;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                // Preserve trailing empty columns
                String[] parts = line.split("\\|", -1);

                // Index 5 is technician username
                if (parts.length >= 6 && parts[5].trim().equalsIgnoreCase(technicianUsername.trim())) {
                    String id = parts[0].trim();
                    String cust = parts.length > 1 ? parts[1].trim() : "";
                    String plate = parts.length > 2 ? parts[2].trim() : "";
                    String service = parts.length > 3 ? parts[3].trim() : "";
                    String datetime = parts.length > 4 ? parts[4].trim() : "";
                    String tech = parts.length > 5 ? parts[5].trim() : "";
                    String status = parts.length > 6 ? parts[6].trim() : "Pending";
                    String comment = parts.length > 7 ? parts[7].trim() : "";
                    String feedback = parts.length > 8 ? parts[8].trim() : "";

                    jobs.add(new String[]{id, cust, plate, service, datetime, tech, status, comment, feedback});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jobs;
    }

    // 2. Update status (index 6) and feedback (index 8)
    public static boolean updateJob(String appointmentId, String newStatus, String newFeedback) {
        File file = getDataFile("appointments.txt");
        if (!file.exists()) {
            return false;
        }

        List<String> allLines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) continue;

                String[] parts = trimmed.split("\\|", -1);

                if (parts.length >= 7 && parts[0].trim().equalsIgnoreCase(appointmentId.trim())) {
                    String cust = parts.length > 1 ? parts[1].trim() : "";
                    String plate = parts.length > 2 ? parts[2].trim() : "";
                    String service = parts.length > 3 ? parts[3].trim() : "";
                    String datetime = parts.length > 4 ? parts[4].trim() : "";
                    String tech = parts.length > 5 ? parts[5].trim() : "";
                    String comment = parts.length > 7 ? parts[7].trim() : "";

                    // Rebuild with updated status and feedback
                    String updated = parts[0].trim() + "|" +
                                     cust + "|" +
                                     plate + "|" +
                                     service + "|" +
                                     datetime + "|" +
                                     tech + "|" +
                                     newStatus.trim() + "|" +
                                     comment + "|" +
                                     (newFeedback == null ? "" : newFeedback.trim());

                    allLines.add(updated);
                    found = true;
                } else {
                    allLines.add(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (!found || allLines.isEmpty()) {
            return false;
        }

        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, false)))) {
            for (String l : allLines) {
                pw.println(l);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. User profile helper
    public static String[] getUserProfile(String username) {
        File file = getDataFile("users.txt");
        if (!file.exists()) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] parts = line.split("\\|", -1);
                if (parts.length >= 4 && parts[0].trim().equalsIgnoreCase(username.trim())) {
                    return new String[]{parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim()};
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // 4. Update profile in users.txt
    public static boolean updateProfile(String username, String newPassword, String newPhone) {
        File file = getDataFile("users.txt");
        if (!file.exists()) return false;

        List<String> allLines = new ArrayList<>();
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) continue;

                String[] parts = trimmed.split("\\|", -1);
                if (parts.length >= 4 && parts[0].trim().equalsIgnoreCase(username.trim())) {
                    String updated = parts[0].trim() + "|" +
                                     newPassword.trim() + "|" +
                                     parts[2].trim() + "|" +
                                     newPhone.trim();
                    allLines.add(updated);
                    found = true;
                } else {
                    allLines.add(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (!found || allLines.isEmpty()) return false;

        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, false)))) {
            for (String l : allLines) {
                pw.println(l);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}