package com.example.catalogoanimales;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import androidx.appcompat.widget.SearchView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.example.catalogoanimales.adapter.AnimalPagerAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.catalogoanimales.dialog.AddAnimalDialog;
import com.example.catalogoanimales.fragments.AnimalListFragment;
import com.example.catalogoanimales.model.Animal;
import com.example.catalogoanimales.api.ApiClient;
import java.util.Arrays;
import java.util.List;
import android.os.Handler;
import android.os.Looper;
import androidx.fragment.app.Fragment;
import com.example.catalogoanimales.interfaces.OnAnimalCategoryChangedListener;
import android.widget.Toast;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.catalogoanimales.api.AnimalesResponse;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements AddAnimalDialog.OnAnimalAddedListener, OnAnimalCategoryChangedListener {
    private static final long SEARCH_DELAY_MS = 300;
    private static final String FRAGMENT_TAG_PREFIX = "f";

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private MaterialToolbar toolbar;
    private SearchView searchView;
    private FloatingActionButton fabAddAnimal;
    private List<String> categorias = Arrays.asList("Mamíferos", "Aves", "Reptiles", "Anfibios", "Peces");
    private AnimalPagerAdapter pagerAdapter;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupToolbar();
        setupViewPager();
        setupSearchView();
        setupFab();
        setupApi();
        cargarAnimales();
    }

    private void initializeViews() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        toolbar = findViewById(R.id.toolbar);
        searchView = findViewById(R.id.searchView);
        fabAddAnimal = findViewById(R.id.fabAddAnimal);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        
        swipeRefreshLayout.setOnRefreshListener(this::cargarAnimales);
    }

    private void setupApi() {
        ApiClient.init(this);
    }

    private void cargarAnimales() {
        swipeRefreshLayout.setRefreshing(true);
        try {
            AnimalesResponse response = ApiClient.getAnimales();
            if (response != null && response.getAnimales() != null) {
                List<Animal> animales = response.getAnimales();
                Toast.makeText(MainActivity.this, "Número de animales cargados: " + animales.size(), Toast.LENGTH_LONG).show();
                procesarAnimales(animales);
            } else {
                Toast.makeText(MainActivity.this, "La lista de animales está vacía", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            String errorMsg = "Error al cargar los animales: " + e.getMessage();
            Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void procesarAnimales(List<Animal> animales) {
        // Primero limpiar las listas existentes en todos los fragmentos
        for (String categoria : categorias) {
            AnimalListFragment fragment = getFragmentByCategory(categoria);
            if (fragment != null) {
                fragment.clearAnimals();
            }
        }

        // Luego agregar los nuevos animales
        for (Animal animal : animales) {
            Toast.makeText(MainActivity.this, "Procesando animal: " + animal.getNombre() + " - " + animal.getCategoria(), Toast.LENGTH_SHORT).show();
            AnimalListFragment fragment = getFragmentByCategory(animal.getCategoria());
            if (fragment != null) {
                fragment.addAnimalToList(animal);
            }
        }
        
        Toast.makeText(MainActivity.this, "Animales cargados correctamente", Toast.LENGTH_SHORT).show();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }
    }

    private void setupViewPager() {
        // Crear y configurar el adaptador primero
        pagerAdapter = new AnimalPagerAdapter(this, categorias);
        
        // Configurar el ViewPager2
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);

        // Configurar el TabLayout después de que el adaptador esté listo
        new TabLayoutMediator(tabLayout, viewPager,
            (tab, position) -> tab.setText(categorias.get(position))
        ).attach();

        // Agregar un listener para manejar los cambios de página
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                try {
                    // Actualizar la lista del fragmento actual
                    AnimalListFragment currentFragment = getFragmentByPosition(position);
                    if (currentFragment != null) {
                        currentFragment.actualizarLista();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchRunnable != null) {
                    handler.removeCallbacks(searchRunnable);
                }
                searchRunnable = () -> {
                    // Aplicar la búsqueda a todos los fragmentos
                    for (String categoria : categorias) {
                        AnimalListFragment fragment = getFragmentByCategory(categoria);
                        if (fragment != null) {
                            fragment.filtrarAnimales(newText);
                        }
                    }
                };
                handler.postDelayed(searchRunnable, SEARCH_DELAY_MS);
                return true;
            }
        });
    }

    private void setupFab() {
        fabAddAnimal.setOnClickListener(v -> {
            String categoriaActual = categorias.get(viewPager.getCurrentItem());
            AddAnimalDialog dialog = AddAnimalDialog.newInstance(categoriaActual);
            dialog.show(getSupportFragmentManager(), "AddAnimalDialog");
        });
    }

    private AnimalListFragment getFragmentByPosition(int position) {
        try {
            String tag = FRAGMENT_TAG_PREFIX + position;
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
            if (fragment instanceof AnimalListFragment) {
                return (AnimalListFragment) fragment;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private AnimalListFragment getCurrentFragment() {
        return getFragmentByPosition(viewPager.getCurrentItem());
    }

    private AnimalListFragment getFragmentByCategory(String category) {
        int position = categorias.indexOf(category);
        if (position != -1) {
            return getFragmentByPosition(position);
        }
        return null;
    }

    @Override
    public void onAnimalAdded(Animal animal) {
        try {
            // Obtener el fragmento de la categoría del animal
            AnimalListFragment targetFragment = getFragmentByCategory(animal.getCategoria());
            
            if (targetFragment != null) {
                targetFragment.addAnimalToList(animal);
                
                // Si el animal es de una categoría diferente a la actual, cambiar a esa categoría
                if (!animal.getCategoria().equals(categorias.get(viewPager.getCurrentItem()))) {
                    int newPosition = categorias.indexOf(animal.getCategoria());
                    if (newPosition != -1) {
                        viewPager.setCurrentItem(newPosition);
                    }
                }
                
                Toast.makeText(this, "Animal agregado a la categoría: " + animal.getCategoria(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error: No se pudo encontrar el fragmento para la categoría " + animal.getCategoria(), Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al agregar el animal: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAnimalCategoryChanged(Animal animal, String oldCategory, String newCategory) {
        int newPosition = categorias.indexOf(newCategory);
        if (newPosition != -1) {
            viewPager.setCurrentItem(newPosition);
            
            AnimalListFragment newCategoryFragment = getFragmentByCategory(newCategory);
            if (newCategoryFragment != null) {
                newCategoryFragment.addAnimalToList(animal);
                
                AnimalListFragment oldCategoryFragment = getFragmentByCategory(oldCategory);
                if (oldCategoryFragment != null) {
                    oldCategoryFragment.actualizarLista();
                }
            }
        }
    }
}