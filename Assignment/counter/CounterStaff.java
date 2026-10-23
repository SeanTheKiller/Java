package counter;

import common.User;

public class CounterStaff extends User{
    
    public CounterStaff(String username, String password, String phonenumber,
            int age, String name, String email, String homeAddress) {
        super(username, password, "Counter" , phonenumber, age, name, email, homeAddress);
    }
    
   
}
