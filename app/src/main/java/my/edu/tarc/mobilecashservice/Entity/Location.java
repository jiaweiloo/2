package my.edu.tarc.mobilecashservice.Entity;

/**
 * Created by jiaweiloo on 4/1/2018.
 */

public class Location {
    int location_id;
    String location_name;
    double location_x;
    double location_y;
    String status;

    public Location() {
    }

    public Location(int location_id, String location_name, double location_x, double location_y, String status) {
        this.location_id = location_id;
        this.location_name = location_name;
        this.location_x = location_x;
        this.location_y = location_y;
        this.status = status;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
