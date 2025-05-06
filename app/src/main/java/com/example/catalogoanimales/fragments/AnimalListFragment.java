package com.example.catalogoanimales.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.catalogoanimales.DetailActivity;
import com.example.catalogoanimales.R;
import com.example.catalogoanimales.adapter.AnimalAdapter;
import com.example.catalogoanimales.model.Animal;
import com.example.catalogoanimales.model.Mamifero;
import com.example.catalogoanimales.model.Ave;
import com.example.catalogoanimales.model.AveRapaz;
import com.example.catalogoanimales.model.Reptil;
import com.example.catalogoanimales.model.Anfibio;
import com.example.catalogoanimales.model.Pez;
import com.example.catalogoanimales.dialog.AddAnimalDialog;
import com.example.catalogoanimales.dialog.EditAnimalDialogFragment;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import android.widget.Toast;
import com.example.catalogoanimales.interfaces.OnAnimalCategoryChangedListener;
import com.example.catalogoanimales.dialog.AnimalDetailsDialog;

public class AnimalListFragment extends Fragment implements AnimalAdapter.OnAnimalClickListener, AddAnimalDialog.OnAnimalAddedListener, EditAnimalDialogFragment.OnAnimalEditedListener {
    // Constantes para categorías
    public static final String CATEGORIA_MAMIFERO = "Mamífero";
    public static final String CATEGORIA_AVE = "Ave";
    public static final String CATEGORIA_AVE_RAPAZ = "Ave Rapaz";
    public static final String CATEGORIA_REPTIL = "Reptil";
    public static final String CATEGORIA_ANFIBIO = "Anfibio";
    public static final String CATEGORIA_PEZ = "Pez";

    private static final String ARG_CATEGORIA = "categoria";
    private String categoria;
    private RecyclerView recyclerView;
    private AnimalAdapter adapter;
    private List<Animal> animales;
    private List<Animal> animalesFiltrados;
    private SwipeRefreshLayout swipeRefreshLayout;
    private OnAnimalCategoryChangedListener categoryChangedListener;
    public static List<Animal> animalesGlobales = new ArrayList<>();
    private static List<Animal> animalesPredeterminados = new ArrayList<>();
    private static boolean predeterminadosCargados = false;

