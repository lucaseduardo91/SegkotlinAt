package com.infnetkot.segkotlinat.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.infnetkot.segkotlinat.MainActivity
import com.infnetkot.segkotlinat.R
import com.infnetkot.segkotlinat.model.Anotacao
import com.infnetkot.segkotlinat.util.AnotacaoStorage

class AnotacaoAdapter (anotacoes : List<Anotacao>, activity: Activity, anotacaoStorage: AnotacaoStorage) :
    RecyclerView.Adapter<AnotacaoAdapter.AnotacaoViewHolder>(){
    var listaAnotacoes = anotacoes
    var activity = activity
    var anotacaoStorage = anotacaoStorage

    class AnotacaoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var titulo = itemView.findViewById<TextView>(R.id.titulo_lista)
        var data = itemView.findViewById<TextView>(R.id.data_lista)
        var btn = itemView.findViewById<ImageButton>(R.id.info_anotacao)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnotacaoViewHolder {
        val card = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_anotacao, parent, false)

        return AnotacaoViewHolder(card)
    }

    override fun getItemCount() = listaAnotacoes.size

    override fun onBindViewHolder(holder: AnotacaoViewHolder, position: Int) {

        holder.titulo.text = listaAnotacoes[position].titulo
        holder.data.text = listaAnotacoes[position].data
        holder.btn.setOnClickListener {
            anotacaoStorage.adicionaAnotacao(listaAnotacoes[position])
            activity.findNavController(R.id.nav_host_fragment).navigate(R.id.nav_detalhe)
        }
    }

}