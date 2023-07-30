
import umontreal.ssj.simevents.*;
import umontreal.ssj.rng.*;
import umontreal.ssj.randvar.*;
import umontreal.ssj.stat.*;
import java.util.LinkedList;
import java.util.Scanner;
import umontreal.ssj.charts.HistogramChart;
import umontreal.ssj.charts.EmpiricalChart;

public class QueueEvIRN {

    RandomVariateGen genArr;
    RandomVariateGen genServ;
    LinkedList<Customer> waitList = new LinkedList<Customer> ();
    LinkedList<Customer> servList = new LinkedList<Customer> ();
    TallyStore custWaits     = new TallyStore ("Waiting times");
    Accumulate totWait  = new Accumulate ("Size of queue");
    int s;
    class Customer { double arrivTime, servTime; }

    public QueueEvIRN (double lambda, double mu,int s, RandomStream randomMu) {
        genArr = new ExponentialGen (new MRG32k3a(), lambda);
        genServ = new ExponentialGen (randomMu, mu);
        this.s=s;
    }

    public void simulate (double timeHorizon) {
        Sim.init();
        new EndOfSim().schedule (timeHorizon);
        new Arrival().schedule (genArr.nextDouble());
        Sim.start();
    }

    class Arrival extends Event {
        public void actions() {
            new Arrival().schedule (genArr.nextDouble()); // Next arrival.
            Customer cust = new Customer();  // Cust just arrived.
            cust.arrivTime = Sim.time();
            cust.servTime = genServ.nextDouble();
            if (servList.size() > s-1) {       // Must join the queue.
                waitList.addLast (cust);
                totWait.update (waitList.size());
            } else {                         // Starts service.
                custWaits.add (0.0);
                servList.addLast (cust);
                new Departure().schedule (cust.servTime);
            }
        }
    }

    class Departure extends Event {
        public void actions() {
            servList.removeFirst();
            if (waitList.size() > 0) {
                // Starts service for next one in queue.
                Customer cust = waitList.removeFirst();
                totWait.update (waitList.size());
                custWaits.add (Sim.time() - cust.arrivTime);
                servList.addLast (cust);
                new Departure().schedule (cust.servTime);
            }
        }
    }

    class EndOfSim extends Event {
        public void actions() {
            Sim.stop();
        }
    }

    public static void main (String[] args) {
        int s=1;
        double mu1=3.5;
        double mu2=2.5;
        double lambda= 2;
        /********************* IRN  ***************************************/
        RandomStream streamServ=new MRG32k3a();

        /********************* S1 ***************************************/
        QueueEvIRN queue1 = new QueueEvIRN (lambda, mu1,s, streamServ );
        queue1.simulate (1000.0);
        //double W1=queue1.custWaits.average();
        double data1[]=queue1.custWaits.getArray();
        System.out.println("Taille 1="+data1.length);

        /*********************** S2  *************************************/
        QueueEvIRN queue2 = new QueueEvIRN (lambda, mu2,s, streamServ );
        queue2.simulate (1000.0);
        // double W2=queue2.custWaits.average();

        double data2[]=queue2.custWaits.getArray();
        System.out.println("Taille 2="+data2.length);



        double data[]=new double[data2.length] ;

        Tally diff=new Tally("Difference Waiting time");
        for(int i=0;i<data2.length;i++)
        {
            data[i]=data2[i]-data1[i];
            diff.add(data2[i]-data1[i]);
        }

        System.out.println (diff.report());
        HistogramChart hist = new HistogramChart("Distribution of $T$",
                "Values of $T$", "Frequency", data, data.length);
        hist.view(800, 500);
    }
}
