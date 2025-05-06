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
import android.content.Context;

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
        View view = inflater.inflate(R.layout.dialog_edit_animal, null);

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

        // Configurar el Spinner con los mismos nombres que las pestañas
        String[] categorias = {"Mamíferos", "Aves", "Reptiles", "Anfibios", "Peces"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);
        // Forzar el despliegue del menú al hacer clic
        spinnerCategoria.setInputType(android.text.InputType.TYPE_NULL);
        spinnerCategoria.setKeyListener(null);
        spinnerCategoria.setOnClickListener(v -> spinnerCategoria.showDropDown());
        // Obtener referencia al TextInputLayout de categoría
        final com.google.android.material.textfield.TextInputLayout textInputLayoutCategoria = view.findViewById(R.id.spinnerCategoria).getParent() instanceof com.google.android.material.textfield.TextInputLayout ? (com.google.android.material.textfield.TextInputLayout) view.findViewById(R.id.spinnerCategoria).getParent() : null;
        // Solo aquí asignar la categoría actual y forzar hint flotante
        if (animalToEdit != null) {
            spinnerCategoria.setText(animalToEdit.getCategoria(), false);
            if (textInputLayoutCategoria != null) {
                textInputLayoutCategoria.setHintEnabled(true);
                textInputLayoutCategoria.setHintAnimationEnabled(true);
            }
            spinnerCategoria.clearFocus();
            spinnerCategoria.requestFocus();
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
               .setTitle("Editar Animal");

        AlertDialog dialog = builder.create();

        // Configurar listeners para los botones personalizados
        Button btnGuardar = view.findViewById(R.id.btnGuardar);
        Button btnCancelar = view.findViewById(R.id.btnCancelar);
        btnGuardar.setOnClickListener(v -> {
            guardarAnimal();
        });
        btnCancelar.setOnClickListener(v -> {
            dialog.dismiss();
        });

        return dialog;
    }

    private void guardarAnimal() {
        String nombre = etNombre.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();
        String categoria = spinnerCategoria.getText() != null ? spinnerCategoria.getText().toString().trim() : "";
        // No modificar el valor, usarlo tal cual para que coincida con las pestañas
        String imagenUrl = etImagenUrl.getText().toString().trim();
        String especie = etEspecie.getText().toString().trim();
        String habitat = etHabitat.getText().toString().trim();
        double valor = 0.0;
        try {
            valor = Double.parseDouble(etValor.getText().toString().trim());
        } catch (Exception ignored) {}
        if (nombre.isEmpty() || descripcion.isEmpty() || especie.isEmpty() || habitat.isEmpty()) {
            Toast.makeText(getContext(), "Por favor complete todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }
        // Actualizar campos básicos
        animalToEdit.setNombre(nombre);
        animalToEdit.setDescripcion(descripcion);
        animalToEdit.setCategoria(categoria);
        animalToEdit.setImagenUri(imagenUrl);
        animalToEdit.setEspecie(especie);
        animalToEdit.setHabitat(habitat);
        animalToEdit.setValor(valor);

        // Actualizar campos específicos si existen
        if (animalToEdit instanceof Mamifero) {
            Mamifero m = (Mamifero) animalToEdit;
            if (etTemperaturaCorporal != null) {
                try { m.setTemperaturaCorporal(Double.parseDouble(etTemperaturaCorporal.getText().toString())); } catch (Exception ignored) {}
            }
            if (etTiempoGestacion != null) {
                try { m.setTiempoGestacion(Integer.parseInt(etTiempoGestacion.getText().toString())); } catch (Exception ignored) {}
            }
            if (etTipoAlimentacion != null) m.setTipoAlimentacion(etTipoAlimentacion.getText().toString());
            EditText etTipoPelaje = getDialog().findViewById(R.id.etTipoPelaje);
            if (etTipoPelaje != null) m.setTipoPelaje(etTipoPelaje.getText().toString());
            SwitchMaterial switchEsNocturno = getDialog().findViewById(R.id.switchEsNocturno);
            if (switchEsNocturno != null) m.setEsNocturno(switchEsNocturno.isChecked());
        } else if (animalToEdit instanceof AveRapaz) {
            AveRapaz ar = (AveRapaz) animalToEdit;
            if (etEnvergaduraAlas != null) {
                try { ar.setEnvergaduraAlas(Double.parseDouble(etEnvergaduraAlas.getText().toString())); } catch (Exception ignored) {}
            }
            if (etColorPlumaje != null) ar.setColorPlumaje(etColorPlumaje.getText().toString());
            EditText etTipoPico = getDialog().findViewById(R.id.etTipoPico);
            if (etTipoPico != null) ar.setTipoPico(etTipoPico.getText().toString());
            if (etVelocidadVuelo != null) {
                try { ar.setVelocidadVuelo(Double.parseDouble(etVelocidadVuelo.getText().toString())); } catch (Exception ignored) {}
            }
            if (etTipoPresa != null) ar.setTipoPresa(etTipoPresa.getText().toString());
        } else if (animalToEdit instanceof Ave) {
            Ave a = (Ave) animalToEdit;
            if (etEnvergaduraAlas != null) {
                try { a.setEnvergaduraAlas(Double.parseDouble(etEnvergaduraAlas.getText().toString())); } catch (Exception ignored) {}
            }
            if (etColorPlumaje != null) a.setColorPlumaje(etColorPlumaje.getText().toString());
            EditText etTipoPico = getDialog().findViewById(R.id.etTipoPico);
            if (etTipoPico != null) a.setTipoPico(etTipoPico.getText().toString());
            EditText etTipoVuelo = getDialog().findViewById(R.id.etTipoVuelo);
            if (etTipoVuelo != null) a.setTipoVuelo(etTipoVuelo.getText().toString());
            SwitchMaterial switchPuedeVolar = getDialog().findViewById(R.id.switchPuedeVolar);
            if (switchPuedeVolar != null) a.setPuedeVolar(switchPuedeVolar.isChecked());
        } else if (animalToEdit instanceof Reptil) {
            Reptil r = (Reptil) animalToEdit;
            EditText etTipoEscamas = getDialog().findViewById(R.id.etTipoEscamas);
            if (etTipoEscamas != null) r.setTipoEscamas(etTipoEscamas.getText().toString());
            EditText etTipoReproduccion = getDialog().findViewById(R.id.etTipoReproduccion);
            if (etTipoReproduccion != null) r.setTipoReproduccion(etTipoReproduccion.getText().toString());
            SwitchMaterial switchEsVenenoso = getDialog().findViewById(R.id.switchEsVenenoso);
            if (switchEsVenenoso != null) r.setEsVenenoso(switchEsVenenoso.isChecked());
        } else if (animalToEdit instanceof Anfibio) {
            Anfibio an = (Anfibio) animalToEdit;
            EditText etTipoPiel = getDialog().findViewById(R.id.etTipoPiel);
            if (etTipoPiel != null) an.setTipoPiel(etTipoPiel.getText().toString());
            EditText etEtapaVida = getDialog().findViewById(R.id.etEtapaVida);
            if (etEtapaVida != null) an.setEtapaVida(etEtapaVida.getText().toString());
            SwitchMaterial switchEsVenenosoAnfibio = getDialog().findViewById(R.id.switchEsVenenosoAnfibio);
            if (switchEsVenenosoAnfibio != null) an.setEsVenenoso(switchEsVenenosoAnfibio.isChecked());
        } else if (animalToEdit instanceof Pez) {
            Pez p = (Pez) animalToEdit;
            EditText etTipoAgua = getDialog().findViewById(R.id.etTipoAgua);
            if (etTipoAgua != null) p.setTipoAgua(etTipoAgua.getText().toString());
            EditText etColoracion = getDialog().findViewById(R.id.etColoracion);
            if (etColoracion != null) p.setColoracion(etColoracion.getText().toString());
            SwitchMaterial switchEsDepredador = getDialog().findViewById(R.id.switchEsDepredador);
            if (switchEsDepredador != null) p.setEsPredador(switchEsDepredador.isChecked());
        }

        // Notificar cambios
        if (listener != null) {
            listener.onAnimalEdited(animalToEdit);
        }
        Toast.makeText(getContext(), "Animal actualizado correctamente", Toast.LENGTH_SHORT).show();
        dismiss();
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (listener == null && getParentFragment() instanceof OnAnimalEditedListener) {
            listener = (OnAnimalEditedListener) getParentFragment();
        } else if (listener == null && context instanceof OnAnimalEditedListener) {
            listener = (OnAnimalEditedListener) context;
        }
    }
} 