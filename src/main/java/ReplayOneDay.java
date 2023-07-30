

import jdk.internal.event.Event;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class ReplayOneDay {
    LinkedList<Customer> waitList = new LinkedList<Customer> ();

    //Contains the length of queues
    private int[] array_queue_length =  new int[27];
    private double[] array_LES = new double[27];
    private LinkedList[] array_Avg_LES = new LinkedList[27];
    private LinkedList[][] array_AvgC_LES = new LinkedList[27][50];
    private double[][] array_WAvgC_LES = new double[27][50];
    private int nb_servers;
//    private ArrayList<Customer> served_customer = new ArrayList<>();
//    private ArrayList<Customer> abandon_customer = new ArrayList<>();

    public ReplayOneDay() {
        for (int i=0; i<27; i++){
            array_Avg_LES[i] = new LinkedList<Double>();
            for (int j=0; j<50; j++){
                array_AvgC_LES[i][j] = new LinkedList<Double>();
            }
        }
    }

    public void createCustomerOfTheDay(String file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        br.readLine();
        String read_line = br.readLine();
        while (read_line != null){
            Customer cust = new Customer();
            String[] elements =  read_line.split(",");
            cust.setArrival_time(getTime(elements[0]));
            cust.setType(Integer.parseInt(elements[7]));
            cust.setWaiting_time(getWaitingTime(elements[0], elements[3], elements[6]));
            if (elements[3] == "NULL") {
                cust.setIs_served(false);
            }
            if (elements[3] != "NULL") {
                cust.setService_time(getTime(elements[6]) - getTime(elements[3]));
            }

            //On programme la rentree du cust dans la file
//            new Arrival(cust).schedule(cust.getArrival_time());
            read_line = br.readLine();
        }
    }

    public double getTime(String s) {
        String s1 = s.split(" ")[1];
        String[] time = s1.split(":");
        return Integer.parseInt(time[0])*3600 + Integer.parseInt(time[1])*60 + Integer.parseInt(time[2]) - 8*3600;
    }

    public double getWaitingTime(String arrival, String answered, String hangup){
        if (answered != "NULL") {
            return getTime(answered) - getTime(arrival);
        } else {
            return getTime(hangup) - getTime(arrival);
        }
    }

    public class Arrival extends Event {
        Customer cust;

        public Arrival(Customer cust){
            this.cust = cust;
        }

        public void actions() {

            // Initialiser le nombre de cust du meme type trouve dans la file
            //      Initialisation des predicteurs(LES, AvgLES, AvgCLES, WAvgCLES)
            //            Initialiser nb_servers(le nombre de serveurs occupes)
            //            Placer le cust dans la file
            //            scheduler son depart(abandon ou servis) de la file dans wainting_time;
            //            Incrementer le nombre de cust de ce type   (array_queue_length)
        }
    }

    public class Departure extends Event {
        Customer cust;

        public Departure(Customer cust){
            this.cust = cust;
        }

        public void actions() {
            //         Mettre a jours les donnees utilisees par les predicteurs
            //         Decrementer le nb cust de ce type()  (array_queue_length)
            //         Si depart == abandon alors mettre cust dans la liste des cust abandonnes  (abandon_customer)
            //         Si depart == debutService alors incrementer nb_servers
            //              schudeler la fin de service

        }


    }
}
