package com.infnetkot.segkotlinat.ui.detalhe

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.infnetkot.segkotlinat.R
import com.infnetkot.segkotlinat.util.AnotacaoStorage
import com.infnetkot.segkotlinat.util.Criptografador
import kotlinx.android.synthetic.main.fragment_detalhe.*
import java.io.FileInputStream


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

        var bytes = encryptedIn.readBytes()

        var bitmap = BitmapFactory.decodeByteArray(bytes, 0,bytes.size)

        imagem_anotacao_detalhe.setImageBitmap(bitmap)
    }
}
