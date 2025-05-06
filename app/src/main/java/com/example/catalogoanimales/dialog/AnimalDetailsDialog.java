package com.example.catalogoanimales.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.bumptech.glide.Glide;
import com.example.catalogoanimales.R;
import com.example.catalogoanimales.model.*;

public class AnimalDetailsDialog extends DialogFragment {
    private Animal animal;
    private ImageView ivPreview;

    public static AnimalDetailsDialog newInstance(Animal animal) {
        AnimalDetailsDialog fragment = new AnimalDetailsDialog();
        Bundle args = new Bundle();
        args.putString("id", animal.getId());
        args.putString("nombre", animal.getNombre());
        args.putString("especie", animal.getEspecie());
        args.putString("habitat", animal.getHabitat());
        args.putString("descripcion", animal.getDescripcion());
        args.putString("imagenUri", animal.getImagenUri());
        args.putString("categoria", animal.getCategoria());
        args.putString("tipo", animal.getClass().getSimpleName());
        args.putDouble("valor", animal.getValor());
        args.putInt("esperanzaVida", animal.getEsperanzaVida());
        args.putDouble("velocidadMaxima", animal.getVelocidadMaxima());
        args.putDouble("alturaPromedio", animal.getAlturaPromedio());

        // Guardar características específicas
        if (animal instanceof Mamifero) {
            Mamifero m = (Mamifero) animal;
            args.putString("tipoPelaje", m.getTipoPelaje());
            args.putString("tipoAlimentacion", m.getTipoAlimentacion());
            args.putBoolean("esNocturno", m.isEsNocturno());
        } else if (animal instanceof Ave) {
            Ave a = (Ave) animal;
            args.putString("tipoPico", a.getTipoPico());
            args.putString("tipoVuelo", a.getTipoVuelo());
            args.putBoolean("puedeVolar", a.isPuedeVolar());
        } else if (animal instanceof Reptil) {
            Reptil r = (Reptil) animal;
            args.putString("tipoEscamas", r.getTipoEscamas());
            args.putString("tipoReproduccion", r.getTipoReproduccion());
            args.putBoolean("esVenenoso", r.isEsVenenoso());
        } else if (animal instanceof Anfibio) {
            Anfibio an = (Anfibio) animal;
            args.putString("tipoPiel", an.getTipoPiel());
            args.putString("etapaVida", an.getEtapaVida());
            args.putBoolean("esVenenosoAnfibio", an.isEsVenenoso());
        } else if (animal instanceof Pez) {
            Pez p = (Pez) animal;
            args.putString("tipoAgua", p.getTipoAgua());
            args.putString("coloracion", p.getColoracion());
            args.putBoolean("esDepredador", p.isEsPredador());
        }

        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_animal_details, null);

        // Inicializar vistas
        ivPreview = view.findViewById(R.id.ivPreview);
        TextView tvNombre = view.findViewById(R.id.tvNombre);
        TextView tvCategoria = view.findViewById(R.id.tvCategoria);
        TextView tvEspecie = view.findViewById(R.id.tvEspecie);
        TextView tvHabitat = view.findViewById(R.id.tvHabitat);
        TextView tvDescripcion = view.findViewById(R.id.tvDescripcion);
        TextView tvValor = view.findViewById(R.id.tvValor);
        LinearLayout layoutCaracteristicas = view.findViewById(R.id.layoutDetalles);

