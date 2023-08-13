import umontreal.ssj.simevents.Event;
import umontreal.ssj.simevents.Sim;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Simulation {
    private double simulationDuration;
    private String inputFile;

    public Simulation(double simulationDuration, String inputFile) {
        this.simulationDuration = simulationDuration;
        this.inputFile = inputFile;
    }

    public class EndSimulation extends Event {
        private double duration;
        public EndSimulation(double duration) {
            this.duration = duration;
        }
        public void actions() {
            System.out.println("Simulation ended at time: " + Sim.time());
            Sim.stop();
        }
    }

    public void runSimulation() {
        try {
            ReplayOneDay replay = new ReplayOneDay();
            replay.createCustomerOfTheDay(inputFile);
            System.out.println("Starting simulation...");
//            new EndSimulation(simulationDuration).schedule(simulationDuration);
            Sim.start();
            System.out.println("Simulation completed.");
//            for (Customer customer : replay.served_customer) {
//                System.out.println("Customer: " + customer.toString());
//            }
//            System.out.println(replay.served_customer.size());
            // Save customers to CSV file
            saveCustomersToCSV(replay.served_customer, "customers2.csv");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void saveCustomersToCSV(List<Customer> customers, String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);

        // Write the header row
        writer.write("Type,Arrival_Time,LES,Avg_LES,AvgC_LES,WAvgC_LES,Waiting_Time,Is_Served,Service_Time\n");
//        writer.write("Type,Length_File_0,Length_File_1,...,Arrival_Time,Num_Servers,LES,Avg_LES,AvgC_LES,WAvgC_LES,Waiting_Time,Is_Served,Service_Time\n");

        // Write each customer's data
        for (Customer customer : customers) {
            StringBuilder sb = new StringBuilder();

            // Append all fields separated by commas
            sb.append(customer.getType()).append(",");
//            for (int i = 0; i < customer.getLength_file().length; i++) {
//                sb.append(customer.getLength_file()[i]).append(",");
//            }
            sb.append(customer.getArrival_time()).append(",");
//            sb.append(customer.getNb_server()).append(",");
            sb.append(customer.getLES()).append(",");
            sb.append(customer.getAvg_LES()).append(",");
            sb.append(customer.getAvgC_LES()).append(",");
            sb.append(customer.getWAvgC_LES()).append(",");
            sb.append(customer.getWaiting_time()).append(",");
            sb.append(customer.isIs_served()).append(",");
            sb.append(customer.getService_time()).append("\n");

            writer.write(sb.toString());
        }
        writer.close();
    }

    public static void main(String[] args) {
        // Set the simulation duration and input file path
        double simulationDuration = 6000; // Assuming a 24-hour simulation
        String inputFile = "C:\\Users\\Surface\\Desktop\\Cours modelisation Stochastique\\examen\\VANAD_data\\calls-2014-01.csv";
        Simulation simulation = new Simulation(simulationDuration, inputFile);
        simulation.runSimulation();


    }
}
