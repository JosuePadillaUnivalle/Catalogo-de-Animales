package com.example.catalogoanimales.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.catalogoanimales.fragments.AnimalListFragment;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class AnimalPagerAdapter extends FragmentStateAdapter {
    private final List<String> categorias;
    private final FragmentManager fragmentManager;
    private final Map<Integer, Fragment> fragmentMap;

    public AnimalPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<String> categorias) {
        super(fragmentActivity);
        this.categorias = categorias;
        this.fragmentManager = fragmentActivity.getSupportFragmentManager();
        this.fragmentMap = new HashMap<>();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        try {
            // Verificar si ya existe un fragmento para esta posición
            Fragment existingFragment = fragmentMap.get(position);
            if (existingFragment != null && existingFragment.isAdded()) {
                return existingFragment;
            }

            // Si no existe o no está añadido, crear uno nuevo
            String categoria = categorias.get(position);
            AnimalListFragment newFragment = AnimalListFragment.newInstance(categoria);
            fragmentMap.put(position, newFragment);
            return newFragment;
        } catch (Exception e) {
            e.printStackTrace();
            // En caso de error, devolver un fragmento vacío
            return AnimalListFragment.newInstance("Error");
        }
    }

    @Override
    public int getItemCount() {
        return categorias != null ? categorias.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean containsItem(long itemId) {
        return itemId >= 0 && itemId < getItemCount();
    }
} 