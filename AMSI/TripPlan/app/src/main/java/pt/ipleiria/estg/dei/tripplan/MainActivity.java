package pt.ipleiria.estg.dei.tripplan;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import pt.ipleiria.estg.dei.tripplan.adapters.TripAdapter;
import pt.ipleiria.estg.dei.tripplan.model.SingletonTripPlan;
import pt.ipleiria.estg.dei.tripplan.model.Trip;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvTrips;
    private TripAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Encontrar a RecyclerView no layout
        rvTrips = findViewById(R.id.rvTrips);

        // 2. Definir como a lista se comporta (Lista vertical padrão)
        rvTrips.setLayoutManager(new LinearLayoutManager(this));

        // 3. Obter os dados do Singleton
        ArrayList<Trip> trips = SingletonTripPlan.getInstance().getTrips();

        // 4. Criar e associar o Adapter
        adapter = new TripAdapter(this, trips);
        rvTrips.setAdapter(adapter);
    }
}