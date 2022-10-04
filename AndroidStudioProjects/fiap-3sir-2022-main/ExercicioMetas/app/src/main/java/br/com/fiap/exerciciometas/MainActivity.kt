package br.com.fiap.exerciciometas

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.fiap.exerciciometas.Constants.IS_FIRST_TIME
import br.com.fiap.exerciciometas.Constants.META_PREFERENCES
import br.com.fiap.exerciciometas.Constants.META_RESULT
import br.com.fiap.exerciciometas.Constants.META_STORE
import br.com.fiap.exerciciometas.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class MainActivity : AppCompatActivity(), IUpdateMeta {
    private lateinit var databind: ActivityMainBinding
    val metaAdapter = MetasAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(databind.root)

        if (isFirstTime()) {
            callWelcomeDialog()
            savefirstTime()
        }

        setupReclycler()
        metaAdapter.setList(getMetas())

        databind.adicionarMetaBtn.setOnClickListener {
            addMetaRegister.launch(Intent(this, AdicionarMetaActivity::class.java))
        }
    }

    private fun setupReclycler() {
        databind.listaMetasView.layoutManager = LinearLayoutManager(this)
        databind.listaMetasView.adapter = metaAdapter
    }

    fun getMetas(): ArrayList<MetasModel> {

        val sharedPref = this.getSharedPreferences(META_PREFERENCES, Context.MODE_PRIVATE)
        val gsonValue = sharedPref?.getString(META_STORE, null)
        if (gsonValue != null) {
            val itemType: Type = object : TypeToken<ArrayList<MetasModel>>() {}.type
            return Gson().fromJson(gsonValue, itemType)
        }
        return ArrayList()
    }

    fun isFirstTime(): Boolean {

        val sharedPref = this.getSharedPreferences(META_PREFERENCES, Context.MODE_PRIVATE)
        val isFirstTime = sharedPref?.getBoolean(IS_FIRST_TIME, true)
        return isFirstTime ?: true
    }

    private fun savefirstTime() {
        val sharedPreference = getSharedPreferences(META_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putBoolean(IS_FIRST_TIME, false)
        editor.apply()
    }

    private fun saveMetasList(metas: ArrayList<MetasModel>) {
        val sharedPreference = getSharedPreferences(META_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        val json = Gson().toJson(metas)
        editor.putString(META_STORE, json)
        editor.apply()
    }

    private fun callWelcomeDialog() {

        val confirmDialog = AlertDialog.Builder(this)
        confirmDialog.setTitle("Bem Vindo!")
        confirmDialog.setMessage("Gostaria de já cadastrar sua primeira meta ?")
        confirmDialog.setPositiveButton("Sim") { _, _ ->
            addMetaRegister.launch(Intent(this, AdicionarMetaActivity::class.java))
        }
        confirmDialog.setNegativeButton("Não") { dialog, _ ->
            dialog.cancel()
        }
        confirmDialog.show()
    }

    private val addMetaRegister = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data ->
                if (data.hasExtra(META_RESULT)) {
                    val resultMeta = data.getParcelableExtra<MetasModel>(META_RESULT)
                    if (resultMeta != null) {
                        metaAdapter.addListItem(resultMeta)
                        saveMetasList(metaAdapter.metasList as ArrayList<MetasModel>)
                    }
                }
            }
        }
    }

    override fun UpdateMeta() {
        saveMetasList(metaAdapter.metasList as ArrayList<MetasModel>)
    }


}