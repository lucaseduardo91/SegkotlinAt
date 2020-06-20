package com.infnetkot.segkotlinat.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.infnetkot.segkotlinat.R
import com.infnetkot.segkotlinat.adapter.AnotacaoAdapter
import com.infnetkot.segkotlinat.model.Anotacao
import com.infnetkot.segkotlinat.util.AnotacaoStorage
import com.infnetkot.segkotlinat.util.Criptografador
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.*

class HomeFragment : Fragment() {

    private lateinit var anotacaoStorage: AnotacaoStorage
    lateinit var mAdView : AdView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().let { act->
            anotacaoStorage = ViewModelProviders.of(act)
                .get(AnotacaoStorage::class.java) }

        // Pega o viewmodel de compra e verifica se o status está como comprado.
        // Se não estiver, executa o código abaixo das propagandas
        // Se estiver, usa o atributo visibility do banner e altera para View.GONE

        MobileAds.initialize(requireActivity()) {}

        mAdView = requireActivity().findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        carregarLista()
    }

    fun carregarLista(){

        var lista = buscarArquivos()

        if(!lista.isNullOrEmpty())
        {
            listagem_anotacoes.layoutManager = LinearLayoutManager(this.context)
            listagem_anotacoes.adapter = AnotacaoAdapter(lista,requireActivity(),anotacaoStorage)
        }
        else{
            listagem_anotacoes.visibility = View.GONE
            empty_view_anotacoes.visibility = View.VISIBLE
        }
    }

    fun buscarArquivos() : MutableList<Anotacao>?{

        var path = requireActivity().applicationContext.filesDir
        var dir = File(path, "/ARQSLOC")
        if (!dir.exists() || !dir.isDirectory) {
            val arqLoqDirectory = File(path, "ARQSLOC")
            arqLoqDirectory.mkdirs()
            dir = File(path, "/ARQSLOC")
        }

        var arquivos = dir.listFiles()
        var titulo = ""
        var data = ""
        var lista = mutableListOf<Anotacao>()

        var listaArq = filtrarArquivos(arquivos)

        if(listaArq.isNullOrEmpty())
            return null

        for(arquivo in listaArq)
        {
            titulo = obterTitulo(arquivo)
            data = obterData(arquivo)
            lista.add(lerArquivo(arquivo,titulo,data))
        }

        return lista;
    }

    fun filtrarArquivos(listaArq : Array<File>?) : MutableList<String>{

        var lista = mutableListOf<String>()

        if(listaArq != null)
        {
            for(arq in listaArq)
            {
                if(arq.name.contains(".txt"))
                    lista.add(arq.name)
            }
        }
        return lista
    }

    fun lerArquivo(nomeArquivo : String, titulo : String, data : String) : Anotacao{
        val encryptedIn: FileInputStream =
            Criptografador.getInstance().getEncFile(nomeArquivo, requireActivity()).openFileInput()
        val br = BufferedReader(InputStreamReader(encryptedIn))
        var conteudo = ""
        br.lines().forEach{
                t -> conteudo = t
        }
        var anotacao = Anotacao(titulo,conteudo,data)
        encryptedIn.close()
        return anotacao
    }

    fun obterTitulo(texto : String) : String{
        var partes = texto.split("(")

        return partes[0]
    }

    fun obterData(texto : String) : String{
        var partes = texto.split("(")
        var parte = partes[1].replace(")","")

        var data = parte.replace(".txt","")

        return data
    }
}
