

import umontreal.ssj.simevents.Event;
import umontreal.ssj.simevents.Sim;

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
    public ArrayList<Customer> served_customer = new ArrayList<>();
    private ArrayList<Customer> abandon_customer = new ArrayList<>();

    public ReplayOneDay() {
        nb_servers =0;
        for (int i=0; i<27; i++){
            array_LES[i]=0;
            array_queue_length[i]=0;
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
//            System.out.println("Type"+elements[1]);
            cust.setArrival_time(getTime(elements[0]));
            cust.setType(Integer.parseInt(String.valueOf(mapQueueName(Integer.parseInt(elements[1])))));
//            System.out.println("Ans"+elements[3]);
//            System.out.println("Hang"+elements[6]);
            cust.setWaiting_time(getWaitingTime(elements[0], elements[3], elements[6]));
            if (elements[3].equals("NULL") ) {
                cust.setIs_served(false);
            }
            if (!elements[3].equals("NULL")) {
                cust.setService_time(getTime(elements[6]) - getTime(elements[3]));
            }

            //On programme la rentree du cust dans la file
            new Arrival(cust).schedule(cust.getArrival_time());
            read_line = br.readLine();
        }

    }

    public double getTime(String s) {
        String[] parts = s.split(" ");
        if (parts.length >= 2) {
            String s1 = parts[1];
            String[] time = s1.split(":");
            if (time.length >= 3) {
                return Integer.parseInt(time[0]) * 3600 + Integer.parseInt(time[1]) * 60 + Integer.parseInt(time[2]) - 8 * 3600;
            }
        }
        // Si la séparation ne s'est pas faite correctement ou si les parties ne sont pas présentes, renvoyer une valeur par défaut comme -1
        return -1;
    }

    private int mapQueueName(int queueName) {
        // Ici, nous pouvons utiliser une structure de données, comme une HashMap, pour faire correspondre les valeurs "queue_name" à des entiers.
        // À titre d'exemple, je vais utiliser un simple switch pour montrer comment cela peut être fait.

        switch (queueName) {
            case 30175 :
                return 0;
            case 30560:
                return 1;
            case 30172:
                return 2;
            case 30181:
                return 3;
            case 30179:
                return 4;
            case 30066:
                return 5;
            case 30241:
                return 6;
            case 30511:
                return 7;
            case 30519:
                return 8;
            case  30173:
                return 9;
            case 30176:
                return 10;
            case 30180:
                return 11;
            case
                30174:
                return 12;
            case
                30325:
                return 13;
            case
                30236:
                return 14;
            case
                30363:
                return 15;
            case
                30334:
                return 16;
            default:

                return -1;
        }
    }
    public double getWaitingTime(String arrival, String answered, String hangup){
        if (!answered.equals("NULL")) {
            // Si l'appel a été répondu, retourner la différence entre l'heure de réponse et l'heure d'arrivée
            return getTime(answered) - getTime(arrival);
        } else if (!hangup.equals("NULL")) {
            // Si l'appel n'a pas été répondu mais a été raccroché (hangup), retourner la différence entre l'heure de raccrochage et l'heure d'arrivée
            return getTime(hangup) - getTime(arrival);
        } else {
            // Si l'appel n'a pas été répondu ni raccroché, il n'y a pas de temps d'attente valable, vous pouvez retourner une valeur par défaut comme -1
            return -1;
        }
    }

    public class Arrival extends Event {
        Customer cust;

        public Arrival(Customer cust){
            this.cust = cust;
        }

        public void actions() {

            // Initialiser le nombre de cust du meme type trouve dans la file
           int nbrecustumerSameType =   array_queue_length[cust.getType()] ++;

            // Initialisation des predicteurs(LES, AvgLES, AvgCLES, WAvgCLES)
            // LES
            if (nbrecustumerSameType ==0){
                cust.setLES(cust.getWaiting_time());
            }
            else {
                cust.setLES(Double.parseDouble(String.valueOf(array_LES[cust.getType()])));
            }

            // AvgLES
            LinkedList<Double> list = array_Avg_LES[cust.getType()];
            if (list.size() < 10) {
                list.add(cust.getWaiting_time());
            } else {
                list.remove(0);
                list.add(cust.getWaiting_time());
            }
            double avg_les = 0;
            for (Double waiting_time : list) {
                avg_les += waiting_time;
            }
            avg_les /= list.size();
            cust.setAvg_LES(avg_les);

//            cust.setAvgC_LES(3);
//            cust.setWAvgC_LES(5);

            // Initialiser nb_servers(le nombre de serveurs occupes)
            nb_servers++;
            // Placer le cust dans la file
            waitList.add(cust);
            // scheduler son depart(abandon ou servis) de la file dans wainting_time;
            if (cust.isIs_served()) {
                new Departure(cust).schedule(cust.getWaiting_time() + cust.getService_time());

            } else {
                new Departure(cust).schedule(cust.getWaiting_time());

            }
            // Incrementer le nombre de cust de ce type   (array_queue_length)
            array_queue_length[cust.getType()]++;

        }
    }

    public class Departure extends Event {
        Customer cust;

        public Departure(Customer cust){
            this.cust = cust;
        }

        public void actions() {
            // Mettre a jours les donnees utilisees par les predicteurs

            // array_LES
            array_LES[cust.getType()] = cust.getWaiting_time();
            // update array_avg_LES
            array_Avg_LES[cust.getType()].add(cust.getWaiting_time());

            // Decrementer le nb cust de ce type()  (array_queue_length)
            array_queue_length[cust.getType()]--;

            // Si depart == abandon alors mettre cust dans la liste des cust abandonnes  (abandon_customer)
            if (!cust.isIs_served()) {
                abandon_customer.add(cust);
            } else {
                served_customer.add(cust);
            }

            // Si depart == debutService alors incrementer nb_servers
            if (cust.isIs_served()) {
                nb_servers--;
            }

            // scheduler la fin de service du prochain customer
            if (!waitList.isEmpty()) {
                Customer next_cust = waitList.poll();
                new Departure(next_cust).schedule(next_cust.getWaiting_time() + next_cust.getService_time());
            }

        }

    }


    public class EndSimulation extends Event {
        private double duration;

        public EndSimulation(double duration) {
            this.duration = duration;
        }

        public void actions() {
            Sim.stop();
        }
    }



}
