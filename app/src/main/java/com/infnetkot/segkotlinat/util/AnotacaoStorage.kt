package com.infnetkot.segkotlinat.util

import androidx.lifecycle.ViewModel
import com.infnetkot.segkotlinat.model.Anotacao

class AnotacaoStorage : ViewModel(){
    var anotacao : Anotacao? = null

    fun adicionaAnotacao(novaAnotacao : Anotacao) {
        anotacao = novaAnotacao
    }
}