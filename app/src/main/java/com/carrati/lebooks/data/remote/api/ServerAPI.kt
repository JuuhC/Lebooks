package com.carrati.lebooks.data.remote.api

import android.os.AsyncTask
import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class ServerAPI(){

    private var response: JSONArray? = null

    fun getJSON(): JSONArray? {
        JsonTask().execute("https://raw.githubusercontent.com/Felcks/desafio-mobile-lemobs/master/products.json")
        return response
    }

    /*
     1- faz o download do json e joga pra dentro de um JSONArray
     2- percorre pegando os dados e criando objetos book
     3- joga os objetos dentro da lista usada pelo recyclerView
     4- notifica o adapter para recarregar a lista
    */
    private inner class JsonTask : AsyncTask<String, String, String>() {

        override fun doInBackground(vararg params: String): String? { //solicita o download do json

            var connection: HttpURLConnection? = null
            var reader: BufferedReader? = null

            try {
                val url = URL(params[0])
                connection = url.openConnection() as HttpURLConnection
                connection.connect()


                val stream = connection.inputStream

                reader = BufferedReader(InputStreamReader(stream))

                val buffer = StringBuilder()
                var line: String? = null

                while ({ line = reader.readLine(); line}() != null) {
                    buffer.append(line + "\n")
                }

                return buffer.toString()


            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
                try {
                    reader?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            return null
        }

        override fun onPostExecute(result: String) { //pega a resposta e faz o resto
            super.onPostExecute(result)

            if (result.isEmpty()) {
                //Toast.makeText(this@BookstoreActivity, "ERROR 1: Não foi possivel carregar a loja.", Toast.LENGTH_LONG).show()
                Log.e("ServerAPI", "ERROR 1: result from api response is empty")
                return
            } else {
                try {
                    response = JSONArray(result)
                } catch (e: JSONException) {
                    //Toast.makeText(this@BookstoreActivity, "ERROR 2: Não foi possivel carregar a loja.", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                    Log.e("ServerAPI","ERROR 2: problem to transform string result into JSONArray object")
                    return
                }
            }

        }
    }
}