package techniciancustomer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class TechnicianFunctions implements JobOperations {

    public static File getDataFile(String fileName) {
        File file = new File("Assignment/data/" + fileName);
        if (file.exists()) {
            return file;
        }
        return new File("data/" + fileName);
    }

    @Override
    public List<String[]> getAssignedJobs(String technicianUsername) {
        return getJobsForTechnician(technicianUsername);
    }

    @Override
    public boolean updateJobRecord(String appointmentId, String newStatus, String newFeedback) {
        return updateJob(appointmentId, newStatus, newFeedback);
    }

    // Read all appointments for technician (9 columns)
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

    // Update appointment status and feedback
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

    // Read full user profile (returns guaranteed 8 elements)
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
                if (parts.length >= 3 && parts[0].trim().equalsIgnoreCase(username.trim())) {
                    String[] full = new String[8];
                    for (int i = 0; i < 8; i++) {
                        full[i] = (i < parts.length) ? parts[i].trim() : "";
                    }
                    return full;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Check if new username is already taken by someone else
    public static boolean isUsernameTaken(String newUsername, String currentUsername) {
        if (newUsername.equalsIgnoreCase(currentUsername)) {
            return false;
        }

        File file = getDataFile("users.txt");
        if (!file.exists()) return false;

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
                if (parts.length >= 1 && parts[0].trim().equalsIgnoreCase(newUsername.trim())) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Update all 8 fields in users.txt (role is preserved) and migrate appointments if username changed
    public static boolean updateFullProfile(String oldUsername, String newUsername, String newPassword, 
                                            String newPhone, String newAge, String newName, 
                                            String newEmail, String newAddress) {
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
                if (parts.length >= 3 && parts[0].trim().equalsIgnoreCase(oldUsername.trim())) {
                    String preservedRole = parts[2].trim(); // Role is NEVER changed

                    String updatedRow = newUsername.trim() + "|" +
                                       newPassword.trim() + "|" +
                                       preservedRole + "|" +
                                       newPhone.trim() + "|" +
                                       newAge.trim() + "|" +
                                       newName.trim() + "|" +
                                       newEmail.trim() + "|" +
                                       newAddress.trim();

                    allLines.add(updatedRow);
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
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        // If username was changed, migrate assigned appointments to new username
        if (!oldUsername.equalsIgnoreCase(newUsername)) {
            migrateAppointmentsUsername(oldUsername, newUsername);
        }

        return true;
    }

    // Helper to rename technician in appointments.txt if username is changed
    private static void migrateAppointmentsUsername(String oldUser, String newUser) {
        File file = getDataFile("appointments.txt");
        if (!file.exists()) return;

        List<String> lines = new ArrayList<>();
        boolean modified = false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) continue;

                String[] parts = trimmed.split("\\|", -1);
                if (parts.length >= 6 && parts[5].trim().equalsIgnoreCase(oldUser.trim())) {
                    parts[5] = newUser.trim();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < parts.length; i++) {
                        sb.append(parts[i]);
                        if (i < parts.length - 1) sb.append("|");
                    }
                    lines.add(sb.toString());
                    modified = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (Exception ignored) {}

        if (modified) {
            try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, false)))) {
                for (String l : lines) {
                    pw.println(l);
                }
            } catch (Exception ignored) {}
        }
    }
}