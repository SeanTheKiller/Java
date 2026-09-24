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
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password.contains("|")){
            throw new IllegalArgumentException("Password contain '|'");
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
