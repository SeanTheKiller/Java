package techniciancustomer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TechnicianFunctions {

    public static File getAppointmentsFile() {
        String userDir = System.getProperty("user.dir");
        
        // Priority 1: Check Assignment/data/appointments.txt
        File f1 = new File(userDir, "Assignment/data/appointments.txt");
        if (f1.exists()) {
            return f1;
        }

        // Priority 2: Check data/appointments.txt
        File f2 = new File(userDir, "data/appointments.txt");
        if (f2.exists()) {
            return f2;
        }

        // Fallback default
        return f1;
    }

    public static List<String[]> getJobsForTechnician(String technicianUsername) {
        List<String[]> jobs = new ArrayList<>();
        File file = getAppointmentsFile();

        System.out.println("[DEBUG READ] Reading from: " + file.getAbsolutePath());

        if (!file.exists()) {
            System.out.println("[DEBUG READ] File does not exist!");
            return jobs;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }

                String[] parts = line.split("\\|");
                if (parts.length >= 7) {
                    String techInRow = parts[4].trim();
                    if (techInRow.equalsIgnoreCase(technicianUsername.trim())) {
                        jobs.add(parts);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("[DEBUG READ] Found " + jobs.size() + " jobs for " + technicianUsername);
        return jobs;
    }

    public static boolean updateJob(String appointmentId, String newStatus, String newNotes) {
        File file = getAppointmentsFile();
        System.out.println("[DEBUG WRITE] Updating file: " + file.getAbsolutePath());

        if (!file.exists()) {
            System.out.println("[DEBUG WRITE] Target file not found!");
            return false;
        }

        List<String> updatedLines = new ArrayList<>();
        boolean targetFound = false;

        // 1. Read every line into memory
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) continue;

                String[] parts = trimmed.split("\\|");
                if (parts.length >= 7 && parts[0].trim().equalsIgnoreCase(appointmentId.trim())) {
                    // Update this specific appointment
                    String rebuilt = parts[0].trim() + "|" +
                                     parts[1].trim() + "|" +
                                     parts[2].trim() + "|" +
                                     parts[3].trim() + "|" +
                                     parts[4].trim() + "|" +
                                     newStatus.trim() + "|" +
                                     newNotes.trim();
                    updatedLines.add(rebuilt);
                    targetFound = true;
                } else {
                    // Keep existing line untouched (header or other rows)
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // SAFETY: If the file was empty or target ID wasn't matched, abort immediately
        if (!targetFound || updatedLines.isEmpty()) {
            System.out.println("[DEBUG WRITE] Aborting write! targetFound=" + targetFound + ", lineCount=" + updatedLines.size()); //Debuggin purposess dont delete
            return false;
        }

        // 2. Write back to file safely
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, false)))) {
            for (String l : updatedLines) {
                pw.println(l);
            }
            System.out.println("[DEBUG WRITE] Successfully wrote " + updatedLines.size() + " lines back to file."); //Debuggin purposess dont delete
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}