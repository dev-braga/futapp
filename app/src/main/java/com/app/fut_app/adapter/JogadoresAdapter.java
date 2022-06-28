package com.app.fut_app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fut_app.R;
import com.app.fut_app.models.Jogador;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class JogadoresAdapter extends RecyclerView.Adapter<JogadoresAdapter.MyViewHolder> {

    private List<Jogador> listaJogadores;

    private DatabaseReference db = FirebaseDatabase.getInstance().getReference("Jogadores");


    public JogadoresAdapter(List<Jogador> jogadores) {
        this.listaJogadores = jogadores;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from( parent.getContext() )
                .inflate(R.layout.item_recycler_goleiro, parent, false);

        return new MyViewHolder( itemLista );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Jogador jogador = listaJogadores.get( position );

        holder.nome.setText( jogador.getNome() );

        /*
        if(jogador.getPosicao().contains("Goleiro")){
            holder.icGoleiro.setBackgroundResource(R.drawable.luvasgoleiro);
        }*/

        // Botão de excluir
        holder.btExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.child( jogador.getId() ).removeValue();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listaJogadores.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView nome;
        private ImageView icGoleiro;
        private ImageView btExcluir;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textViewNameGoleiro);
            icGoleiro = itemView.findViewById(R.id.icone_goleiro);
            btExcluir = itemView.findViewById(R.id.btn_excluir_goleiro);
        }
    }

}
