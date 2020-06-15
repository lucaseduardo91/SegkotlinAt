package com.infnetkot.segkotlinat.ui.conta

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.infnetkot.segkotlinat.MainActivity

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
    }

}
