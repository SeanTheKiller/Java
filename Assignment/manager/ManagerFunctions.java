package manager;

import common.FileUtil;
import common.ServicePrice;
import common.User;

public class ManagerFunctions {

    public static void createUser(String username, String password, String role, String phoneNumber) {
        username = username.trim();
        password = password.trim();
        role = role.trim();
        phoneNumber = phoneNumber.trim();

        if (username.isEmpty() || password.isEmpty() || role.isEmpty() || phoneNumber.isEmpty()) {
            throw new IllegalArgumentException("All fields are required.");
        }
        if (username.contains("|") || password.contains("|")
                || role.contains("|") || phoneNumber.contains("|")) {
            throw new IllegalArgumentException("Fields cannot contain '|'.");
        }
        if (phoneNumber.length() != 10 || !phoneNumber.matches("\\d+")) {
            throw new IllegalArgumentException("Phone number must contain 10 digits.");
        }

        FileUtil.loadAllUsers();
        if (FileUtil.findUserByUsername(username) != null) {
            throw new IllegalArgumentException("Username already taken.");
        }

        new User(username, password, role, phoneNumber);
        FileUtil.saveAllUsers();
    }

    public static void updateUser(String originalUsername, String username, String password,
            String role, String phoneNumber) {
        username = username.trim();
        password = password.trim();
        role = role.trim();
        phoneNumber = phoneNumber.trim();

        validateUserInput(username, password, role, phoneNumber);
        FileUtil.loadAllUsers();

        User user = FileUtil.findUserByUsername(originalUsername);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        User existingUser = FileUtil.findUserByUsername(username);
        if (existingUser != null && existingUser != user) {
            throw new IllegalArgumentException("Username already taken.");
        }

        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        user.setPhoneNumber(phoneNumber);
        FileUtil.saveAllUsers();
    }

    public static void deleteUser(String username) {
        FileUtil.loadAllUsers();
        User user = FileUtil.findUserByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        FileUtil.allUsers.remove(user);
        FileUtil.saveAllUsers();
    }

    private static void validateUserInput(String username, String password, String role, String phoneNumber) {
        if (username.isEmpty() || password.isEmpty() || role.isEmpty() || phoneNumber.isEmpty()) {
            throw new IllegalArgumentException("All fields are required.");
        }
        if (username.contains("|") || password.contains("|")
                || role.contains("|") || phoneNumber.contains("|")) {
            throw new IllegalArgumentException("Fields cannot contain '|'.");
        }
        if (phoneNumber.length() != 10 || !phoneNumber.matches("\\d+")) {
            throw new IllegalArgumentException("Phone number must contain 10 digits.");
        }
    }

    public static void updateServicePrice(String serviceType, double price) {
        if (serviceType == null || serviceType.trim().isEmpty()) {
            throw new IllegalArgumentException("Service type is required.");
        }
        if (Double.isNaN(price) || Double.isInfinite(price) || price < 0) {
            throw new IllegalArgumentException("Price must be a valid non-negative number.");
        }

        FileUtil.loadPrices();
        ServicePrice servicePrice = FileUtil.findPriceByServiceType(serviceType.trim());
        if (servicePrice == null) {
            throw new IllegalArgumentException("Service type not found.");
        }
        servicePrice.setPrice(price);
        FileUtil.savePrices();
    }

}
