package br.com.syd.sharedpreferencessample

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.syd.sharedpreferencessample.Contants.CHAVE_TAREFAS
import br.com.syd.sharedpreferencessample.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class MainActivity : AppCompatActivity() {

    private lateinit var databind: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(databind.root)

        //NECESSÁRIO ALTERAR BUILD.GRADLE PARA ADICIONAR O SHAREDPREFERENCES

        val lTarefas = getTarefas() //recebe o array de taregas

        if (lTarefas.size > 0) {
            lTarefas.sortBy { it.prioridade }
            var tarefaText = ""
            for (tarefa: TarefaModel in lTarefas) {
                tarefaText += " " + tarefa.prioridade + " - " + tarefa.conteudo + "\n"
            }
            databind.Tarefas.text = tarefaText
        } else {
            databind.Tarefas.text = "nada para fazer"
        }

        databind.addTarefabutton.setOnClickListener {
            if (databind.editTextPrioridade.text.toString() != "") {
                val prioridade: Int = Integer.parseInt(databind.editTextPrioridade.text.toString())

                val conteudo = databind.editTextConteudo.text.toString()

                lTarefas.add(TarefaModel(prioridade, conteudo))

                SaveList(lTarefas)
            }
        }

    }

    fun getTarefas(): ArrayList<TarefaModel> { //retornar um arraylist de tarefas
        val sharedPref = this.getSharedPreferences(CHAVE_TAREFAS, Context.MODE_PRIVATE) //sharedPref pegou o contexto
        val gsonValue = sharedPref?.getString(CHAVE_TAREFAS, null) //faz get do objeto
        if (gsonValue != null) {
            val itemType: Type = object : TypeToken<ArrayList<TarefaModel>>() {}.type
            return Gson().fromJson(gsonValue, itemType) //converte de JSON para outro tipo de dado, no caso -> ArrayList de TarefaModel
        }
        return ArrayList()
    }

    private fun SaveList(favorites: ArrayList<TarefaModel>) {
        val sharedPreference =  getSharedPreferences(CHAVE_TAREFAS,Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        val json = Gson().toJson(favorites)
        editor.putString(CHAVE_TAREFAS,json) //converter de um objeto para JSON
        editor.apply()
    }
}