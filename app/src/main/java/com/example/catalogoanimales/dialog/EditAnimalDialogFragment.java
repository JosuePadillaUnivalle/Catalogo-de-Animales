package com.example.catalogoanimales.dialog;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.bumptech.glide.Glide;
import com.example.catalogoanimales.R;
import com.example.catalogoanimales.model.Animal;
import com.example.catalogoanimales.model.Mamifero;
import com.example.catalogoanimales.model.Ave;
import com.example.catalogoanimales.model.AveRapaz;
import com.example.catalogoanimales.model.Reptil;
import com.example.catalogoanimales.model.Anfibio;
import com.example.catalogoanimales.model.Pez;
import com.google.android.material.switchmaterial.SwitchMaterial;
import android.widget.AdapterView;

public class EditAnimalDialogFragment extends DialogFragment {
    private static final String TAG = "EditAnimalDialogFragment";
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText etNombre;
    private EditText etDescripcion;
    private AutoCompleteTextView spinnerCategoria;
    private EditText etImagenUrl;
    private ImageView ivPreview;
    private Animal animalToEdit;
    private OnAnimalEditedListener listener;
    private EditText etTipoVuelo;
    private EditText etEspecie;
    private EditText etHabitat;
    private EditText etValor;
    private EditText etTemperaturaCorporal;
    private EditText etTiempoGestacion;
    private EditText etTipoAlimentacion;
    private EditText etEnvergaduraAlas;
    private EditText etColorPlumaje;
    private EditText etVelocidadVuelo;
    private EditText etTipoPresa;
    private SwitchMaterial switchEsRapaz;
    private SwitchMaterial switchPuedeVolar;

    public interface OnAnimalEditedListener {
        void onAnimalEdited(Animal animal);
    }

