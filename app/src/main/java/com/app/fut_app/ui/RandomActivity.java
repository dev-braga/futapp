package com.app.fut_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.app.fut_app.R;
import com.app.fut_app.adapter.JogadoresAdapter;
import com.app.fut_app.databinding.ActivityRandomBinding;
import com.app.fut_app.models.Jogador;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RandomActivity extends AppCompatActivity {

    private ActivityRandomBinding binding;
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference("Jogadores");
    private JogadoresAdapter adapter;
    private ValueEventListener valueEventListener;

    // Dados RadioButton
    private String radioResult = "";
    // Dados Checkbox
    private boolean checkBoxChegouCedoResult;
    private boolean checkBoxVisitanteResult;
    private String validaCampo;
    private int validaMinTime = 4;


    // Lista de Jogadores
    private List<Jogador> jogadores = new ArrayList<>();
    private Jogador jogador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRandomBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        initMethods();
        initRecyclerView();

    }

    private void initRecyclerView() {

        adapter = new JogadoresAdapter( jogadores );
        binding.recyclerViewNames.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        binding.recyclerViewNames.setAdapter( adapter );
        addJogador(); // Adiciona os jogadores
    }

    private void initMethods() {
        //  Inicializando valores default
        verifRadioButton();
        cleanComponents();
        inserirQuantJog();
        sortTeams();
        btnBack();

    }

    private void addJogador() {

           binding.btAdd.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   String nome = binding.editTextInserir.getText().toString();

                   jogador = new Jogador(nome, radioResult);
                   verfiCheckBox();

                   if(!nome.isEmpty()){

                        // Verificando se o radiobutton não está marcado
                       if (radioResult != "" && radioResult != null ){

                           jogador.quantJogadores = validaMinTime;
                           jogador.chegada = checkBoxChegouCedoResult;
                           jogador.situacao = checkBoxVisitanteResult;

                           db.push().setValue(  jogador );
                           cleanComponents();
                           carregarDados();

                       }else{
                           initSnackbar("Escolha uma posição");
                       }

                   }else{
                       initSnackbar("Insira um nome");
                   }

               }
           });

    }

    private void carregarDados(){

      valueEventListener = db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                jogadores.clear();
                radioResult = null; // Reseta o valor dos radiobuttons

                for ( DataSnapshot data : snapshot.getChildren() ){

                    Jogador jogador = data.getValue( Jogador.class );
                    jogador.setId( data.getKey() );

                    jogadores.add( jogador );
                }
                    
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                initSnackbar( error.getMessage() );
            }
        });

    }

    private void cleanComponents() {

        binding.radioGoleiro.setChecked(false);
        binding.radioZagueiro.setChecked(false);
        binding.radioMeia.setChecked(false);
        binding.radioAtacante.setChecked(false);
        binding.ckbVisitante.setChecked(false);
        binding.ckbChegouCedo.setChecked(false);
        binding.ckbVisitante.setChecked(false);
        binding.txtNumbJogValue.setText("4");
        binding.editTextInserir.setText("");
        checkBoxChegouCedoResult = false;
        checkBoxVisitanteResult = false;
    }

    private void verifRadioButton(){

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch ( checkedId ){
                    case R.id.radioGoleiro:
                        radioResult = "Goleiro";
                        break;
                    case R.id.radioZagueiro:
                        radioResult = "Zagueiro";
                        break;
                    case R.id.radioMeia:
                        radioResult = "Meia";
                        break;
                    case R.id.radioAtacante:
                        radioResult = "Atacante";
                        break;

                }
            }
        });

    }

    private void verfiCheckBox(){

        if(binding.ckbChegouCedo.isChecked() ){
            checkBoxChegouCedoResult = true;
        }
        if(binding.ckbVisitante.isChecked()){
            checkBoxVisitanteResult = true;
        }

    }

    private void btnBack() {

        binding.btnvoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

            }
        });
    }


    private void initSnackbar(String msg){

        Snackbar.make(binding.getRoot(), msg, Snackbar.LENGTH_SHORT).show();

    }

    private void sortTeams(){

        binding.btnSortear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), ResultActivity.class);
                startActivity(i);
            }
        });
    }

    private void inserirQuantJog(){

        binding.txtNumbJogValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir o alertDialog
                alertDialog("Teste");
            }
        });
    }

    private void alertDialog(String msg){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        AlertDialog dialog;

        // Inserir o layout
        View customLayout = getLayoutInflater().inflate(R.layout.alert_dialog_times, null);
        alertDialog.setCancelable(false);

        MaterialButton btOk = customLayout.findViewById(R.id.btInserirNumbJogadores);
        EditText editText = customLayout.findViewById(R.id.editTextNumbJogadores);
        ImageView btClose = customLayout.findViewById(R.id.bt_close);

        alertDialog.setView( customLayout );
        // Crir o alerta e mostrar
        dialog = alertDialog.create();

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validaCampo = editText.getText().toString();

                if( !validaCampo.isEmpty()){

                    validaMinTime = Integer.parseInt( validaCampo );
                    // Validar a quantidade mínima de jogadores
                    if( validaMinTime >= 4 ){
                        // Validar a quantidade máxima de jogadores
                        if (validaMinTime <= 11 ){

                            binding.txtNumbJogValue.setText( String.valueOf(validaMinTime) );
                            dialog.dismiss();

                        } else{
                            Toast.makeText(getApplicationContext(), "Máximo 11 jogadores permitidos", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "É necessário no mínimo 4 jogadores", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Preencha um valor.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        carregarDados();
    }

    @Override
    protected void onStop() {
        super.onStop();

        db.removeEventListener( valueEventListener );
    }
}