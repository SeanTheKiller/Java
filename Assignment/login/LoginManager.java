package login;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class LoginManager {

    // helper to find users.txt so program dont crash if run from diff folder
    private static File getUsersFile() {
        File file = new File("Assignment/data/users.txt");
        if (file.exists()) {
            return file;
        }
        return new File("data/users.txt");
    }

    // take username & password, check file, return the role if pass, return null if fail
    public static String authenticate(String enteredUsername, String enteredPassword) {
        File file = getUsersFile();

        // if file missing then directly reject 
        if (!file.exists()) {
            System.out.println("users.txt missing!");
            return null;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue; // skip blank rows

                // skip the header (username|password|role...)
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                // split by pipe |
                String[] parts = line.split("\\|");
                if (parts.length >= 3) {
                    String fileUser = parts[0].trim();
                    String filePass = parts[1].trim();
                    String fileRole = parts[2].trim();

                    // if both match then return role immediately
                    if (fileUser.equals(enteredUsername) && filePass.equals(enteredPassword)) {
                        return fileRole; 
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Error reading file: " + ex.getMessage());
        }

        // if loop finish and no match, return null
        return null;
    }
}