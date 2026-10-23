package login;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class LoginManager {

    public static File getUsersFile() {
        File file = new File("Assignment/data/users.txt");
        if (file.exists()) {
            return file;
        }
        return new File("data/users.txt");
    }

    public static String authenticate(String enteredUsername, String enteredPassword) {
        File file = getUsersFile();

        if (!file.exists()) {
            System.err.println("users.txt not found!");
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Skip header: username|password|role|phoneNumber
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line.split("\\|", -1);
                if (parts.length >= 3) {
                    String fileUser = parts[0].trim();
                    String filePass = parts[1].trim();
                    String fileRole = parts[2].trim();

                    if (fileUser.equalsIgnoreCase(enteredUsername.trim()) && filePass.equals(enteredPassword.trim())) {
                        return fileRole;
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println("Error reading users.txt: " + ex.getMessage());
        }

        return null;
    }
}