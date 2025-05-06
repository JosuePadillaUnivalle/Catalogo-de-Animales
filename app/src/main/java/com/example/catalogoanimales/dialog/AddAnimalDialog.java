package com.example.catalogoanimales.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.catalogoanimales.R;
import com.example.catalogoanimales.model.Animal;
import com.example.catalogoanimales.model.Anfibio;
import com.example.catalogoanimales.model.Ave;
import com.example.catalogoanimales.model.AveRapaz;
import com.example.catalogoanimales.model.Mamifero;
import com.example.catalogoanimales.model.Pez;
import com.example.catalogoanimales.model.Reptil;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class AddAnimalDialog extends DialogFragment {
    private static final String TAG = "AddAnimalDialog";
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int RESULT_OK = Activity.RESULT_OK;
    private String categoriaActual;
    private OnAnimalAddedListener listener;
    private View dialogView;
    private EditText etNombre, etEspecie, etHabitat, etDescripcion, etImagenUrl, etTemperaturaCorporal, etTiempoGestacion, etTipoAlimentacion, etEnvergaduraAlas, etColorPlumaje, etVelocidadVuelo, etTipoPresa;
    private ImageView ivPreview;
    private Button btnSelectImage;
    private MaterialAutoCompleteTextView spinnerCategoria;
    private AlertDialog dialog;
    private Uri selectedImageUri;

    public interface OnAnimalAddedListener {
        void onAnimalAdded(Animal animal);
    }

    public void setOnAnimalAddedListener(OnAnimalAddedListener listener) {
        this.listener = listener;
    }

    public static AddAnimalDialog newInstance(String categoria) {
        AddAnimalDialog dialog = new AddAnimalDialog();
        Bundle args = new Bundle();
        args.putString("categoria", categoria);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoriaActual = getArguments().getString("categoria", "Mamíferos");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnAnimalAddedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " debe implementar OnAnimalAddedListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        
        // Inflar y guardar la vista
        dialogView = inflater.inflate(R.layout.dialog_add_animal, null);
        
        // Inicializar vistas
        setupViews(dialogView);
        
        // Configurar el spinner de categorías
        setupSpinner();
        
        // Crear el diálogo
        AlertDialog dialog = builder.setView(dialogView)
                     .setTitle("Agregar Animal")
                     .setPositiveButton("Guardar", (dialog1, id) -> guardarAnimal())
                     .setNegativeButton("Cancelar", (dialog1, id) -> dismiss())
                     .create();
        
        // Mostrar campos específicos según la categoría actual después de que el diálogo esté creado
        dialog.setOnShowListener(dialogInterface -> {
            if (categoriaActual != null) {
                showSpecificFields(categoriaActual);
            }
        });
        
        return dialog;
    }

    private void setupViews(View view) {
        if (view == null) return;
        
        etNombre = view.findViewById(R.id.etNombre);
        etEspecie = view.findViewById(R.id.etEspecie);
        etHabitat = view.findViewById(R.id.etHabitat);
        etDescripcion = view.findViewById(R.id.etDescripcion);
        etImagenUrl = view.findViewById(R.id.etImagenUrl);
        ivPreview = view.findViewById(R.id.ivPreview);
        spinnerCategoria = view.findViewById(R.id.spinnerCategoria);
        btnSelectImage = view.findViewById(R.id.btnSelectImage);
        
        // Configurar el botón de seleccionar imagen
        if (btnSelectImage != null) {
            btnSelectImage.setOnClickListener(v -> openImagePicker());
        }
        
        // Configurar el preview de imagen
        if (ivPreview != null) {
            ivPreview.setOnClickListener(v -> openImagePicker());
        }
    }

    private void setupSpinner() {
        String[] categorias = {"Mamíferos", "Aves", "Aves Rapaces", "Reptiles", "Anfibios", "Peces"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, categorias);
        spinnerCategoria.setAdapter(adapter);
        spinnerCategoria.setKeyListener(null);
        spinnerCategoria.setInputType(0);
        spinnerCategoria.setFocusable(false);
        spinnerCategoria.setCursorVisible(false);
        if (categoriaActual != null) {
            spinnerCategoria.setText(categoriaActual, false);
        }
        // Mostrar los campos únicos de la categoría seleccionada al abrir el diálogo
        showSpecificFields(spinnerCategoria.getText().toString());
        spinnerCategoria.setOnItemClickListener((parent, view1, position, id) -> {
            String categoriaSeleccionada = parent.getItemAtPosition(position).toString();
            showSpecificFields(categoriaSeleccionada);
        });
    }

    private void mostrarCamposCategoria(String categoria) {
        // Primero ocultar todos los campos
        ocultarTodosCamposEspecificos();

        // Mostrar los campos según la categoría actual
        Toast.makeText(requireContext(), "Mostrando campos para: " + categoria, Toast.LENGTH_SHORT).show();

        switch (categoria) {
            case "Mamíferos":
                dialogView.findViewById(R.id.layoutMamifero).setVisibility(View.VISIBLE);
                dialogView.findViewById(R.id.layoutTipoAlimentacion).setVisibility(View.VISIBLE);
                dialogView.findViewById(R.id.switchEsNocturno).setVisibility(View.VISIBLE);
                break;
            case "Aves":
                dialogView.findViewById(R.id.layoutAve).setVisibility(View.VISIBLE);
                // El layoutAveRapaz se mostrará/ocultará según el estado del switch
                SwitchMaterial switchEsRapaz = dialogView.findViewById(R.id.switchEsRapaz);
                if (switchEsRapaz != null && switchEsRapaz.isChecked()) {
                    dialogView.findViewById(R.id.layoutAveRapaz).setVisibility(View.VISIBLE);
                }
                break;
            case "Reptiles":
                dialogView.findViewById(R.id.layoutReptil).setVisibility(View.VISIBLE);
                dialogView.findViewById(R.id.layoutTipoReproduccion).setVisibility(View.VISIBLE);
                dialogView.findViewById(R.id.switchEsVenenoso).setVisibility(View.VISIBLE);
                break;
            case "Anfibios":
                dialogView.findViewById(R.id.layoutAnfibio).setVisibility(View.VISIBLE);
                dialogView.findViewById(R.id.layoutEtapaVida).setVisibility(View.VISIBLE);
                dialogView.findViewById(R.id.switchEsVenenosoAnfibio).setVisibility(View.VISIBLE);
                break;
            case "Peces":
                dialogView.findViewById(R.id.layoutPez).setVisibility(View.VISIBLE);
                dialogView.findViewById(R.id.layoutTipoAletas).setVisibility(View.VISIBLE);
                dialogView.findViewById(R.id.switchEsDepredador).setVisibility(View.VISIBLE);
                break;
        }
    }

    private void ocultarTodosCamposEspecificos() {
        int[] ids = {
            R.id.layoutMamifero, R.id.layoutTipoAlimentacion, R.id.switchEsNocturno,
            R.id.layoutAve, R.id.layoutTipoVuelo, R.id.switchPuedeVolar,
            R.id.layoutAveRapaz,
            R.id.layoutReptil, R.id.layoutTipoReproduccion, R.id.switchEsVenenoso,
            R.id.layoutAnfibio, R.id.layoutEtapaVida, R.id.switchEsVenenosoAnfibio,
            R.id.layoutPez, R.id.layoutTipoAletas, R.id.switchEsDepredador
        };

        for (int id : ids) {
            View view = dialogView.findViewById(id);
            if (view != null) {
                view.setVisibility(View.GONE);
            }
        }
    }

    private void guardarAnimal() {
        try {
            // Obtener valores comunes
            String nombre = etNombre.getText().toString().trim();
            String especie = etEspecie.getText().toString().trim();
            String habitat = etHabitat.getText().toString().trim();
            String descripcion = etDescripcion.getText().toString().trim();
            String categoria = spinnerCategoria.getText().toString();

            Toast.makeText(getContext(), "Categoría seleccionada: " + categoria, Toast.LENGTH_LONG).show();
            // Depuración: mostrar valor antes del switch/case
            Toast.makeText(getContext(), "Switch/case categoría: " + categoria, Toast.LENGTH_LONG).show();

            // Validar campos obligatorios
            if (nombre.isEmpty() || especie.isEmpty() || habitat.isEmpty() || descripcion.isEmpty()) {
                Toast.makeText(getContext(), "Por favor complete todos los campos obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            // Crear el animal según su categoría
            Animal animalNuevo = null;
            String imagenUrl = etImagenUrl.getText().toString().trim();
            String esperanzaVidaStr = "0";
            EditText etValor = dialogView.findViewById(R.id.etValor);
            if (etValor != null) {
                esperanzaVidaStr = etValor.getText().toString().trim();
            }
            Toast.makeText(getContext(), "URL imagen: " + imagenUrl, Toast.LENGTH_LONG).show();
            Toast.makeText(getContext(), "Esperanza de vida: " + esperanzaVidaStr, Toast.LENGTH_LONG).show();

            switch (categoria) {
                case "Mamíferos":
                    EditText etTipoPelaje = dialogView.findViewById(R.id.etTipoPelaje);
                    EditText etTipoAlimentacion = dialogView.findViewById(R.id.etTipoAlimentacion);
                    SwitchMaterial switchEsNocturno = dialogView.findViewById(R.id.switchEsNocturno);

                    if (etTipoPelaje == null || etTipoAlimentacion == null || switchEsNocturno == null) {
                        Toast.makeText(getContext(), "Error: Campos de mamífero no encontrados", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    animalNuevo = new Mamifero(
                        nombre, especie, habitat, descripcion, imagenUrl, categoria,
                        Double.parseDouble(etTemperaturaCorporal.getText().toString().trim()),
                        Integer.parseInt(etTiempoGestacion.getText().toString().trim()),
                        etTipoAlimentacion.getText().toString().trim()
                    );
                    ((Mamifero) animalNuevo).setTipoPelaje(etTipoPelaje.getText().toString().trim());
                    ((Mamifero) animalNuevo).setEsNocturno(switchEsNocturno.isChecked());
                    break;

                case "Aves":
                case "Aves Rapaces":
                    EditText etEnvergaduraAlas = dialogView.findViewById(R.id.etEnvergaduraAlas);
                    EditText etColorPlumaje = dialogView.findViewById(R.id.etColorPlumaje);
                    EditText etTipoPico = dialogView.findViewById(R.id.etTipoPico);
                    EditText etVelocidadVuelo = dialogView.findViewById(R.id.etVelocidadVuelo);
                    EditText etTipoPresa = dialogView.findViewById(R.id.etTipoPresa);
                    SwitchMaterial switchPuedeVolar = dialogView.findViewById(R.id.switchPuedeVolar);

                    if (etEnvergaduraAlas == null || etColorPlumaje == null || etTipoPico == null || 
                        etVelocidadVuelo == null || switchPuedeVolar == null) {
                        Toast.makeText(getContext(), "Error: Campos de ave no encontrados", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (categoria.equals("Aves Rapaces")) {
                        if (etTipoPresa == null) {
                            Toast.makeText(getContext(), "Error: Campos de ave rapaz no encontrados", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        animalNuevo = new AveRapaz(
                            nombre, especie, habitat, descripcion, imagenUrl, categoria,
                            Double.parseDouble(etEnvergaduraAlas.getText().toString().trim()),
                            etColorPlumaje.getText().toString().trim(),
                            etTipoPico.getText().toString().trim(),
                            Double.parseDouble(etVelocidadVuelo.getText().toString().trim()),
                            etTipoPresa.getText().toString().trim()
                        );
                    } else {
                        animalNuevo = new Ave(
                            nombre, especie, habitat, descripcion, imagenUrl, categoria,
                            Double.parseDouble(etEnvergaduraAlas.getText().toString().trim()),
                            etColorPlumaje.getText().toString().trim(),
                            etTipoPico.getText().toString().trim()
                        );
                        ((Ave) animalNuevo).setTipoVuelo(etVelocidadVuelo.getText().toString().trim());
                        ((Ave) animalNuevo).setPuedeVolar(switchPuedeVolar.isChecked());
                    }
                    break;

                case "Reptiles":
                    EditText etTipoEscamas = dialogView.findViewById(R.id.etTipoEscamas);
                    EditText etTipoReproduccion = dialogView.findViewById(R.id.etTipoReproduccion);
                    SwitchMaterial switchEsVenenoso = dialogView.findViewById(R.id.switchEsVenenoso);

                    if (etTipoEscamas == null || etTipoReproduccion == null || switchEsVenenoso == null) {
                        Toast.makeText(getContext(), "Error: Campos de reptil no encontrados", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    animalNuevo = new Reptil(
                        nombre, especie, habitat, descripcion, imagenUrl, categoria,
                        etTipoEscamas.getText().toString().trim(),
                        etTipoReproduccion.getText().toString().trim(),
                        switchEsVenenoso.isChecked()
                    );
                    break;

                case "Anfibios":
                    EditText etTipoPiel = dialogView.findViewById(R.id.etTipoPiel);
                    SwitchMaterial switchEsVenenosoAnfibio = dialogView.findViewById(R.id.switchEsVenenosoAnfibio);

                    if (etTipoPiel == null || switchEsVenenosoAnfibio == null) {
                        Toast.makeText(getContext(), "Error: Campos de anfibio no encontrados", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    animalNuevo = new Anfibio(
                        nombre, especie, habitat, descripcion, imagenUrl, categoria,
                        etTipoPiel.getText().toString().trim(),
                        switchEsVenenosoAnfibio.isChecked()
                    );
                    break;

                case "Peces":
                    EditText etTipoAgua = dialogView.findViewById(R.id.etTipoAgua);
                    EditText etColoracion = dialogView.findViewById(R.id.etColoracion);
                    SwitchMaterial switchEsDepredador = dialogView.findViewById(R.id.switchEsDepredador);

                    if (etTipoAgua == null || etColoracion == null || switchEsDepredador == null) {
                        Toast.makeText(getContext(), "Error: Campos de pez no encontrados", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    animalNuevo = new Pez(
                        nombre, especie, habitat, descripcion, imagenUrl, categoria,
                        etTipoAgua.getText().toString().trim(),
                        etColoracion.getText().toString().trim(),
                        switchEsDepredador.isChecked()
                    );
                    break;
            }

            if (animalNuevo != null) {
                try {
                    animalNuevo.setEsperanzaVida((int) Double.parseDouble(esperanzaVidaStr));
                } catch (Exception e) {
                    animalNuevo.setEsperanzaVida(0);
                }
                // Generar un ID único para el animal
                animalNuevo.setId(nombre.toLowerCase() + "-" + System.currentTimeMillis());
                
                // Notificar al listener
                if (listener != null) {
                    listener.onAnimalAdded(animalNuevo);
                    Toast.makeText(getContext(), "Animal agregado correctamente", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            } else {
                Toast.makeText(getContext(), "Error al crear el animal", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error al guardar el animal: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar Imagen"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            ImageView ivPreview = getView().findViewById(R.id.ivPreview);
            Glide.with(this)
                .load(selectedImageUri)
                .into(ivPreview);
        }
    }

    private void showSpecificFields(String categoria) {
        if (dialogView == null) {
            return;
        }
        View layoutMamifero = dialogView.findViewById(R.id.layoutMamifero);
        View layoutAve = dialogView.findViewById(R.id.layoutAve);
        View layoutAveRapaz = dialogView.findViewById(R.id.layoutAveRapaz);
        View layoutReptil = dialogView.findViewById(R.id.layoutReptil);
        View layoutAnfibio = dialogView.findViewById(R.id.layoutAnfibio);
        View layoutPez = dialogView.findViewById(R.id.layoutPez);
        if (layoutMamifero != null) layoutMamifero.setVisibility(View.GONE);
        if (layoutAve != null) layoutAve.setVisibility(View.GONE);
        if (layoutAveRapaz != null) layoutAveRapaz.setVisibility(View.GONE);
        if (layoutReptil != null) layoutReptil.setVisibility(View.GONE);
        if (layoutAnfibio != null) layoutAnfibio.setVisibility(View.GONE);
        if (layoutPez != null) layoutPez.setVisibility(View.GONE);
        switch (categoria) {
            case "Mamíferos":
                if (layoutMamifero != null) layoutMamifero.setVisibility(View.VISIBLE);
                break;
            case "Aves":
                if (layoutAve != null) layoutAve.setVisibility(View.VISIBLE);
                break;
            case "Aves Rapaces":
                if (layoutAve != null) layoutAve.setVisibility(View.VISIBLE);
                if (layoutAveRapaz != null) layoutAveRapaz.setVisibility(View.VISIBLE);
                break;
            case "Reptiles":
                if (layoutReptil != null) layoutReptil.setVisibility(View.VISIBLE);
                break;
            case "Anfibios":
                if (layoutAnfibio != null) layoutAnfibio.setVisibility(View.VISIBLE);
                break;
            case "Peces":
                if (layoutPez != null) layoutPez.setVisibility(View.VISIBLE);
                break;
        }
    }
} 