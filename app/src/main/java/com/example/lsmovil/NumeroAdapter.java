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

public class NumeroAdapter extends RecyclerView.Adapter<NumeroAdapter.NumeroViewHolder> {

    private List<Numero> numeros;
    private Context context;

    public NumeroAdapter(Context context, List<Numero> numeros) {
        this.context = context;
        this.numeros = numeros;
    }

    @NonNull
    @Override
    public NumeroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_numero, parent, false);
        return new NumeroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NumeroViewHolder holder, int position) {
        Numero numero = numeros.get(position);
        holder.textViewNumero.setText(numero.getNumero());
        holder.imageViewNumero.setImageResource(numero.getImagenResId());

        // Click listener para mostrar el diálogo
        holder.cardNumero.setOnClickListener(v -> mostrarDialogoDetalle(numero));
    }

    @Override
    public int getItemCount() {
        return numeros.size();
    }

    private void mostrarDialogoDetalle(Numero numero) {
        // Crear el diálogo
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_numero_detalle);
        
        // Hacer el fondo del diálogo transparente
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        // Referencias a las vistas del diálogo
        TextView textViewNumeroDialog = dialog.findViewById(R.id.textViewNumeroDialog);
        ImageView imageViewNumeroDialog = dialog.findViewById(R.id.imageViewNumeroDialog);
        TextView textViewDescripcionDialog = dialog.findViewById(R.id.textViewDescripcionDialog);
        ImageView btnCerrarDialog = dialog.findViewById(R.id.btnCerrarDialog);
        com.google.android.material.button.MaterialButton btnPracticar = dialog.findViewById(R.id.btnPracticar);

        // Configurar datos
        textViewNumeroDialog.setText(numero.getNumero());
        imageViewNumeroDialog.setImageResource(numero.getImagenResId());
        textViewDescripcionDialog.setText(numero.getDescripcion());

        // Botón cerrar
        btnCerrarDialog.setOnClickListener(v -> dialog.dismiss());

        // Botón practicar
        btnPracticar.setOnClickListener(v -> {
            dialog.dismiss();
            
            // Crear Intent para PracticarActivity
            android.content.Intent intent = new android.content.Intent(context, PracticarActivity.class);
            intent.putExtra("tipo", "numero");
            intent.putExtra("valor", numero.getNumero());
            intent.putExtra("imagen", numero.getImagenResId());
            intent.putExtra("descripcion", numero.getDescripcion());
            
            context.startActivity(intent);
        });

        // Mostrar el diálogo
        dialog.show();
    }

    static class NumeroViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardNumero;
        ImageView imageViewNumero;
        TextView textViewNumero;

        public NumeroViewHolder(@NonNull View itemView) {
            super(itemView);
            cardNumero = itemView.findViewById(R.id.cardNumero);
            imageViewNumero = itemView.findViewById(R.id.imageViewNumero);
            textViewNumero = itemView.findViewById(R.id.textViewNumero);
        }
    }
}
