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
    public static ArrayList<Appointment> allAppointments = new ArrayList<Appointment>();
    public static ArrayList<ServicePrice> allPrices = new ArrayList<ServicePrice>();
    public static ArrayList<Payment> allPayment = new ArrayList<Payment>();
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

    public static ArrayList<Appointment> loadAllAppointments() {
        allAppointments.clear();
        List<String> lines = read("appointments.txt");
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split("\\|", -1);
            if (parts.length >= 9) {
                allAppointments.add(new Appointment(parts[0], parts[1], parts[2], parts[3],
                        parts[4], parts[5], parts[6], parts[7], parts[8]));
            }
        }
        return allAppointments;
    }
    
    public static Appointment findAppointmentByID(String AppointmentID){
        for (Appointment apt: allAppointments){
            if (apt.getAppointmentId().equals(AppointmentID)){
                return apt;
            }
        }
        return null;
    }

    public static void saveAllAppointments() {
        List<String> lines = new ArrayList();
        lines.add("appointmentId|customerName|carPlate|serviceType|datetime|technician|status|customerComment|technicianFeedback");
        for (Appointment a : allAppointments) {
            lines.add(a.toFileString());
        }
        write("appointments.txt", lines);
    }
    
    public static void loadAllPayments() {
        allPayment.clear();
        List<String> lines = read("payments.txt");
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split("\\|");
            if (parts.length >= 3) {
                try {
                    allPayment.add(new Payment(parts[0], parts[1], parts[2]));
                } catch (IllegalArgumentException ex) {
                    System.out.println("Skipping invalid payment record: " + lines.get(i));
                }
            }
        }
    }
    
    public static Payment findPaymentByID(String AppointmentID){
        for (Payment p: allPayment){
            if (p.getAppointmentID().equals(AppointmentID)){
                return p;
            }
        }
        return null;
    }
    
    public static void saveAllPayments() {
        List<String> lines = new ArrayList<>();
        lines.add("appointmentId|billedAmount|paidAmount");
        for (Payment p : allPayment) {
            lines.add(p.getAppointmentID() + "|" + p.getBilledAmount() + "|" + p.getPaidAmount());
        }
        write("payments.txt", lines);
    }

    public static void loadPrices() {
        allPrices.clear();
        List<String> lines = read("price.txt");
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split("\\|");
            if (parts.length >= 2) {
                try {
                    allPrices.add(new ServicePrice(parts[0], Double.parseDouble(parts[1])));
                } catch (IllegalArgumentException ex) {
                    System.out.println("Skipping invalid price record: " + lines.get(i));
                }
            }
        }
    }

    public static void savePrices() {
        List<String> lines = new ArrayList<>();
        lines.add("serviceType|price");
        for (ServicePrice servicePrice : allPrices) {
            lines.add(servicePrice.getServiceType() + "|" + servicePrice.getPrice());
        }
        write("price.txt", lines);
    }

    public static ServicePrice findPriceByServiceType(String serviceType) {
        for (ServicePrice servicePrice : allPrices) {
            if (servicePrice.getServiceType().equalsIgnoreCase(serviceType)) {
                return servicePrice;
            }
        }
        return null;
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
    public static void printAllPayment() {
        for (Payment p : allPayment){
            System.out.println("Payment info are: "+p.getAppointmentID()+" "+p.getBilledAmount()+" "+p.getPaidAmount());
        }
    }
    
    public static void main(String[] args){
        // testing purposes
//        loadAllUsers();
//        printAllUsers();
//        saveAllUsers();
//        User a = findUserByUsername("lim ");
//        if (a != null){
//            System.out.println("Found user: "+a.username + " "+a.role);
//        } else {
//            System.out.println("cannot find user");
//        }
          loadAllPayments();
          printAllPayment();
          saveAllPayments();
    }
    
}
