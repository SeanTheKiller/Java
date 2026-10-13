package techniciancustomer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TechnicianFunctions {

    public static File getAppointmentsFile() {
        File file = new File("Assignment/data/appointments.txt");
        if (!file.exists()) {
            file = new File("data/appointments.txt");
        }
        return file;
    }

    // Returns all appointments assigned to the logged-in technician
    public static List<String[]> getJobsForTechnician(String technicianUsername) {
        List<String[]> jobs = new ArrayList<>();
        File file = getAppointmentsFile();

        if (!file.exists()) return jobs;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (firstLine) {
                    firstLine = false; // skip header
                    continue;
                }

                String[] parts = line.split("\\|");
                // Check if this appointment belongs to this technician (index 4)
                if (parts.length >= 7 && parts[4].trim().equalsIgnoreCase(technicianUsername)) {
                    jobs.add(parts);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading appointments: " + e.getMessage());
        }
        return jobs;
    }

    // Updates status and notes for a specific appointment ID
    public static boolean updateJob(String appointmentId, String newStatus, String newNotes) {
        File file = getAppointmentsFile();
        List<String> allLines = new ArrayList<>();
        boolean updated = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split("\\|");
                if (parts.length >= 7 && parts[0].trim().equals(appointmentId)) {
                    String updatedLine = parts[0].trim() + "|" +
                            parts[1].trim() + "|" +
                            parts[2].trim() + "|" +
                            parts[3].trim() + "|" +
                            parts[4].trim() + "|" +
                            newStatus + "|" +
                            newNotes;
                    allLines.add(updatedLine);
                    updated = true;
                } else {
                    allLines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (updated) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                for (String l : allLines) {
                    pw.println(l);
                }
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}