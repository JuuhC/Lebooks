package com.carrati.lebooks.Config;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private final String NOME_ARQUIVO = "user.preferences";
    private final String CHAVE_NOME = "nome";
    private final String CHAVE_SALDO = "saldo";

    public UserPreferences(Context c) {
        this.context = c;
        preferences = context.getSharedPreferences(NOME_ARQUIVO,0);
        editor = preferences.edit();
    }

    public void cleanPreferences(){
        editor.clear();
        editor.commit();
    }

    public void setSaldo(String saldo){
        editor.putString(CHAVE_SALDO, saldo );
        editor.commit();
    }

    public void setNome(String nome){
        editor.putString(CHAVE_NOME, nome );
        editor.commit();
    }

    public String getSaldo(){
        return preferences.getString(CHAVE_SALDO, "100");
    }

    public String getNome() { return preferences.getString(CHAVE_NOME, "altere o nome no menu acima."); }
}
