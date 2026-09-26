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

    public User(String username, String password, String role, String phonenumber) {
        if (username.contains("|")||password.contains("|")||phonenumber.contains("|")||role.contains("|")){
            throw new IllegalArgumentException("Input contain '|'");
        }
        if (username.equals("")||password.equals("")||phonenumber.equals("")||role.equals("")){
            throw new IllegalArgumentException("Input is empty");
        }
        this.username = username;
        this.password = password;
        this.role = role;
        this.phoneNumber = phonenumber;
        FileUtil.allUsers.add(this);
    }

    public User(String username, String password, String role) {
        if (username.contains("|")||password.contains("|")){
            throw new IllegalArgumentException("Input contain '|'");
        }
        if (username.equals("")||password.equals("")||role.equals("")){
            throw new IllegalArgumentException("Input is empty");
        }
        this.username = username;
        this.password = password;
        this.role = role;
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
    
    public static void main(String[] args){
        //testing purposes
        // this is how u call util func
        FileUtil.loadAllUsers();
        FileUtil.printAllUsers();
    }
    
}
