package my.edu.tarc.mobilecashservice.Entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Loi Kah Hou on 12/30/2017.
 */

public class Withdrawal implements Serializable{


    int withdrawal_id;
    int user_id;
    double amount;
    int deposit_id;
    int location_id;
    String dateTime;
    String status;

    public Withdrawal() {
        //this(0,0,0.0,0,0,null);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        dateTime = dateFormat.format(new Date());
    }
/*
    public Withdrawal(int withdrawal_id, int user_id, double amount, int deposit_id, String status) {
        this.deposit_id = deposit_id;
        this.user_id = user_id;
        this.amount = amount;
        this.withdrawal_id = withdrawal_id;
        this.status = status;
    } */

    public Withdrawal(int withdrawal_id, int user_id, double amount, int deposit_id, int location_id, String dateTime, String status) {
        this.withdrawal_id = withdrawal_id;
        this.user_id = user_id;
        this.amount = amount;
        this.deposit_id = deposit_id;
        this.location_id = location_id;
        this.dateTime = dateTime;
        this.status = status;
    }

    public int getWithdrawal_id() {
        return withdrawal_id;
    }

    public void setWithdrawal_id(int withdrawal_id) {
        this.withdrawal_id = withdrawal_id;
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

    public int getDeposit_id() {
        return deposit_id;
    }

    public void setDeposit_id(int deposit_id) {
        this.deposit_id = deposit_id;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("withdrawal_id", withdrawal_id);
        result.put("user_id", user_id);
        result.put("amount", amount);
        result.put("deposit_id", deposit_id);
        result.put("location_id", location_id);
        result.put("dateTime", dateTime);
        result.put("status", status);
        return result;
    }
}
