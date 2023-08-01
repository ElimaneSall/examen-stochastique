import umontreal.ssj.simevents.Sim;

public class Tester {

    public static void main(String[] args) {
        ReplayOneDay replayOneDay = new ReplayOneDay();

        try {
            // Charger les données des clients à partir du fichier
            String fileName = "C:\\Users\\Surface\\Desktop\\Cours modelisation Stochastique\\examen\\VANAD_data\\calls-2014-01.csv";
            replayOneDay.createCustomerOfTheDay(fileName);

            // Définir la durée de la simulation (peut être ajustée selon les besoins)
            double simulationDuration = 3600; // 24 heures en secondes

            // Démarrer la simulation
            Sim.init();

            Sim.start();
            Sim.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
