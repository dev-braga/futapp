package com.app.fut_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.fut_app.R;
import com.app.fut_app.databinding.ActivityResultBinding;
import com.app.fut_app.models.Jogador;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private ActivityResultBinding binding;
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference("Jogadores");

    private int quantJogadores;
    private List<String> time1 = new ArrayList<>();
    private List<String> time2 = new ArrayList<>();
    private List<String> time3 = new ArrayList<>();
    private List<String> time4 = new ArrayList<>();
    private List<String> nomes = new ArrayList<>();
    private List<String> naolistados = new ArrayList<>();

    Jogador jogador = new Jogador();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityResultBinding.inflate( getLayoutInflater() );
        View view = binding.getRoot();
        setContentView(view);



        btnBack();
        sortearTimes();
    }

    public void sortearTimes(){

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for ( DataSnapshot data : snapshot.getChildren() ){

                    Jogador jogador = data.getValue( Jogador.class );
                    jogador.setId( data.getKey() );
                    quantJogadores = jogador.quantJogadores;

                    nomes.add( jogador.getNome() + "\n" );

                }

                // *-----------------------------
                for(int i = 0; i < nomes.size(); i++) {

                    try {
                        while (time1.size() < quantJogadores) {
                            binding.titleTime1.setText("Time 1");
                            binding.cardTime1.setCardBackgroundColor(getResources().getColor(R.color.orange));
                            time1.add( sorteia() );
                        }
                        while (time2.size() < quantJogadores && quantJogadores != 0) {
                            binding.titleTime2.setText("Time 2");
                            time2.add( sorteia() );
                        }
                        while (time3.size() < quantJogadores && quantJogadores != 0) {
                            binding.titleTime3.setText("Time 3");
                            time3.add( sorteia() );
                        }
                        while (time4.size() < quantJogadores && quantJogadores != 0) {
                            binding.titleTime4.setText("Time 4");
                            time4.add( sorteia());
                        }
                        while (quantJogadores != 0) {
                            binding.titleJNaoSorteados.setText("Não sorteados");
                            naolistados.add( sorteia() );
                        }
                    } catch (Exception e) {
                        // System.out.println("Erro: " + e.getMessage());
                    }
                }
                    // Retirando caracteres especiais

                    binding.txtTeste.setText( time1.toString() );
                    binding.txtTeste1.setText( time2.toString() );
                    binding.txtTeste2.setText( time3.toString() );
                    binding.txtTeste3.setText( time4.toString() );

                    binding.txtJogNaoSorteados.setText( naolistados.toString() );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void btnBack() {

        binding.btnvoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), RandomActivity.class);
                startActivity(i);
            }
        });

    }

    public int randInt(int min, int max) {
        return (min + (int)(Math.random() * ((max - min) + 1)));
    }

    public String sorteia() {

        Collections.shuffle( nomes );
        int index = randInt(0, nomes.size() - 1);
        return nomes.remove(index);
    }

}