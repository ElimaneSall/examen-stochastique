import java.util.Arrays;

public class Customer {

    // model deep learning:
    //    type of service
    private int type;
    //    contains the length of the files
    private int[] length_file = new int[27];

    //    arrival time of the customer
    private double arrival_time;
    //    number of servers
    private int nb_server;
    //    the Last to Enter Service
    private double LES;
    private double Avg_LES;
    private double AvgC_LES;
    private double WAvgC_LES;
    //real waiting time
    private double waiting_time;
    private boolean is_served = true;
    private double service_time;

    @Override
    public String toString() {
        return "Customer{" +
                "type=" + type +
                ", length_file=" + Arrays.toString(length_file) +
                ", arrival_time=" + arrival_time +
                ", nb_server=" + nb_server +
                ", LES=" + LES +
                ", Avg_LES=" + Avg_LES +
                ", AvgC_LES=" + AvgC_LES +
                ", WAvgC_LES=" + WAvgC_LES +
                ", waiting_time=" + waiting_time +
                ", is_served=" + is_served +
                ", service_time=" + service_time +
                '}';
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int[] getLength_file() {
        return length_file;
    }

    public void setLength_file(int[] length_file) {
        this.length_file = length_file;
    }

    public double getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(double arrival_time) {
        this.arrival_time = arrival_time;
    }

    public int getNb_server() {
        return nb_server;
    }

    public void setNb_server(int nb_server) {
        this.nb_server = nb_server;
    }

    public double getLES() {
        return LES;
    }

    public void setLES(double LES) {
        this.LES = LES;
    }

    public double getAvg_LES() {
        return Avg_LES;
    }

    public void setAvg_LES(double avg_LES) {
        Avg_LES = avg_LES;
    }

    public double getAvgC_LES() {
        return AvgC_LES;
    }

    public void setAvgC_LES(double avgC_LES) {
        AvgC_LES = avgC_LES;
    }

    public double getWAvgC_LES() {
        return WAvgC_LES;
    }

    public void setWAvgC_LES(double WAvgC_LES) {
        this.WAvgC_LES = WAvgC_LES;
    }

    public double getWaiting_time() {
        return waiting_time;
    }

    public void setWaiting_time(double waiting_time) {
        this.waiting_time = waiting_time;
    }

    public boolean isIs_served() {
        return is_served;
    }

    public void setIs_served(boolean is_served) {
        this.is_served = is_served;
    }

    public double getService_time() {
        return service_time;
    }

    public void setService_time(double service_time) {
        this.service_time = service_time;
    }
}
