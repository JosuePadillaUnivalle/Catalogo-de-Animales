package com.example.catalogoanimales.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.catalogoanimales.R;
import com.example.catalogoanimales.model.Animal;
import com.example.catalogoanimales.model.Mamifero;
import com.example.catalogoanimales.model.Ave;
import com.example.catalogoanimales.model.AveRapaz;
import com.example.catalogoanimales.model.Pez;
import com.example.catalogoanimales.model.Anfibio;
import com.example.catalogoanimales.model.Reptil;
import java.util.List;

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.AnimalViewHolder> {
    private List<Animal> animales;
    private OnAnimalClickListener listener;
    private Context context;

    public interface OnAnimalClickListener {
        void onAnimalClick(Animal animal, int position);
    }

    public AnimalAdapter(Context context, List<Animal> animales, OnAnimalClickListener listener) {
        this.context = context;
        this.animales = animales;
        this.listener = listener;
    }

    public void actualizarLista(List<Animal> nuevosAnimales) {
        this.animales = nuevosAnimales;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_animal, parent, false);
        return new AnimalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalViewHolder holder, int position) {
        Animal animal = animales.get(position);
        holder.bind(animal, position);
    }

    @Override
    public int getItemCount() {
        return animales != null ? animales.size() : 0;
    }

    class AnimalViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final ImageView ivAnimal;
        private final TextView tvNombre;
        private final TextView tvEspecie;
        private final TextView tvCategoria;

        AnimalViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            ivAnimal = itemView.findViewById(R.id.imageViewAnimal);
            tvNombre = itemView.findViewById(R.id.textViewNombre);
            tvEspecie = itemView.findViewById(R.id.textViewEspecie);
            tvCategoria = itemView.findViewById(R.id.textViewCategoria);
        }

        void bind(Animal animal, int position) {
            tvNombre.setText(animal.getNombre());
            tvEspecie.setText(animal.getEspecie());
            tvCategoria.setText(animal.getCategoria());

            // Establecer color según el tipo de animal
            int colorResId;
            if (animal instanceof Mamifero) {
                colorResId = R.color.mamifero_color;
            } else if (animal instanceof AveRapaz) {
                colorResId = R.color.ave_rapaz_color;
            } else if (animal instanceof Ave) {
                colorResId = R.color.ave_color;
            } else if (animal instanceof Pez) {
                colorResId = R.color.pez_color;
            } else if (animal instanceof Anfibio) {
                colorResId = R.color.anfibio_color;
            } else if (animal instanceof Reptil) {
                colorResId = R.color.reptil_color;
            } else {
                colorResId = R.color.default_color;
            }
            
            cardView.setCardBackgroundColor(ContextCompat.getColor(itemView.getContext(), colorResId));

            // Cargar imagen con Glide
            String imageUrl = animal.getImagenUri();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                if (imageUrl.startsWith("http")) {
                    // Para URLs web
                    Glide.with(itemView.getContext())
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.placeholder_animal)
                        .error(R.drawable.error_image)
                        .timeout(15000) // Aumentar el timeout a 15 segundos
                        .into(ivAnimal);
                } else {
                    // Para URIs locales o recursos
                    try {
                        Uri uri = Uri.parse(imageUrl);
                        Glide.with(itemView.getContext())
                            .load(uri)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder_animal)
                            .error(R.drawable.error_image)
                            .timeout(15000) // Aumentar el timeout a 15 segundos
                            .into(ivAnimal);
                    } catch (Exception e) {
                        ivAnimal.setImageResource(R.drawable.placeholder_animal);
                    }
                }
            } else {
                ivAnimal.setImageResource(R.drawable.placeholder_animal);
            }

            cardView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAnimalClick(animal, position);
                }
            });

            // Añadir efecto de elevación al pasar el dedo
            cardView.setOnTouchListener((v, event) -> {
                switch (event.getAction()) {
                    case android.view.MotionEvent.ACTION_DOWN:
                        cardView.setCardElevation(16f);
                        break;
                    case android.view.MotionEvent.ACTION_UP:
                    case android.view.MotionEvent.ACTION_CANCEL:
                        cardView.setCardElevation(8f);
                        break;
                }
                return false;
            });
        }
    }
} 