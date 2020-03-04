package com.carrati.lebooks.data

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(private val context: Context) {
    private val preferences: SharedPreferences
    private val editor: SharedPreferences.Editor

    private val NOME_ARQUIVO = "user.preferences"
    private val CHAVE_NOME = "nome"
    private val CHAVE_SALDO = "saldo"

    var saldo: Int
        get() = preferences.getInt(CHAVE_SALDO, 100)
        set(saldo) {
            editor.putInt(CHAVE_SALDO, saldo)
            editor.commit()
        }

    var nome: String
        get() = preferences.getString(CHAVE_NOME, "altere o nome no menu acima.")!!
        set(nome) {
            editor.putString(CHAVE_NOME, nome)
            editor.commit()
        }

    init {
        preferences = context.getSharedPreferences(NOME_ARQUIVO, 0)
        editor = preferences.edit()
    }

    fun cleanPreferences() {
        editor.clear()
        editor.commit()
    }
}
