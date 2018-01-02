package my.edu.tarc.mobilecashservice.Entity;

/**
 * Created by Nan Fung Lim on 31/12/2017.
 */

public class UserRecord {
    int user_id, phone;
    String user_name, password, ic_number, email;

    public UserRecord() {
    }

    public UserRecord(int user_id, int phone, String user_name, String password, String ic_number, String email) {
        this.user_id = user_id;
        this.phone = phone;
        this.user_name = user_name;
        this.password = password;
        this.ic_number = ic_number;
        this.email = email;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setIc_number(String ic_number) {
        this.ic_number = ic_number;
    }

    public String getIc_number() {
        return ic_number;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public int getPhone() {
        return phone;
    }
}