        // Obtener datos del bundle
        Bundle args = getArguments();
        if (args != null) {
            String nombre = args.getString("nombre", "");
            String categoria = args.getString("categoria", "");
            String especie = args.getString("especie", "");
            String habitat = args.getString("habitat", "");
            String descripcion = args.getString("descripcion", "");
            String imagenUri = args.getString("imagenUri", "");
            String tipo = args.getString("tipo", "");
            double valor = args.getDouble("valor", 0.0);

            // Establecer datos básicos
            tvNombre.setText(nombre);
            tvCategoria.setText(categoria);
            tvEspecie.setText(especie);
            tvHabitat.setText(habitat);
            tvDescripcion.setText(descripcion);
            tvValor.setText(String.format("$%.2f", valor));

            // Agregar información adicional
            addCaracteristica(layoutCaracteristicas, "Esperanza de vida", String.valueOf(args.getInt("esperanzaVida", 0)));
            addCaracteristica(layoutCaracteristicas, "Velocidad máxima", args.getDouble("velocidadMaxima", 0) + " km/h");
            addCaracteristica(layoutCaracteristicas, "Altura promedio", args.getDouble("alturaPromedio", 0) + " m");

            // Cargar imagen
            if (imagenUri != null && !imagenUri.isEmpty()) {
                try {
                    Glide.with(requireContext())
                        .load(imagenUri)
                        .placeholder(R.drawable.default_animal)
                        .error(R.drawable.default_animal)
                        .into(ivPreview);
                } catch (Exception e) {
                    ivPreview.setImageResource(R.drawable.default_animal);
                }
            } else {
                ivPreview.setImageResource(R.drawable.default_animal);
            }

            // Agregar características específicas
            switch (tipo) {
                case "Mamifero":
                    addCaracteristica(layoutCaracteristicas, "Tipo de Pelaje", args.getString("tipoPelaje", ""));
                    addCaracteristica(layoutCaracteristicas, "Tipo de Alimentación", args.getString("tipoAlimentacion", ""));
                    addCaracteristica(layoutCaracteristicas, "Es Nocturno", args.getBoolean("esNocturno", false) ? "Sí" : "No");
                    break;
                case "Ave":
                    addCaracteristica(layoutCaracteristicas, "Tipo de Pico", args.getString("tipoPico", ""));
                    addCaracteristica(layoutCaracteristicas, "Tipo de Vuelo", args.getString("tipoVuelo", ""));
                    addCaracteristica(layoutCaracteristicas, "Puede Volar", args.getBoolean("puedeVolar", false) ? "Sí" : "No");
                    break;
                case "Reptil":
                    addCaracteristica(layoutCaracteristicas, "Tipo de Escamas", args.getString("tipoEscamas", ""));
                    addCaracteristica(layoutCaracteristicas, "Tipo de Reproducción", args.getString("tipoReproduccion", ""));
                    addCaracteristica(layoutCaracteristicas, "Es Venenoso", args.getBoolean("esVenenoso", false) ? "Sí" : "No");
                    break;
                case "Anfibio":
                    addCaracteristica(layoutCaracteristicas, "Tipo de Piel", args.getString("tipoPiel", ""));
                    addCaracteristica(layoutCaracteristicas, "Etapa de Vida", args.getString("etapaVida", ""));
                    addCaracteristica(layoutCaracteristicas, "Es Venenoso", args.getBoolean("esVenenosoAnfibio", false) ? "Sí" : "No");
                    break;
                case "Pez":
                    addCaracteristica(layoutCaracteristicas, "Tipo de Agua", args.getString("tipoAgua", ""));
                    addCaracteristica(layoutCaracteristicas, "Coloración", args.getString("coloracion", ""));
                    addCaracteristica(layoutCaracteristicas, "Es Depredador", args.getBoolean("esDepredador", false) ? "Sí" : "No");
                    break;
            }
        }

        builder.setView(view)
               .setTitle("Detalles del Animal")
               .setPositiveButton("Cerrar", (dialog, id) -> dismiss());

        return builder.create();
    }

    private void addCaracteristica(LinearLayout container, String titulo, String valor) {
        View view = getLayoutInflater().inflate(R.layout.item_caracteristica, container, false);
        TextView tvTitulo = view.findViewById(R.id.tvTituloCaracteristica);
        TextView tvValor = view.findViewById(R.id.tvValorCaracteristica);
        
        tvTitulo.setText(titulo);
        tvValor.setText(valor);
        
        container.addView(view);
    }
} 