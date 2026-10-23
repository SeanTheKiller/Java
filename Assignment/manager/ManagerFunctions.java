package manager;

import common.FileUtil;
import common.ServicePrice;
import common.User;

public class ManagerFunctions {

    public static void createUser(String username, String password, String role, String phoneNumber,
            String ageText, String name, String email, String homeAddress) {
        username = username.trim();
        password = password.trim();
        role = role.trim();
        int age = validateUserInput(username, password, role, phoneNumber,
                ageText, name, email, homeAddress);

        FileUtil.loadAllUsers();
        if (FileUtil.findUserByUsername(username) != null) {
            throw new IllegalArgumentException("Username already taken.");
        }

        new User(username, password, role, phoneNumber, age, name, email, homeAddress);
        FileUtil.saveAllUsers();
    }

    public static void updateUser(String originalUsername, String username, String password,
            String role, String phoneNumber,
            String ageText, String name, String email, String homeAddress) {
        username = username.trim();
        password = password.trim();
        int age = validateUserInput(username, password, role.trim(), phoneNumber.trim(),
                ageText, name.trim(), email.trim(), homeAddress.trim());
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
        user.setAge(age);
        user.setName(name);
        user.setEmail(email);
        user.setHomeAddress(homeAddress);
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

    // validate input and return the parsed age
    private static int validateUserInput(String username, String password, String role, String phoneNumber,
            String ageText, String name, String email, String homeAddress) {
        if (username.isEmpty() || password.isEmpty() || role.isEmpty() || phoneNumber.isEmpty()
                || ageText.isEmpty() || name.isEmpty() || email.isEmpty() || homeAddress.isEmpty()) {
            throw new IllegalArgumentException("All fields are required.");
        }
        if (username.contains("|") || password.contains("|")
                || role.contains("|") || phoneNumber.contains("|")
                || name.contains("|") || email.contains("|") || homeAddress.contains("|")) {
            throw new IllegalArgumentException("Fields cannot contain '|'.");
        }
        if (phoneNumber.length() != 10 || !phoneNumber.matches("\\d+")) {
            throw new IllegalArgumentException("Phone number must contain 10 digits.");
        }
        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Age must be a number.");
        }
        return age;
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