    public static AnimalListFragment newInstance(String categoria) {
        AnimalListFragment fragment = new AnimalListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORIA, categoria);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoria = getArguments().getString(ARG_CATEGORIA);
        }
        
        // Inicializar las listas
        animales = new ArrayList<>();
        animalesFiltrados = new ArrayList<>();
        
        // Cargar animales predeterminados solo una vez
        if (!predeterminadosCargados) {
            cargarAnimalesPredeterminados();
            predeterminadosCargados = true;
        }
    }

    private void cargarAnimalesPredeterminados() {
        // Mamífero
        Mamifero leon = new Mamifero(
            "León",
            "Panthera leo",
            "Sabana africana",
            "El león es un gran felino carnívoro que vive en manadas.",
            "https://upload.wikimedia.org/wikipedia/commons/7/73/Lion_waiting_in_Namibia.jpg",
            "Mamíferos",
            37.5,  // temperaturaCorporal
            110,   // tiempoGestacion
            "Carnívoro"  // tipoAlimentacion
        );
        leon.setNombreCientifico("Panthera leo");
        leon.setEstadoConservacion("Vulnerable");
        leon.setPesoPromedio(190.0);
        leon.setEsperanzaVida(35);  // Los leones viven entre 10-15 años, en escala 35/100
        leon.setVelocidadMaxima(80);
        leon.setAlturaPromedio(1.2);
        animalesPredeterminados.add(leon);

        // Ave
        Ave paloma = new Ave(
            "Paloma",
            "Columba livia",
            "Urbano",
            "La paloma es un ave común en entornos urbanos.",
            "https://upload.wikimedia.org/wikipedia/commons/thumb/a/aa/PigeonMonceau_%28cropped%29.jpg/250px-PigeonMonceau_%28cropped%29.jpg",
            "Aves",
            50.0,  // envergaduraAlas
            "Gris",  // colorPlumaje
            "Corto y fuerte"  // tipoPico
        );
        paloma.setNombreCientifico("Columba livia");
        paloma.setEstadoConservacion("Preocupación menor");
        paloma.setPesoPromedio(0.3);
        paloma.setEsperanzaVida(15);  // Las palomas viven 5-6 años, en escala 15/100
        paloma.setVelocidadMaxima(77);
        paloma.setAlturaPromedio(0.25);
        animalesPredeterminados.add(paloma);

        // Ave Rapaz
        AveRapaz aguila = new AveRapaz(
            "Águila Real",
            "Aquila chrysaetos",
            "Montañas",
            "El águila real es una de las aves rapaces más grandes.",
            "https://bloximages.newyork1.vip.townnews.com/ivpressonline.com/content/tncms/assets/v3/editorial/f/d0/fd0f6b8c-e14c-11e9-8175-4f576f812cc5/5d8e48162893f.image.jpg",
            "Aves",
            220.0,  // envergaduraAlas
            "Marrón dorado",  // colorPlumaje
            "Gancho",  // tipoPico
            320.0,  // velocidadVuelo
            "Pequeños mamíferos"  // tipoPresa
        );
        aguila.setNombreCientifico("Aquila chrysaetos");
        aguila.setEstadoConservacion("Preocupación menor");
        aguila.setPesoPromedio(4.5);
        aguila.setEsperanzaVida(45);  // Las águilas viven 20-25 años, en escala 45/100
        aguila.setVelocidadMaxima(320);
        aguila.setAlturaPromedio(0.9);
        animalesPredeterminados.add(aguila);

        // Reptil
        Reptil cocodrilo = new Reptil(
            "Cocodrilo del Nilo",
            "Crocodylus niloticus",
            "Ríos y lagos",
            "El cocodrilo del Nilo es uno de los reptiles más grandes.",
            "https://upload.wikimedia.org/wikipedia/commons/b/bd/Nile_crocodile_head.jpg",
            "Reptiles",
            "Escamas duras",
            "Oviparo",
            false
        );
        cocodrilo.setNombreCientifico("Crocodylus niloticus");
        cocodrilo.setEstadoConservacion("Preocupación menor");
        cocodrilo.setPesoPromedio(500.0);
        cocodrilo.setEsperanzaVida(85);  // Los cocodrilos viven 70-100 años, en escala 85/100
        cocodrilo.setVelocidadMaxima(35);
        cocodrilo.setAlturaPromedio(0.5);
        animalesPredeterminados.add(cocodrilo);

        // Anfibio
        Anfibio rana = new Anfibio(
            "Rana verde",
            "Pelophylax perezi",
            "Zonas húmedas",
            "La rana verde es común en charcas y estanques.",
            "https://inaturalist-open-data.s3.amazonaws.com/photos/4065940/original.jpg",
            "Anfibios",
            "Húmeda y permeable",
            false
        );
        rana.setEtapaVida("Metamorfosis");
        rana.setNombreCientifico("Pelophylax perezi");
        rana.setEstadoConservacion("Preocupación menor");
        rana.setPesoPromedio(0.05);
        rana.setEsperanzaVida(20);  // Las ranas viven 8-10 años, en escala 20/100
        rana.setVelocidadMaxima(8);
        rana.setAlturaPromedio(0.08);
        animalesPredeterminados.add(rana);

        // Pez
        Pez tiburonBlanco = new Pez(
            "Tiburón Blanco", 
            "Carcharodon carcharias", 
            "Océanos templados", 
            "El gran tiburón blanco es uno de los depredadores más temidos de los océanos", 
            "https://upload.wikimedia.org/wikipedia/commons/5/56/White_shark.jpg", 
            "Peces", 
            "Salada", 
            "Gris azulado", 
            true
        );
        tiburonBlanco.setEsperanzaVida(90);  // Los tiburones blancos viven 70+ años, en escala 90/100
        tiburonBlanco.setVelocidadMaxima(56);
        tiburonBlanco.setAlturaPromedio(4.5);
        animalesPredeterminados.add(tiburonBlanco);

        // Agregar todos los animales predeterminados a la lista global
        for (Animal animal : animalesPredeterminados) {
            if (!animalesGlobales.contains(animal)) {
                animalesGlobales.add(animal);
            }
        }
        System.out.println("Animales predeterminados cargados. Total en lista global: " + animalesGlobales.size());
        
        // Actualizar la lista local con los animales de esta categoría
        actualizarLista();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animal_list, container, false);
        
        recyclerView = view.findViewById(R.id.recyclerViewAnimales);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        
        // Usar GridLayoutManager con 2 columnas
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        
        // Inicializar el adaptador
        animales = new ArrayList<>();
        animalesFiltrados = new ArrayList<>();
        adapter = new AnimalAdapter(requireContext(), animalesFiltrados, this);
        recyclerView.setAdapter(adapter);
        
        // Cargar los animales predeterminados si es necesario
        if (!predeterminadosCargados) {
            cargarAnimalesPredeterminados();
            predeterminadosCargados = true;
        }
        
        // Agregar los animales predeterminados a la lista actual si corresponden a esta categoría
        for (Animal animal : animalesPredeterminados) {
            if (animal.getCategoria().equals(categoria)) {
                addAnimalToList(animal);
            }
        }
        
        // Actualizar la lista
        actualizarLista();

        // Configurar SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(() -> {
            actualizarLista();
            swipeRefreshLayout.setRefreshing(false);
        });
        
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            categoryChangedListener = (OnAnimalCategoryChangedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " debe implementar OnAnimalCategoryChangedListener");
        }
    }

    private void showAnimalOptions(Animal animal, int position) {
        String[] options;
        if (animal.isPredeterminado()) {
            options = new String[]{"Detalles", "Eliminar"};
        } else {
            options = new String[]{"Detalles", "Editar", "Eliminar"};
        }
        
        new AlertDialog.Builder(requireContext())
            .setTitle("Opciones")
            .setItems(options, (dialog, which) -> {
                if (animal.isPredeterminado()) {
                    switch (which) {
                        case 0: // Detalles
                            AnimalDetailsDialog detailsDialog = AnimalDetailsDialog.newInstance(animal);
                            detailsDialog.show(getParentFragmentManager(), "AnimalDetailsDialog");
                            break;
                        case 1: // Eliminar
                            if (animal.isPredeterminado()) {
                                Toast.makeText(getContext(), "No se pueden eliminar animales predeterminados", Toast.LENGTH_SHORT).show();
                            } else {
                                showDeleteConfirmation(animal, position);
                            }
                            break;
                    }
                } else {
                    // Para animales no predeterminados
                    switch (which) {
                        case 0: // Detalles
                            AnimalDetailsDialog detailsDialog = AnimalDetailsDialog.newInstance(animal);
                            detailsDialog.show(getParentFragmentManager(), "AnimalDetailsDialog");
                            break;
                        case 1: // Editar
                            Animal animalToEdit = animal.clone();
                            EditAnimalDialogFragment editDialog = EditAnimalDialogFragment.newInstance(animalToEdit);
                            editDialog.setOnAnimalEditedListener(this);
                            editDialog.show(getParentFragmentManager(), "EditAnimalDialog");
                            break;
                        case 2: // Eliminar
                            showDeleteConfirmation(animal, position);
                            break;
                    }
                }
            })
            .show();
    }

    private void showDeleteConfirmation(Animal animal, int position) {
        new AlertDialog.Builder(requireContext())
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar este animal?")
            .setPositiveButton("Sí", (dialog, which) -> {
                deleteAnimal(animal, position);
            })
            .setNegativeButton("No", null)
            .show();
    }

    private void deleteAnimal(Animal animal, int position) {
        // Eliminar de la lista global si existe
        animalesGlobales.removeIf(a -> a.getId().equals(animal.getId()));
        
        // Eliminar de la lista de predeterminados si existe
        animalesPredeterminados.removeIf(a -> a.getNombre().equals(animal.getNombre()) && 
                                             a.getCategoria().equals(animal.getCategoria()));
        
        // Eliminar de la lista local
        animales.removeIf(a -> a.getId().equals(animal.getId()));

        // Actualizar las listas locales
        actualizarLista();
        
        // Mostrar mensaje de éxito
        Toast.makeText(getContext(), 
            "Animal eliminado correctamente", 
            Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAnimalEdited(Animal animal) {
        if (animal == null) return;

        // Depuración: mostrar datos del animal editado
        Toast.makeText(getContext(), "onAnimalEdited: " + animal.getNombre() + " - " + animal.getCategoria(), Toast.LENGTH_LONG).show();

        // Si el animal editado pertenece a otra categoría
        if (!animal.getCategoria().equals(categoria)) {
            // Eliminar el animal de la lista global si existe
            animalesGlobales.removeIf(a -> a.getId().equals(animal.getId()));
            // Eliminar de la lista local
            animales.removeIf(a -> a.getId().equals(animal.getId()));
            // Agregar el animal editado a la lista global
            if (!animalesGlobales.contains(animal)) {
                animalesGlobales.add(animal);
            }
            // Si el usuario está viendo la nueva categoría, agregarlo a la lista local
            if (animal.getCategoria().equals(categoria)) {
                addAnimalToList(animal);
            }
            // Actualizar la vista actual
            actualizarLista();

            // Notificar el cambio de categoría
            if (categoryChangedListener != null) {
                categoryChangedListener.onAnimalCategoryChanged(animal, categoria, animal.getCategoria());
            }
            Toast.makeText(getContext(),
                    "Animal movido a la categoría " + animal.getCategoria(),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Actualizar el animal en la lista local
        for (int i = 0; i < animales.size(); i++) {
            if (animales.get(i).getId().equals(animal.getId())) {
                animales.set(i, animal);
                break;
            }
        }
        // Actualizar el animal en la lista global
        for (int i = 0; i < animalesGlobales.size(); i++) {
            if (animalesGlobales.get(i).getId().equals(animal.getId())) {
                animalesGlobales.set(i, animal);
                break;
            }
        }
        actualizarLista();

            Toast.makeText(getContext(), 
                "Animal actualizado correctamente", 
                Toast.LENGTH_SHORT).show();
    }

    public void filtrarAnimales(String query) {
        if (query == null || query.isEmpty()) {
            // Si no hay búsqueda, mostrar solo los animales de esta categoría
            animalesFiltrados = new ArrayList<>(animales);
        } else {
            String queryLower = query.toLowerCase();
            
            // Buscar en la lista global de todos los animales
            animalesFiltrados = animalesGlobales.stream()
                .filter(animal -> 
                    animal.getNombre().toLowerCase().contains(queryLower) ||
                    animal.getEspecie().toLowerCase().contains(queryLower) ||
                    animal.getDescripcion().toLowerCase().contains(queryLower))
                .collect(Collectors.toList());
        }
        
        if (adapter != null) {
            adapter.actualizarLista(animalesFiltrados);
        }
    }

    public void filtrarAnimalesGlobal(String query) {
        if (query == null || query.isEmpty()) {
            mostrarTodosLosAnimales();
            return;
        }

        // Obtener todos los animales que coinciden con la búsqueda de la lista global
        List<Animal> resultadosBusqueda = animalesGlobales.stream()
            .filter(animal ->
                animal.getNombre().toLowerCase().contains(query) ||
                animal.getEspecie().toLowerCase().contains(query) ||
                animal.getDescripcion().toLowerCase().contains(query))
            .collect(Collectors.toList());

        // Actualizar la lista del fragmento con solo los resultados que corresponden a esta categoría
        animalesFiltrados.clear();
        for (Animal animal : resultadosBusqueda) {
            if (animal.getCategoria().equals(categoria)) {
                animalesFiltrados.add(animal);
            }
        }
        
        // Actualizar el adaptador
        if (adapter != null) {
            adapter.actualizarLista(animalesFiltrados);
        }
    }

    public void mostrarTodosLosAnimales() {
        // Sincronizar la lista local con la global
        animales.clear();
        for (Animal animal : animalesGlobales) {
            if (animal.getCategoria().equals(categoria)) {
                animales.add(animal);
            }
        }
        // Mostrar solo los animales de la categoría actual
        animalesFiltrados.clear();
        animalesFiltrados.addAll(animales);
        if (adapter != null) {
            adapter.actualizarLista(animalesFiltrados);
        }
    }

    public void actualizarLista() {
        if (adapter != null) {
            animalesFiltrados.clear();
            animalesFiltrados.addAll(animales);
            adapter.actualizarLista(animalesFiltrados);
        }
    }

    @Override
    public void onAnimalClick(Animal animal, int position) {
        showAnimalOptions(animal, position);
    }

    @Override
    public void onAnimalAdded(Animal animal) {
        if (animal != null) {
            // Agregar a la lista global
            if (!animalesGlobales.contains(animal)) {
                animalesGlobales.add(animal);
                System.out.println("Animal agregado a lista global. Total: " + animalesGlobales.size());
            }
            
            // Si corresponde a esta categoría, agregarlo a la lista local
            if (animal.getCategoria().equals(categoria)) {
                addAnimalToList(animal);
            }
        }
    }

    public void addAnimalToList(Animal animal) {
        if (animal != null && animal.getCategoria().equals(categoria)) {
            if (!animales.contains(animal)) {
                animales.add(animal);
                // Agregar a la lista global si no existe
                if (!animalesGlobales.contains(animal)) {
                    animalesGlobales.add(animal);
                }
                actualizarLista();
            }
        }
    }

    public void clearAnimals() {
        // Filtrar para mantener solo los animales predeterminados
        List<Animal> animalesPredeterminadosActuales = animales.stream()
                .filter(Animal::isPredeterminado)
                .collect(Collectors.toList());
        
        animales.clear();
        // Restaurar los animales predeterminados
        animales.addAll(animalesPredeterminadosActuales);
        actualizarLista();
    }

    public String getCategoria() {
        return categoria;
    }
} 