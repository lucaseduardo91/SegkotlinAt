package com.infnetkot.segkotlinat.ui.cadastro

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.graphics.drawable.toBitmap
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys

import com.infnetkot.segkotlinat.R
import com.infnetkot.segkotlinat.util.Criptografador
import kotlinx.android.synthetic.main.fragment_cadastro.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs


class CadastroFragment : Fragment() {

    private var TAKE_PICTURE = 1
    val REQUEST_PERMISSIONS_CODE = 128
    var dadosArq = ""
    var tituloArq = ""
    var dataArq = ""
    var conteudoArq = ""
    private lateinit var fotoArq : ByteArray


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

            tituloArq = titulo_cadastro.text.toString()
            dataArq = data_cadastro.text.toString()
            conteudoArq = texto_cadastro.text.toString()
            fotoArq = getByteArrayImage()

            if(tituloArq.isNullOrBlank() || dataArq.isNullOrBlank() || conteudoArq.isNullOrBlank())
                Toast.makeText(requireActivity(),"Preencha todos os campos!",Toast.LENGTH_SHORT).show()
            else{
                callAccessLocation(null)

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
        pw.print("Localização: ${dadosArq} | Descrição: ${conteudo}")
        pw.flush()
        encryptedOut.close()

    }

    fun gravarArquivoFoto(titulo : String, data : String, bytes : ByteArray){

        var arquivo = Criptografador.getInstance().getEncFile("$titulo($data).fig",requireActivity())
        val encryptedOut: FileOutputStream = arquivo.openFileOutput()

        encryptedOut.write(bytes)
        encryptedOut.close()

    }

    fun getByteArrayImage() : ByteArray{

        var bitmap = imagem_anotacao.drawable.toBitmap()
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

    private val locationListener: LocationListener =
        object : LocationListener {
            override fun onLocationChanged(location: Location) {
                dadosArq = "${convertToDms(location.latitude,location.longitude)}"

                gravarArquivoTxt(tituloArq,conteudoArq,dataArq)
                gravarArquivoFoto(tituloArq,dataArq,fotoArq)

                Toast.makeText(requireActivity(),"Arquivos gravados",Toast.LENGTH_SHORT).show()
            }
            override fun onStatusChanged(
                provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

    fun callAccessLocation(view: View?) {
        val permissionAFL = ContextCompat.checkSelfPermission(requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION)
        val permissionACL = ContextCompat.checkSelfPermission(requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION)
        if (permissionAFL != PackageManager.PERMISSION_GRANTED &&
            permissionACL != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                callDialog("É preciso liberar ACCESS_FINE_LOCATION",
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
            } else {
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSIONS_CODE)
            }
        } else {
            readMyCurrentCoordinates()
        }
    }

    private fun callDialog(mensagem: String,
                           permissions: Array<String>) {
        var mDialog = AlertDialog.Builder(requireActivity())
            .setTitle("Permissão")
            .setMessage(mensagem)
            .setPositiveButton("Ok")
            { dialog, id ->
                ActivityCompat.requestPermissions(
                    requireActivity(), permissions,
                    REQUEST_PERMISSIONS_CODE)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel")
            { dialog, id ->
                dialog.dismiss()
            }
        mDialog.show()
    }

    private fun readMyCurrentCoordinates() {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = locationManager.isProviderEnabled(
            LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
        if (!isGPSEnabled && !isNetworkEnabled) {
            Log.d("Permissao", "Ative os serviços necessários")
        } else {
            if (isGPSEnabled) {
                try {
                    locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,locationListener,null)
                } catch(ex: SecurityException) {
                    Log.d("Permissao", "Security Exception")
                }
            }
            else if (isNetworkEnabled) {
                try {
                    locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,locationListener,null)
                } catch(ex: SecurityException) {
                    Log.d("Permissao", "Security Exception")
                }
            }
        }
    }

    private fun convertToDms(latitude: Double, longitude: Double): String? {
        val builder = java.lang.StringBuilder()
        if (latitude < 0) {
            builder.append("S ")
        } else {
            builder.append("N ")
        }
        val latitudeDegrees = Location.convert(
            abs(latitude),
            Location.FORMAT_SECONDS
        )
        val latitudeSplit = latitudeDegrees.split(":").toTypedArray()
        builder.append(latitudeSplit[0])
        builder.append("°")
        builder.append(latitudeSplit[1])
        builder.append("'")
        builder.append(latitudeSplit[2])
        builder.append("\"")
        builder.append(" ")
        if (longitude < 0) {
            builder.append("W ")
        } else {
            builder.append("E ")
        }
        val longitudeDegrees = Location.convert(
            abs(longitude),
            Location.FORMAT_SECONDS
        )
        val longitudeSplit = longitudeDegrees.split(":").toTypedArray()
        builder.append(longitudeSplit[0])
        builder.append("°")
        builder.append(longitudeSplit[1])
        builder.append("'")
        builder.append(longitudeSplit[2])
        builder.append("\"")
        return builder.toString()
    }

}
