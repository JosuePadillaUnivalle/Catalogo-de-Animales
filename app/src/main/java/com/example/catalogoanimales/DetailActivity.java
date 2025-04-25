package com.example.catalogoanimales;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.example.catalogoanimales.model.Animal;
import com.example.catalogoanimales.model.Ave;
import com.example.catalogoanimales.model.Mamifero;
import com.example.catalogoanimales.model.Reptil;
import com.example.catalogoanimales.model.Anfibio;
import com.example.catalogoanimales.model.Pez;
import com.example.catalogoanimales.model.AveRapaz;

public class DetailActivity extends AppCompatActivity {
    private ImageView ivAnimalDetail;
    private TextView tvNombreDetail;
    private TextView tvEspecieDetail;
    private TextView tvHabitatDetail;
    private TextView tvDescripcionDetail;
    private LinearLayout layoutCaracteristicas;
    private Animal animal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Obtener el animal del intent
        animal = (Animal) getIntent().getSerializableExtra("animal");

        // Inicializar vistas
        ivAnimalDetail = findViewById(R.id.ivAnimalDetail);
        tvNombreDetail = findViewById(R.id.tvNombreDetail);
        tvEspecieDetail = findViewById(R.id.tvEspecieDetail);
        tvHabitatDetail = findViewById(R.id.tvHabitatDetail);
        tvDescripcionDetail = findViewById(R.id.tvDescripcionDetail);
        layoutCaracteristicas = findViewById(R.id.layoutCaracteristicas);

        // Configurar toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(animal.getNombre());

        // Cargar datos del animal
        cargarDatosAnimal();
    }

    private void cargarDatosAnimal() {
        // Cargar imagen
        Glide.with(this)
            .load(animal.getImagenUri())
            .placeholder(R.drawable.placeholder_animal)
            .error(R.drawable.error_animal)
            .into(ivAnimalDetail);

        // Cargar datos básicos
        tvNombreDetail.setText(animal.getNombre());
        tvEspecieDetail.setText(animal.getEspecie());
        tvHabitatDetail.setText(animal.getHabitat());
        tvDescripcionDetail.setText(animal.getDescripcion());

        // Cargar características específicas según el tipo de animal
        layoutCaracteristicas.removeAllViews();
        
        if (animal instanceof Mamifero) {
            Mamifero mamifero = (Mamifero) animal;
            agregarCaracteristica("Temperatura Corporal", String.format("%.1f °C", mamifero.getTemperaturaCorporal()));
            agregarCaracteristica("Tiempo de Gestación", mamifero.getTiempoGestacion() + " días");
            agregarCaracteristica("Tipo de Alimentación", mamifero.getTipoAlimentacion());
        } else if (animal instanceof Ave) {
            Ave ave = (Ave) animal;
            agregarCaracteristica("Envergadura de Alas", String.format("%.1f cm", ave.getEnvergaduraAlas()));
            agregarCaracteristica("Color de Plumaje", ave.getColorPlumaje());
            agregarCaracteristica("Tipo de Pico", ave.getTipoPico());
            
            if (animal instanceof AveRapaz) {
                AveRapaz aveRapaz = (AveRapaz) animal;
                agregarCaracteristica("Velocidad de Vuelo", String.format("%.1f km/h", aveRapaz.getVelocidadVuelo()));
                agregarCaracteristica("Tipo de Presa", aveRapaz.getTipoPresa());
            }
        } else if (animal instanceof Reptil) {
            Reptil reptil = (Reptil) animal;
            agregarCaracteristica("Es venenoso", reptil.isEsVenenoso() ? "Sí" : "No");
            agregarCaracteristica("Tipo de escamas", reptil.getTipoEscamas());
            agregarCaracteristica("Tipo de reproducción", reptil.getTipoReproduccion());
        } else if (animal instanceof Anfibio) {
            Anfibio anfibio = (Anfibio) animal;
            agregarCaracteristica("Es venenoso", anfibio.isEsVenenoso() ? "Sí" : "No");
            agregarCaracteristica("Tipo de piel", anfibio.getTipoPiel());
            agregarCaracteristica("Etapa de vida", anfibio.getEtapaVida());
        } else if (animal instanceof Pez) {
            Pez pez = (Pez) animal;
            agregarCaracteristica("Tipo de agua", pez.getTipoAgua());
            agregarCaracteristica("Coloración", pez.getColoracion());
            agregarCaracteristica("Es depredador", pez.isEsPredador() ? "Sí" : "No");
        }
    }

    private void agregarCaracteristica(String titulo, String valor) {
        View caracteristicaView = getLayoutInflater().inflate(R.layout.item_caracteristica, layoutCaracteristicas, false);
        
        TextView tvTitulo = caracteristicaView.findViewById(R.id.tvTituloCaracteristica);
        TextView tvValor = caracteristicaView.findViewById(R.id.tvValorCaracteristica);
        
        tvTitulo.setText(titulo);
        tvValor.setText(valor);
        
        layoutCaracteristicas.addView(caracteristicaView);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 