package com.infnetkot.segkotlinat.ui.detalhe

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders

import com.infnetkot.segkotlinat.R
import com.infnetkot.segkotlinat.util.AnotacaoStorage
import com.infnetkot.segkotlinat.util.Criptografador
import kotlinx.android.synthetic.main.fragment_detalhe.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader


class DetalheFragment : Fragment() {

    private lateinit var anotacaoStorage: AnotacaoStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detalhe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().let { act->
            anotacaoStorage = ViewModelProviders.of(act)
                .get(AnotacaoStorage::class.java) }

        titulo_detalhe.text = anotacaoStorage.anotacao!!.titulo
        data_detalhe.text = anotacaoStorage.anotacao!!.data
        texto_detalhe.text = anotacaoStorage.anotacao!!.texto

        getImage(anotacaoStorage.anotacao!!.titulo,anotacaoStorage.anotacao!!.data)
    }

    fun getImage(titulo : String, data : String){
        val encryptedIn: FileInputStream =
            Criptografador.getInstance().getEncFile("$titulo($data).fig", requireActivity()).openFileInput()
        val br = BufferedReader(InputStreamReader(encryptedIn))
        var conteudo = ""
        var bitmap = BitmapFactory.decodeByteArray(encryptedIn.readBytes(), 0,encryptedIn.readBytes().size)

        imagem_anotacao_detalhe.setImageBitmap(bitmap)
    }
}