    public static EditAnimalDialogFragment newInstance(Animal animal) {
        EditAnimalDialogFragment fragment = new EditAnimalDialogFragment();
        Bundle args = new Bundle();
        
        // En lugar de serializar el animal completo, guardamos sus datos básicos
        args.putString("id", animal.getId());
        args.putString("nombre", animal.getNombre());
        args.putString("especie", animal.getEspecie());
        args.putString("habitat", animal.getHabitat());
        args.putString("descripcion", animal.getDescripcion());
        args.putString("imagenUri", animal.getImagenUri());
        args.putString("categoria", animal.getCategoria());
        args.putBoolean("isPredeterminado", animal.isPredeterminado());
        args.putDouble("valor", animal.getValor());
        
        // Guardamos el tipo específico de animal
        args.putString("tipo", animal.getClass().getSimpleName());
        
        // Guardamos datos específicos según el tipo de animal
        if (animal instanceof Mamifero) {
            Mamifero m = (Mamifero) animal;
            args.putString("tipoPelaje", m.getTipoPelaje());
            args.putString("tipoAlimentacion", m.getTipoAlimentacion());
            args.putBoolean("esNocturno", m.isEsNocturno());
            args.putDouble("temperaturaCorporal", m.getTemperaturaCorporal());
            args.putInt("tiempoGestacion", m.getTiempoGestacion());
        } else if (animal instanceof Ave) {
            Ave a = (Ave) animal;
            args.putString("tipoPico", a.getTipoPico());
            args.putString("tipoVuelo", a.getTipoVuelo());
            args.putBoolean("puedeVolar", a.isPuedeVolar());
            args.putDouble("envergaduraAlas", a.getEnvergaduraAlas());
            args.putString("colorPlumaje", a.getColorPlumaje());
            
            if (animal instanceof AveRapaz) {
                AveRapaz aveRapaz = (AveRapaz) animal;
                args.putDouble("velocidadVuelo", aveRapaz.getVelocidadVuelo());
                args.putString("tipoPresa", aveRapaz.getTipoPresa());
            }
        } else if (animal instanceof Reptil) {
            Reptil r = (Reptil) animal;
            args.putString("tipoEscamas", r.getTipoEscamas());
            args.putString("tipoReproduccion", r.getTipoReproduccion());
            args.putBoolean("esVenenoso", r.isEsVenenoso());
        } else if (animal instanceof Anfibio) {
            Anfibio an = (Anfibio) animal;
            args.putString("tipoPiel", an.getTipoPiel());
            args.putString("etapaVida", an.getEtapaVida());
            args.putBoolean("esVenenoso", an.isEsVenenoso());
        } else if (animal instanceof Pez) {
            Pez p = (Pez) animal;
            args.putString("tipoAgua", p.getTipoAgua());
            args.putString("coloracion", p.getColoracion());
            args.putBoolean("esDepredador", p.isEsPredador());
        }
        
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Creamos un nuevo animal del tipo correcto con los datos guardados
            Bundle args = getArguments();
            String tipo = args.getString("tipo");
            
            String id = args.getString("id");
            String nombre = args.getString("nombre");
            String especie = args.getString("especie");
            String habitat = args.getString("habitat");
            String descripcion = args.getString("descripcion");
            String imagenUri = args.getString("imagenUri");
            String categoria = args.getString("categoria");
            boolean isPredeterminado = args.getBoolean("isPredeterminado");
            
            switch (tipo) {
                case "Mamifero":
                    animalToEdit = new Mamifero(
                        nombre, especie, habitat, descripcion, imagenUri, categoria,
                        args.getDouble("temperaturaCorporal", 0.0),
                        args.getInt("tiempoGestacion", 0),
                        args.getString("tipoAlimentacion", "")
                    );
                    ((Mamifero) animalToEdit).setTipoPelaje(args.getString("tipoPelaje", ""));
                    ((Mamifero) animalToEdit).setEsNocturno(args.getBoolean("esNocturno", false));
                    break;
                case "Ave":
                    animalToEdit = new Ave(
                        nombre, especie, habitat, descripcion, imagenUri, categoria,
                        args.getDouble("envergaduraAlas", 0.0),
                        args.getString("colorPlumaje", ""),
                        args.getString("tipoPico", "")
                    );
                    ((Ave) animalToEdit).setTipoVuelo(args.getString("tipoVuelo", ""));
                    ((Ave) animalToEdit).setPuedeVolar(args.getBoolean("puedeVolar", true));
                    break;
                case "AveRapaz":
                    animalToEdit = new AveRapaz(
                        nombre, especie, habitat, descripcion, imagenUri, categoria,
                        args.getDouble("envergaduraAlas", 0.0),
                        args.getString("colorPlumaje", ""),
                        args.getString("tipoPico", ""),
                        args.getDouble("velocidadVuelo", 0.0),
                        args.getString("tipoPresa", "")
                    );
                    break;
                case "Reptil":
                    animalToEdit = new Reptil(
                        nombre, especie, habitat, descripcion, imagenUri, categoria,
                        args.getString("tipoEscamas", ""),
                        args.getString("tipoReproduccion", ""),
                        args.getBoolean("esVenenoso", false)
                    );
                    break;
                case "Anfibio":
                    animalToEdit = new Anfibio(
                        nombre, especie, habitat, descripcion, imagenUri, categoria,
                        args.getString("tipoPiel", ""),
                        args.getBoolean("esVenenoso", false)
                    );
                    break;
                case "Pez":
                    animalToEdit = new Pez(
                        nombre, especie, habitat, descripcion, imagenUri, categoria,
                        args.getString("tipoAgua", ""),
                        args.getString("coloracion", ""),
                        args.getBoolean("esDepredador", false)
                    );
                    break;
            }
            
            if (animalToEdit != null) {
                animalToEdit.setId(id);
                animalToEdit.setPredeterminado(isPredeterminado);
            }
        }
    }

    public void setOnAnimalEditedListener(OnAnimalEditedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getActivity() == null) {
            return super.onCreateDialog(savedInstanceState);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_animal, null);

        // Inicializar vistas
        etNombre = view.findViewById(R.id.etNombre);
        etDescripcion = view.findViewById(R.id.etDescripcion);
        spinnerCategoria = view.findViewById(R.id.spinnerCategoria);
        ivPreview = view.findViewById(R.id.ivPreview);
        etImagenUrl = view.findViewById(R.id.etImagenUrl);
        etTipoVuelo = view.findViewById(R.id.etVelocidadVuelo);
        etEspecie = view.findViewById(R.id.etEspecie);
        etHabitat = view.findViewById(R.id.etHabitat);
        etValor = view.findViewById(R.id.etValor);
        etTemperaturaCorporal = view.findViewById(R.id.etTemperaturaCorporal);
        etTiempoGestacion = view.findViewById(R.id.etTiempoGestacion);
        etTipoAlimentacion = view.findViewById(R.id.etTipoAlimentacion);
        etEnvergaduraAlas = view.findViewById(R.id.etEnvergaduraAlas);
        etColorPlumaje = view.findViewById(R.id.etColorPlumaje);
        etVelocidadVuelo = view.findViewById(R.id.etVelocidadVuelo);
        etTipoPresa = view.findViewById(R.id.etTipoPresa);
        switchEsRapaz = view.findViewById(R.id.switchEsRapaz);
        switchPuedeVolar = view.findViewById(R.id.switchPuedeVolar);

        // Configurar el Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.categorias_animales,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);
        
        // Establecer la categoría actual
        if (animalToEdit != null) {
            int position = adapter.getPosition(animalToEdit.getCategoria());
            if (position >= 0) {
                spinnerCategoria.setSelection(position);
            }
        }

        // Configurar el listener del Spinner
        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View spinnerView, int position, long id) {
                String categoriaSeleccionada = parent.getItemAtPosition(position).toString();
                actualizarCamposEspecificos(view, categoriaSeleccionada);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });

        ivPreview.setOnClickListener(v -> selectImage());

        // Cargar datos del animal a editar
        if (animalToEdit != null) {
            etNombre.setText(animalToEdit.getNombre());
            etDescripcion.setText(animalToEdit.getDescripcion());
            
            // Cargar imagen
            String imagenUri = animalToEdit.getImagenUri();
            if (imagenUri != null && !imagenUri.isEmpty()) {
                etImagenUrl.setText(imagenUri);
                try {
                    if (getActivity() != null) {
                        Glide.with(getActivity())
                            .load(imagenUri)
                            .placeholder(R.drawable.default_animal)
                            .error(R.drawable.default_animal)
                            .into(ivPreview);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // Si hay un error al cargar la imagen, mostrar la imagen por defecto
                    ivPreview.setImageResource(R.drawable.default_animal);
                }
            } else {
                // Si no hay imagen, mostrar la imagen por defecto
                ivPreview.setImageResource(R.drawable.default_animal);
            }

            // Cargar campos comunes
            if (etEspecie != null) etEspecie.setText(animalToEdit.getEspecie());
            if (etHabitat != null) etHabitat.setText(animalToEdit.getHabitat());

            // Mostrar campos específicos según la categoría
            actualizarCamposEspecificos(view, animalToEdit.getCategoria());

            // Cargar valores específicos según el tipo de animal
            switch (animalToEdit.getCategoria()) {
                case "Mamíferos":
                    if (animalToEdit instanceof Mamifero) {
                        Mamifero mamifero = (Mamifero) animalToEdit;
                        EditText etTipoPelaje = view.findViewById(R.id.etTipoPelaje);
                        EditText etTipoAlimentacion = view.findViewById(R.id.etTipoAlimentacion);
                        SwitchMaterial switchEsNocturno = view.findViewById(R.id.switchEsNocturno);
                        
                        if (etTipoPelaje != null) etTipoPelaje.setText(mamifero.getTipoPelaje());
                        if (etTipoAlimentacion != null) etTipoAlimentacion.setText(mamifero.getTipoAlimentacion());
                        if (switchEsNocturno != null) switchEsNocturno.setChecked(mamifero.isEsNocturno());
                        if (etTemperaturaCorporal != null) etTemperaturaCorporal.setText(String.valueOf(mamifero.getTemperaturaCorporal()));
                        if (etTiempoGestacion != null) etTiempoGestacion.setText(String.valueOf(mamifero.getTiempoGestacion()));
                    }
                    break;
                case "Aves":
                    if (animalToEdit instanceof Ave) {
                        Ave ave = (Ave) animalToEdit;
                        EditText etTipoPico = view.findViewById(R.id.etTipoPico);
                        EditText etTipoVuelo = view.findViewById(R.id.etTipoVuelo);
                        SwitchMaterial switchPuedeVolar = view.findViewById(R.id.switchPuedeVolar);
                        
                        if (etTipoPico != null) etTipoPico.setText(ave.getTipoPico());
                        if (etTipoVuelo != null) etTipoVuelo.setText(ave.getTipoVuelo());
                        if (switchPuedeVolar != null) switchPuedeVolar.setChecked(ave.isPuedeVolar());
                        if (etEnvergaduraAlas != null) etEnvergaduraAlas.setText(String.valueOf(ave.getEnvergaduraAlas()));
                        if (etColorPlumaje != null) etColorPlumaje.setText(ave.getColorPlumaje());
                        
                        if (animalToEdit instanceof AveRapaz) {
                            AveRapaz aveRapaz = (AveRapaz) animalToEdit;
                            if (switchEsRapaz != null) switchEsRapaz.setChecked(true);
                            if (etVelocidadVuelo != null) etVelocidadVuelo.setText(String.valueOf(aveRapaz.getVelocidadVuelo()));
                            if (etTipoPresa != null) etTipoPresa.setText(aveRapaz.getTipoPresa());
                        }
                    }
                    break;
                case "Reptiles":
                    if (animalToEdit instanceof Reptil) {
                        Reptil reptil = (Reptil) animalToEdit;
                        EditText etTipoEscamas = view.findViewById(R.id.etTipoEscamas);
                        EditText etTipoReproduccion = view.findViewById(R.id.etTipoReproduccion);
                        SwitchMaterial switchEsVenenosoReptil = view.findViewById(R.id.switchEsVenenoso);
                        
                        if (etTipoEscamas != null) etTipoEscamas.setText(reptil.getTipoEscamas());
                        if (etTipoReproduccion != null) etTipoReproduccion.setText(reptil.getTipoReproduccion());
                        if (switchEsVenenosoReptil != null) switchEsVenenosoReptil.setChecked(reptil.isEsVenenoso());
                    }
                    break;
                case "Anfibios":
                    if (animalToEdit instanceof Anfibio) {
                        Anfibio anfibio = (Anfibio) animalToEdit;
                        EditText etTipoPiel = view.findViewById(R.id.etTipoPiel);
                        EditText etEtapaVida = view.findViewById(R.id.etEtapaVida);
                        SwitchMaterial switchEsVenenosoAnfibio = view.findViewById(R.id.switchEsVenenosoAnfibio);
                        
                        if (etTipoPiel != null) etTipoPiel.setText(anfibio.getTipoPiel());
                        if (etEtapaVida != null) etEtapaVida.setText(anfibio.getEtapaVida());
                        if (switchEsVenenosoAnfibio != null) switchEsVenenosoAnfibio.setChecked(anfibio.isEsVenenoso());
                    }
                    break;
                case "Peces":
                    if (animalToEdit instanceof Pez) {
                        Pez pez = (Pez) animalToEdit;
                        EditText etTipoAgua = view.findViewById(R.id.etTipoAgua);
                        EditText etColoracion = view.findViewById(R.id.etColoracion);
                        SwitchMaterial switchEsDepredador = view.findViewById(R.id.switchEsDepredador);
                        
                        if (etTipoAgua != null) etTipoAgua.setText(pez.getTipoAgua());
                        if (etColoracion != null) etColoracion.setText(pez.getColoracion());
                        if (switchEsDepredador != null) switchEsDepredador.setChecked(pez.isEsPredador());
                    }
                    break;
            }

            // Cargar valor del animal
            etValor.setText(String.format("%.2f", getArguments().getDouble("valor", 0.0)));
        }

        // Configurar el preview de imagen
        etImagenUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String url = s.toString().trim();
                if (!url.isEmpty() && getActivity() != null) {
                    try {
                        Glide.with(getActivity())
                            .load(url)
                            .placeholder(R.drawable.default_animal)
                            .error(R.drawable.default_animal)
                            .into(ivPreview);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ivPreview.setImageResource(R.drawable.default_animal);
                    }
                } else {
                    ivPreview.setImageResource(R.drawable.default_animal);
                }
            }
        });

        // Configurar el listener del switchEsRapaz
        if (switchEsRapaz != null) {
            switchEsRapaz.setOnCheckedChangeListener((buttonView, isChecked) -> {
                View dialogView = getDialog().getWindow().getDecorView();
                if (isChecked) {
                    dialogView.findViewById(R.id.layoutAveRapaz).setVisibility(View.VISIBLE);
                } else {
                    dialogView.findViewById(R.id.layoutAveRapaz).setVisibility(View.GONE);
                }
            });
        }

        builder.setView(view)
               .setTitle("Editar Animal")
               .setPositiveButton("Guardar", (dialog, id) -> guardarAnimal())
               .setNegativeButton("Cancelar", (dialog, id) -> dismiss());

        return builder.create();
    }

    private void guardarAnimal() {
        String nombre = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String categoria = spinnerCategoria.getText().toString();
        String imagenUrl = etImagenUrl.getText().toString().trim();
        
        View dialogView = getDialog().getWindow().getDecorView();
        EditText etEspecie = dialogView.findViewById(R.id.etEspecie);
        EditText etHabitat = dialogView.findViewById(R.id.etHabitat);
        
        if (etEspecie == null || etHabitat == null) {
            Toast.makeText(getContext(), "Error al obtener los campos del formulario", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String especie = etEspecie.getText().toString().trim();
        String habitat = etHabitat.getText().toString().trim();

        if (nombre.isEmpty() || descripcion.isEmpty() || especie.isEmpty() || habitat.isEmpty()) {
            Toast.makeText(getContext(), "Por favor complete todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Animal animalEditado = null;
        try {
            // Mantener el ID del animal original o generar uno nuevo si no existe
            String id = animalToEdit != null && animalToEdit.getId() != null ? 
                       animalToEdit.getId() : 
                       nombre.toLowerCase() + "-" + System.currentTimeMillis();

            switch (categoria) {
                case "Mamíferos":
                    EditText etTemperaturaCorporal = dialogView.findViewById(R.id.etTemperaturaCorporal);
                    EditText etTiempoGestacion = dialogView.findViewById(R.id.etTiempoGestacion);
                    EditText etTipoAlimentacion = dialogView.findViewById(R.id.etTipoAlimentacion);
                    SwitchMaterial switchEsNocturno = dialogView.findViewById(R.id.switchEsNocturno);
                    
                    double temperaturaCorporal = Double.parseDouble(etTemperaturaCorporal.getText().toString());
                    int tiempoGestacion = Integer.parseInt(etTiempoGestacion.getText().toString());
                    String tipoAlimentacion = etTipoAlimentacion.getText().toString();
                    
                    animalEditado = new Mamifero(nombre, especie, habitat, descripcion,
                            imagenUrl, categoria, temperaturaCorporal, tiempoGestacion, tipoAlimentacion);
                    
                    EditText etTipoPelaje = dialogView.findViewById(R.id.etTipoPelaje);
                    if (etTipoPelaje != null) {
                        ((Mamifero) animalEditado).setTipoPelaje(etTipoPelaje.getText().toString());
                    }
                    if (switchEsNocturno != null) {
                        ((Mamifero) animalEditado).setEsNocturno(switchEsNocturno.isChecked());
                    }
                    break;
                case "Aves":
                    EditText etEnvergaduraAlas = dialogView.findViewById(R.id.etEnvergaduraAlas);
                    EditText etColorPlumaje = dialogView.findViewById(R.id.etColorPlumaje);
                    EditText etTipoPico = dialogView.findViewById(R.id.etTipoPico);
                    
                    double envergaduraAlas = Double.parseDouble(etEnvergaduraAlas.getText().toString());
                    String colorPlumaje = etColorPlumaje.getText().toString();
                    String tipoPico = etTipoPico.getText().toString();
                    
                    if (switchEsRapaz != null && switchEsRapaz.isChecked()) {
                        double velocidadVuelo = Double.parseDouble(etVelocidadVuelo.getText().toString());
                        String tipoPresa = etTipoPresa.getText().toString();
                        
                        animalEditado = new AveRapaz(
                            nombre,
                            especie,
                            habitat,
                            descripcion,
                            imagenUrl,
                            categoria,
                            envergaduraAlas,
                            colorPlumaje,
                            tipoPico,
                            velocidadVuelo,
                            tipoPresa
                        );
                    } else {
                        animalEditado = new Ave(
                            nombre,
                            especie,
                            habitat,
                            descripcion,
                            imagenUrl,
                            categoria,
                            envergaduraAlas,
                            colorPlumaje,
                            tipoPico
                        );
                        
                        EditText etTipoVuelo = dialogView.findViewById(R.id.etTipoVuelo);
                        if (etTipoVuelo != null) {
                            ((Ave) animalEditado).setTipoVuelo(etTipoVuelo.getText().toString());
                        }
                        if (switchPuedeVolar != null) {
                            ((Ave) animalEditado).setPuedeVolar(switchPuedeVolar.isChecked());
                        }
                    }
                    break;
                case "Reptiles":
                    EditText etTipoEscamas = dialogView.findViewById(R.id.etTipoEscamas);
                    EditText etTipoReproduccion = dialogView.findViewById(R.id.etTipoReproduccion);
                    SwitchMaterial switchEsVenenosoReptil = dialogView.findViewById(R.id.switchEsVenenoso);
                    
                    String tipoEscamas = etTipoEscamas.getText().toString();
                    String tipoReproduccion = etTipoReproduccion.getText().toString();
                    boolean esVenenosoReptil = switchEsVenenosoReptil != null && switchEsVenenosoReptil.isChecked();
                    
                    animalEditado = new Reptil(nombre, especie, habitat, descripcion,
                            imagenUrl, categoria, tipoEscamas, tipoReproduccion, esVenenosoReptil);
                    break;
                case "Anfibios":
                    EditText etTipoPiel = dialogView.findViewById(R.id.etTipoPiel);
                    EditText etEtapaVida = dialogView.findViewById(R.id.etEtapaVida);
                    SwitchMaterial switchEsVenenosoAnfibio = dialogView.findViewById(R.id.switchEsVenenosoAnfibio);
                    
                    String tipoPiel = etTipoPiel.getText().toString();
                    String etapaVida = etEtapaVida.getText().toString();
                    boolean esVenenosoAnfibio = switchEsVenenosoAnfibio != null && switchEsVenenosoAnfibio.isChecked();
                    
                    animalEditado = new Anfibio(nombre, especie, habitat, descripcion,
                            imagenUrl, categoria, tipoPiel, esVenenosoAnfibio);
                    ((Anfibio) animalEditado).setEtapaVida(etapaVida);
                    break;
                case "Peces":
                    EditText etTipoAgua = dialogView.findViewById(R.id.etTipoAgua);
                    EditText etColoracion = dialogView.findViewById(R.id.etColoracion);
                    SwitchMaterial switchEsDepredador = dialogView.findViewById(R.id.switchEsDepredador);
                    
                    String tipoAgua = etTipoAgua.getText().toString();
                    String tipoAletas = etColoracion.getText().toString();
                    boolean esDepredador = switchEsDepredador != null && switchEsDepredador.isChecked();
                    
                    animalEditado = new Pez(nombre, especie, habitat, descripcion,
                            imagenUrl, categoria, tipoAgua, tipoAletas, esDepredador);
                    break;
            }

            if (animalEditado != null && listener != null) {
                // Establecer el ID del animal original
                animalEditado.setId(id);
                
                // Establecer el valor del animal
                if (etValor != null) {
                    try {
                        double valor = Double.parseDouble(etValor.getText().toString());
                        animalEditado.setValor(valor);
                    } catch (NumberFormatException e) {
                        // Si hay un error al parsear el valor, usar el valor predeterminado
                        animalEditado.calcularValor();
                    }
                }
                
                listener.onAnimalEdited(animalEditado);
                Toast.makeText(getContext(), "Animal actualizado correctamente", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error al guardar el animal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == -1 && data != null && data.getData() != null) {
            String imageUri = data.getData().toString();
            etImagenUrl.setVisibility(View.GONE);
            etImagenUrl.setText("");
            Glide.with(requireContext())
                .load(imageUri)
                .into(ivPreview);
            if (animalToEdit != null) {
                animalToEdit.setImagenUri(imageUri);
            }
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccionar Imagen"), PICK_IMAGE_REQUEST);
    }

    private void actualizarCamposEspecificos(View dialogView, String categoria) {
        // Ocultar todos los campos específicos primero
        dialogView.findViewById(R.id.layoutMamifero).setVisibility(View.GONE);
        dialogView.findViewById(R.id.layoutAve).setVisibility(View.GONE);
        dialogView.findViewById(R.id.layoutAveRapaz).setVisibility(View.GONE);
        dialogView.findViewById(R.id.layoutReptil).setVisibility(View.GONE);
        dialogView.findViewById(R.id.layoutAnfibio).setVisibility(View.GONE);
        dialogView.findViewById(R.id.layoutPez).setVisibility(View.GONE);

        // Mostrar los campos específicos según la categoría
        switch (categoria) {
            case "Mamíferos":
                dialogView.findViewById(R.id.layoutMamifero).setVisibility(View.VISIBLE);
                break;
            case "Aves":
                dialogView.findViewById(R.id.layoutAve).setVisibility(View.VISIBLE);
                if (switchEsRapaz != null && switchEsRapaz.isChecked()) {
                    dialogView.findViewById(R.id.layoutAveRapaz).setVisibility(View.VISIBLE);
                }
                break;
            case "Reptiles":
                dialogView.findViewById(R.id.layoutReptil).setVisibility(View.VISIBLE);
                break;
            case "Anfibios":
                dialogView.findViewById(R.id.layoutAnfibio).setVisibility(View.VISIBLE);
                break;
            case "Peces":
                dialogView.findViewById(R.id.layoutPez).setVisibility(View.VISIBLE);
                break;
        }
    }
} 