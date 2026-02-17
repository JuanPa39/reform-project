package co.edu.unipiloto.stationadviser;

import java.util.ArrayList;
import java.util.List;

public class StationExpert {

    public List<String> getStations(String zone) {

        List<String> stations = new ArrayList<>();

        switch (zone) {

            case "Caribe":
                stations.add("Terpel Barranquilla");
                stations.add("Primax Cartagena");
                break;

            case "Andina":
                stations.add("Terpel Bogotá");
                stations.add("Biomax Medellín");
                break;

            case "Pacífica":
                stations.add("Primax Cali");
                stations.add("Terpel Buenaventura");
                break;

            case "Orinoquía":
                stations.add("Biomax Villavicencio");
                break;

            case "Amazonía":
                stations.add("Terpel Leticia");
                break;
        }

        return stations;
    }
}
