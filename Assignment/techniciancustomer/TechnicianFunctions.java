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

    // Read all appointments assigned to the logged-in technician
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

                String[] parts = line.split("\\|");
                // Check if index 4 matches the technician username
                if (parts.length >= 8 && parts[4].trim().equalsIgnoreCase(technicianUsername.trim())) {
                    jobs.add(parts);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jobs;
    }

    // Update status and technician feedback (customerComment at index 6 is preserved)
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

                String[] parts = trimmed.split("\\|");
                if (parts.length >= 8 && parts[0].trim().equalsIgnoreCase(appointmentId.trim())) {
                    // parts[6] = original customerComment (preserved)
                    // parts[7] = updated technicianFeedback
                    String updated = parts[0].trim() + "|" +
                                     parts[1].trim() + "|" +
                                     parts[2].trim() + "|" +
                                     parts[3].trim() + "|" +
                                     parts[4].trim() + "|" +
                                     newStatus.trim() + "|" +
                                     parts[6].trim() + "|" +
                                     newFeedback.trim();
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

    // Retrieve technician credentials from users.txt
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

                String[] parts = line.split("\\|");
                if (parts.length >= 4 && parts[0].trim().equalsIgnoreCase(username.trim())) {
                    return new String[]{parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim()};
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Update technician password and phone in users.txt
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

                String[] parts = trimmed.split("\\|");
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