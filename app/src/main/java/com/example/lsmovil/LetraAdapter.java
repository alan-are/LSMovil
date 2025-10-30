package com.example.lsmovil;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class LetraAdapter extends RecyclerView.Adapter<LetraAdapter.LetraViewHolder> {

    private List<Letra> letras;
    private Context context;

    public LetraAdapter(Context context, List<Letra> letras) {
        this.context = context;
        this.letras = letras;
    }

    @NonNull
    @Override
    public LetraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_letra, parent, false);
        return new LetraViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LetraViewHolder holder, int position) {
        Letra letra = letras.get(position);
        holder.textViewLetra.setText(letra.getLetra());
        holder.imageViewLetra.setImageResource(letra.getImagenResId());

        // Click listener para mostrar el diálogo
        holder.cardLetra.setOnClickListener(v -> mostrarDialogoDetalle(letra));
    }

    @Override
    public int getItemCount() {
        return letras.size();
    }

    private void mostrarDialogoDetalle(Letra letra) {
        // Crear el diálogo
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_letra_detalle);
        
        // Hacer el fondo del diálogo transparente
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        // Referencias a las vistas del diálogo
        TextView textViewLetraDialog = dialog.findViewById(R.id.textViewLetraDialog);
        ImageView imageViewLetraDialog = dialog.findViewById(R.id.imageViewLetraDialog);
        TextView textViewDescripcionDialog = dialog.findViewById(R.id.textViewDescripcionDialog);
        ImageView btnCerrarDialog = dialog.findViewById(R.id.btnCerrarDialog);
        com.google.android.material.button.MaterialButton btnPracticar = dialog.findViewById(R.id.btnPracticar);

        // Configurar datos
        textViewLetraDialog.setText(letra.getLetra());
        imageViewLetraDialog.setImageResource(letra.getImagenResId());
        textViewDescripcionDialog.setText(letra.getDescripcion());

        // Botón cerrar
        btnCerrarDialog.setOnClickListener(v -> dialog.dismiss());

        // Botón practicar
        btnPracticar.setOnClickListener(v -> {
            dialog.dismiss();
            
            // Crear Intent para PracticarActivity
            android.content.Intent intent = new android.content.Intent(context, PracticarActivity.class);
            intent.putExtra("tipo", "letra");
            intent.putExtra("valor", letra.getLetra());
            intent.putExtra("imagen", letra.getImagenResId());
            intent.putExtra("descripcion", letra.getDescripcion());
            
            context.startActivity(intent);
        });

        // Mostrar el diálogo
        dialog.show();
    }

    static class LetraViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardLetra;
        ImageView imageViewLetra;
        TextView textViewLetra;

        public LetraViewHolder(@NonNull View itemView) {
            super(itemView);
            cardLetra = itemView.findViewById(R.id.cardLetra);
            imageViewLetra = itemView.findViewById(R.id.imageViewLetra);
            textViewLetra = itemView.findViewById(R.id.textViewLetra);
        }
    }
}
