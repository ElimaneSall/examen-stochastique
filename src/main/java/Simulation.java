import umontreal.ssj.simevents.Event;
import umontreal.ssj.simevents.Sim;

import java.io.IOException;

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
            new EndSimulation(simulationDuration).schedule(simulationDuration);
            Sim.start();
            System.out.println("Simulation completed.");
            for (Customer customer : replay.served_customer) {
                System.out.println("Customer LES: " + customer.toString());
            }
            // Add any required post-simulation analysis or results processing here.
            // You can access the data collected in ReplayOneDay, like served_customer, abandon_customer, etc.
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Set the simulation duration and input file path
        double simulationDuration = 6000; // Assuming a 24-hour simulation
        String inputFile = "C:\\Users\\Surface\\Desktop\\Cours modelisation Stochastique\\examen\\VANAD_data\\calls-2014-01.csv";
        Simulation simulation = new Simulation(simulationDuration, inputFile);
        simulation.runSimulation();


    }
}
