package customer;

import common.User;

/**
 * A Customer IS-A User (inheritance). It reuses everything User already has
 * (username, password, phone number, validation) and fixes the role to "Customer".
 */
public class Customer extends User {

    public Customer(String username, String password, String phoneNumber,
            int age, String name, String email, String homeAddress) {
        // "super(...)" calls the User constructor; role is always "Customer"
        super(username, password, "Customer", phoneNumber, age, name, email, homeAddress);
    }

    // Method overriding: a customer's role is fixed and cannot be changed to anything else
    @Override
    public void setRole(String role) {
        if (!"Customer".equalsIgnoreCase(role)) {
            throw new IllegalArgumentException("A customer's role cannot be changed");
        }
        super.setRole("Customer");
    }

    // Method overriding: replaces Object's default toString()
    @Override
    public String toString() {
        return "Customer " + getName() + " (" + getUsername() + ", " + getPhoneNumber() + ")";
    }
}
