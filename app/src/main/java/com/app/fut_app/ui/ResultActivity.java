package com.app.fut_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Collections;
import java.util.List;

public class ResultActivity extends AppCompatActivity {

    private ActivityResultBinding binding;
    private DatabaseReference db = FirebaseDatabase.getInstance().getReference("Jogadores");

    private int quantJogadores;
    private boolean chegouCedo;
    private String posicaoJogador;

    private List<String> time1 = new ArrayList<>();
    private List<String> time2 = new ArrayList<>();
    private List<String> time3 = new ArrayList<>();
    private List<String> time4 = new ArrayList<>();
    private List<String> nomes = new ArrayList<>();
    private List<String> naolistados = new ArrayList<>();
    // Arrays de posicao de jogadores
    private List<String> posGoleiro = new ArrayList<>();
    private List<String> posZagueiro = new ArrayList<>();
    private List<String> posMeia = new ArrayList<>();
    private List<String> posAtacante = new ArrayList<>();

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

                    chegouCedo = jogador.chegada;
                    quantJogadores = jogador.quantJogadores;
                    posicaoJogador = jogador.getPosicao();

                    nomes.add( jogador.getNome() + "\n" );

                   // Log.i("Posicao: ", posicaoJogador );

                    switch ( posicaoJogador ){
                        case "Goleiro":
                            posGoleiro.add( jogador.getPosicao() );
                            break;
                        case "Zagueiro":
                            posZagueiro.add( jogador.getPosicao() );
                            break;
                        case "Meia":
                            posMeia.add( jogador.getPosicao() );
                            break;
                        case "Atacante":
                            posAtacante.add( jogador.getPosicao() );
                            break;
                    }
                }

                // *-----------------------------
                for( int i = 0; i < nomes.size(); i++ ) {

                    Log.i("asdqwe", posGoleiro.toString() );

                    try {
                        while (time1.size() < quantJogadores) {

                               binding.titleTime1.setText("Time 1");
                               binding.titleTime1.setTextColor(getResources().getColor(R.color.orange));
                               time1.add( sorteia() );

                        }
                        while (time2.size() < quantJogadores && quantJogadores != 0 ) {
                            binding.titleTime2.setText("Time 2");
                            binding.titleTime2.setTextColor(getResources().getColor(R.color.purple));
                            time2.add( sorteia() );

                        }
                        while (time3.size() < quantJogadores && quantJogadores != 0 ) {
                            binding.titleTime3.setText("Time 3");
                            binding.titleTime3.setTextColor(getResources().getColor(R.color.green));
                            time3.add( sorteia() );

                        }
                        while (time4.size() < quantJogadores && quantJogadores != 0 ) {
                            binding.titleTime4.setText("Time 4");
                            binding.titleTime4.setTextColor(getResources().getColor(R.color.red));
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

                    // Tratando os dados - Retirando os caracteres Especiais
                    String textTime1 = time1.toString().replace("[", "")
                            .replace("]", "").replace("," ,"")
                                    .replace(" ","");
                    String textTime2 = time2.toString().replace("[", "")
                            .replace("]", "").replace("," ,"")
                            .replace(" ","");
                    String textTime3 = time3.toString().replace("[", "")
                            .replace("]", "").replace("," ,"")
                            .replace(" ","");
                    String textTime4 = time4.toString().replace("[", "")
                            .replace("]", "").replace("," ,"")
                            .replace(" ","");
                    String textNaoListado = naolistados.toString().replace("[", "")
                            .replace("]", "").replace("," ,"")
                            .replace(" ","");

                    // Fim tratamento das Strings ----------------------------------

                    binding.txtTeste.setText( textTime1 );
                    binding.txtTeste1.setText( textTime2 );
                    binding.txtTeste2.setText( textTime3 );
                    binding.txtTeste3.setText( textTime4 );


                    binding.txtJogNaoSorteados.setText( textNaoListado );

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
