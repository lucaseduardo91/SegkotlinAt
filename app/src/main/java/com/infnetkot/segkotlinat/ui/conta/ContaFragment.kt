package com.infnetkot.segkotlinat.ui.conta

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.infnetkot.segkotlinat.MainActivity
import com.infnetkot.segkotlinat.MenuActivity

import com.infnetkot.segkotlinat.R
import kotlinx.android.synthetic.main.fragment_conta.*


class ContaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conta, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var auth = FirebaseAuth.getInstance()

        email_conta.text = auth.currentUser!!.email

        btn_logoff.setOnClickListener {
            auth.signOut()
            var intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }

        // Verificaria o valor do viewmodel de compra onde foi guardada a informação se o usuário realizou a compra
        // Em caso afirmativo, esconderia o botão com: btn_premium.visibility = View.GONE
        // Em caso negativo, criaria o listener abaixo que chama o processo de compra e atualiza o viewmodel no caso de compra concluída

        btn_premium.setOnClickListener {
            var menuAct = requireActivity() as MenuActivity
            Toast.makeText(menuAct,"Tratamento de compras comentado no código do app", Toast.LENGTH_SHORT).show()

            // menuAct.processGoogleInApp()
        }
    }

}
