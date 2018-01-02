package my.edu.tarc.mobilecashservice.Entity;

/**
 * Created by jiaweiloo on 29/12/2017.
 */

public class Deposit {
    int deposit_id;
    int user_id;
    double amount;
    int withdrawal_id;
    int location_id;
    String status;

    public Deposit() {
        //this(0,0,0.0,0,0,null);
    }

    public Deposit(int deposit_id, int user_id, double amount, int withdrawal_id, int location_id, String status) {
        this.deposit_id = deposit_id;
        this.user_id = user_id;
        this.amount = amount;
        this.withdrawal_id = withdrawal_id;
        this.location_id = location_id;
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

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
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
                ", location_id=" + location_id +
                ", status='" + status + '\'' +
                '}';
    }
}
