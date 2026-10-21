package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import common.User;

public class FileUtil {
    public static ArrayList<User> allUsers = new ArrayList<User>();
    public static User currentUser;
    
    private static final String DATA_DIR = "data";
    // read file from path
    // return the lines
    public static List<String> read(String fileName) {
        List<String> lines = new ArrayList<>();
        String projectRoot = System.getProperty("user.dir");
        Path path = Paths.get(projectRoot, DATA_DIR, fileName);
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Filepath cannot find/read for "+fileName);
            e.printStackTrace();
        }
        return lines;
    }
    
    public static void write(String fileName, List<String> lines){
        String projectRoot = System.getProperty("user.dir");
        Path path = Paths.get(projectRoot, DATA_DIR, fileName);
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // read users.txt and load it into allUsers
    public static void loadAllUsers() {
        allUsers.clear();
        List<String> lines = read("users.txt");
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split("\\|");
            if (parts.length >= 4) {
                new User(parts[0], parts[1], parts[2], parts[3]);
            }
        }
    }
    
    public static void saveAllUsers() {
        List<String> lines = new ArrayList();
        lines.add("username|password|role|phoneNumber");
        for (User u: allUsers){
            String line = u.username+"|"+u.password+"|"+u.role+"|"+u.phoneNumber;
            lines.add(line);
        }
        write("users.txt", lines);
    }
    
    // fetch User class by username, return null if cant
    public static User findUserByUsername(String targetUsername){
        for (User u: allUsers){
            if (u.username.equals(targetUsername)){
                return u;
            }
        }
        return null;
    }
    
    public static void printAllUsers() {
        for (User u : allUsers){
            System.out.println("User info are: "+u.username+" "+u.password+" "+u.role+" "+u.phoneNumber);
        }
    }
    
    public static void main(String[] args){
        // testing purposes
        loadAllUsers();
        printAllUsers();
        saveAllUsers();
        User a = findUserByUsername("lim ");
        if (a != null){
            System.out.println("Found user: "+a.username + " "+a.role);
        } else {
            System.out.println("cannot find user");
        }
    }
    
}
