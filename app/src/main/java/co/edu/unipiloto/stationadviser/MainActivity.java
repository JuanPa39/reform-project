package co.edu.unipiloto.stationadviser;

import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickFindStation(View view) {

        Spinner spinner = findViewById(R.id.zones);
        String zone = String.valueOf(spinner.getSelectedItem());

        TextView stationsView = findViewById(R.id.stations);

        StationExpert expert = new StationExpert();
        List<String> stationList = expert.getStations(zone);

        StringBuilder formattedStations = new StringBuilder();

        for (String station : stationList) {
            formattedStations.append(station).append("\n");
        }

        stationsView.setText(formattedStations);
    }
}
