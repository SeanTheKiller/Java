/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package common;

/**
 *
 * @author johnc
 */
public class User {
    String username;
    String password;
    String role;
    String phoneNumber;
    int age;
    String name;
    String email;
    String homeAddress;

    public User(String username, String password, String role, String phoneNumber,
            int age, String name, String email, String homeAddress) {
        this.username = username;
        this.password = password;
        this.role = role;
        setUsername(username);
        setPassword(password);
        setRole(role);
        setPhoneNumber(phoneNumber);
        setAge(age);
        setName(name);
        setEmail(email);
        setHomeAddress(homeAddress);
        FileUtil.allUsers.add(this);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username.contains("|")){
            throw new IllegalArgumentException("Username contain '|'");
        }
        if (username.equals("")){
            throw new IllegalArgumentException("Input is empty");
        }
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password.contains("|")){
            throw new IllegalArgumentException("Password contain '|'");
        }
        if (password.equals("")){
            throw new IllegalArgumentException("Input is empty");
        }
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber.contains("|")){
            throw new IllegalArgumentException("Phone Number contain '|'");
        }
        if (phoneNumber.equals("")){
            throw new IllegalArgumentException("Input is empty");
        }
        // reject 60123456789, 012-456 6789 and othyer format
        if (phoneNumber.length() != 10 || !phoneNumber.matches("\\d+")){
            throw new IllegalArgumentException("provide phone number in the format of '0123016789'");
        }
        
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if (role.contains("|")){
            throw new IllegalArgumentException("Role contain '|'");
        }
        this.role = role;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age < 1 || age > 120){
            throw new IllegalArgumentException("Age must be a number between 1 and 120");
        }
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.contains("|")){
            throw new IllegalArgumentException("Name contain '|'");
        }
        if (name.equals("")){
            throw new IllegalArgumentException("Input is empty");
        }
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email.contains("|")){
            throw new IllegalArgumentException("Email contain '|'");
        }
        if (email.equals("")){
            throw new IllegalArgumentException("Input is empty");
        }
        this.email = email;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        if (homeAddress.contains("|")){
            throw new IllegalArgumentException("Home Address contain '|'");
        }
        if (homeAddress.equals("")){
            throw new IllegalArgumentException("Input is empty");
        }
        // a record must stay on one line
        this.homeAddress = homeAddress.replace("\n", " ");
    }
    
    public static void main(String[] args){
        //testing purposes
        // this is how u call util func
        FileUtil.loadAllUsers();
        FileUtil.printAllUsers();
    }
    
}
