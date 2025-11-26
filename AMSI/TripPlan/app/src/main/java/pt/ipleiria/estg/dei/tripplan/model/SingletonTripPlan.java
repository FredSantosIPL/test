package pt.ipleiria.estg.dei.tripplan.model;

import java.util.ArrayList;

public class SingletonTripPlan {

    // 1. Instância estática
    private static SingletonTripPlan instance = null;
    private ArrayList<Trip> trips;

    // Construtor privado
    private SingletonTripPlan() {
        trips = new ArrayList<>();
        generateFakeData();
    }

    public static synchronized SingletonTripPlan getInstance() {
        if (instance == null) {
            instance = new SingletonTripPlan();
        }
        return instance;
    }


    private void generateFakeData() {
        trips.add(new Trip(1, "Férias Verão", "Algarve", "2026-08-01", 500.0));
        trips.add(new Trip(2, "Conferência Tech", "Lisboa", "2026-11-10", 150.0));
        trips.add(new Trip(3, "Fim de Ano", "Madeira", "2026-12-31", 800.0));
    }

    public ArrayList<Trip> getTrips() {
        return new ArrayList<>(trips);
    }

    public void addTrip(Trip trip) {
        trips.add(trip);
    }
}