package pt.ipleiria.estg.dei.tripplan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import pt.ipleiria.estg.dei.tripplan.R;
import pt.ipleiria.estg.dei.tripplan.model.Trip;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    private ArrayList<Trip> trips;
    private Context context;
    private LayoutInflater inflater;

    public TripAdapter(Context context, ArrayList<Trip> trips) {
        this.context = context;
        this.trips = trips;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // "Insufla" o layout item_trip.xml para criar a visualização da linha
        View view = inflater.inflate(R.layout.item_trip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Obtém a viagem na posição atual
        Trip trip = trips.get(position);

        // Preenche os dados nos componentes visuais
        holder.tvTitle.setText(trip.getTitle());
        holder.tvDestination.setText(trip.getDestination());
        holder.tvDate.setText(trip.getStartDate());
        holder.tvBudget.setText(trip.getBudget() + " €");
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    // Classe interna que guarda as referências aos elementos visuais
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDestination, tvDate, tvBudget;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvBudget = itemView.findViewById(R.id.tvBudget);
        }
    }
}