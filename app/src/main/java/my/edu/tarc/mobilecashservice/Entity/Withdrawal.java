package my.edu.tarc.mobilecashservice.Entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Loi Kah Hou on 12/30/2017.
 */

public class Withdrawal implements Serializable{
    int deposit_id;
    int user_id;
    double amount;
    int withdrawal_id;
    double location_x;
    double location_y;
    String dateTime;
    String status;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getLocation_x() {
        return location_x;
    }

    public void setLocation_x(double location_x) {
        this.location_x = location_x;
    }

    public double getLocation_y() {
        return location_y;
    }

    public void setLocation_y(double location_y) {
        this.location_y = location_y;
    }

    public Withdrawal() {
        //this(0,0,0.0,0,0,null);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        dateTime = dateFormat.format(new Date());
    }

    public Withdrawal(int withdrawal_id, int user_id, double amount, int deposit_id, String status) {
        this.deposit_id = deposit_id;
        this.user_id = user_id;
        this.amount = amount;
        this.withdrawal_id = withdrawal_id;
        this.status = status;
    }

    public int getDeposit_id() {
        return deposit_id;
    }

    public void setDeposit_id(int deposit_id) {
        this.deposit_id = deposit_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getWithdrawal_id() {
        return withdrawal_id;
    }

    public void setWithdrawal_id(int withdrawal_id) {
        this.withdrawal_id = withdrawal_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Deposit{" +
                "deposit_id=" + deposit_id +
                ", user_id=" + user_id +
                ", amount=" + amount +
                ", withdrawal_id=" + withdrawal_id +
                ", status='" + status + '\'' +
                '}';
    }
}
