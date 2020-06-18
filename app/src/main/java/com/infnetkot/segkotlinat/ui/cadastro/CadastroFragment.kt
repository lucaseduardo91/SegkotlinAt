package com.infnetkot.segkotlinat.ui.cadastro

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys

import com.infnetkot.segkotlinat.R
import com.infnetkot.segkotlinat.util.Criptografador
import kotlinx.android.synthetic.main.fragment_cadastro.*
import java.io.*


class CadastroFragment : Fragment() {

    private var TAKE_PICTURE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cadastro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_cadastrar.setOnClickListener {
            var foto = getByteArrayImage()
            var titulo = titulo_cadastro.text
            var data = data_cadastro.text
            var conteudo = texto_cadastro.text

            if(titulo.isNullOrBlank() || data.isNullOrBlank() || conteudo.isNullOrBlank())
                Toast.makeText(requireActivity(),"Preencha todos os campos!",Toast.LENGTH_SHORT).show()
            else{
                gravarArquivoTxt(titulo.toString(),conteudo.toString(),data.toString())
                gravarArquivoFoto(titulo.toString(),data.toString(),foto)

                Toast.makeText(requireActivity(),"Arquivos gravados",Toast.LENGTH_SHORT).show()
            }
        }

        btn_tirar_foto.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                    startActivityForResult(takePictureIntent, TAKE_PICTURE)
                }
            }
        }
    }

    fun gravarArquivoTxt(titulo : String, conteudo : String, data : String){

        var arquivo = Criptografador.getInstance().getEncFile("$titulo($data).txt",requireActivity())
        val encryptedOut: FileOutputStream = arquivo.openFileOutput()

        val pw = PrintWriter(encryptedOut)
        pw.print(conteudo)
        pw.flush()
        encryptedOut.close()

    }

    fun gravarArquivoFoto(titulo : String, data : String, bytes : ByteArray){

        var arquivo = Criptografador.getInstance().getEncFile("$titulo($data).fig",requireActivity())
        val encryptedOut: FileOutputStream = arquivo.openFileOutput()

        val pw = PrintWriter(encryptedOut)
        pw.print(bytes)
        pw.flush()
        encryptedOut.close()

    }

    fun getByteArrayImage() : ByteArray{
        var bitmap = (imagem_anotacao.drawable as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val image = stream.toByteArray()

        stream.close()
        return image
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            imagem_anotacao.setImageBitmap(imageBitmap)
        }

    }

}
