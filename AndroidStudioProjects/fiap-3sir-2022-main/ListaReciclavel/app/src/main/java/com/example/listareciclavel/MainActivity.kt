package com.example.listareciclavel

import android.app.Activity
import android.app.AlertDialog
import android.app.Instrumentation
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.listareciclavel.Constants.INDEX_INTENT
import com.example.listareciclavel.Constants.INDEX_RESULT
import com.example.listareciclavel.Constants.ITEM_INTENT
import com.example.listareciclavel.Constants.ITEM_RESULT
import com.example.listareciclavel.databinding.ActivityMainBinding
import com.example.listareciclavel.model.ItemListaModel

class MainActivity : AppCompatActivity(), IcrudItem {
    lateinit var binding: ActivityMainBinding
    val lAdapter = ItemLinhaAdapter(this)
    var cont = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //var rc = findViewById<RecyclerView>(R.id.lista_reciclavel_rv)
        setupRecyCler()
        binding.addItemBtn.setOnClickListener {
            //lAdapter.addLista(ItemListaModel("Item $cont", "Detalhe do item $cont"))
            //cont++
            AddItem()
        }
    }

    private fun setupRecyCler() {
        binding.listaReciclavelRv.layoutManager = LinearLayoutManager(this)

        val l = mutableListOf<ItemListaModel>()
        l.add(ItemListaModel("aaaa", "Detalhe AAAA"))
        l.add(ItemListaModel("bbb", "Detalhe BBBB"))
        l.add(ItemListaModel("ccc", "Detalhe CC"))
        lAdapter.setLista(l)
        binding.listaReciclavelRv.adapter = lAdapter
    }

    override fun AddItem() {
        val intent = Intent(this, DetalheItemActivity::class.java)
        register.launch(intent)
    }

    override fun EditItem(item: ItemListaModel, position: Int) {
        val intent = Intent(this, DetalheItemActivity::class.java) //mapea o caminho desta para a Activity q ele deseja ir
        intent.putExtra(ITEM_INTENT, item) //itens que a activity nova vai receber com parâmetro
        intent.putExtra(INDEX_INTENT, position)
        //startActivity(intent)
        register.launch(intent)
    }

    private val register = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) { //resultado foi OK
            result.data?.let { data -> //let é um bloco de código com a condicional que esse data não é null
                if (data.hasExtra(ITEM_RESULT)) {
                    val item = data.getParcelableExtra<ItemListaModel>(ITEM_RESULT)
                    if (item != null) {
                        val index = data.getIntExtra(INDEX_RESULT, -1)
                        if (index >= 0) {
                            lAdapter.editLista(item, index)
                        } else {
                            lAdapter.addLista(item)
                        }
                    }
                }
            }
        }
    }

    override fun RemoveItem(item: ItemListaModel, position: Int) {
        val confirmDialog = AlertDialog.Builder(this) //Builder vai construir uma interface padrão de Dialog
        confirmDialog.setTitle("Exclusão")
        confirmDialog.setMessage("Confirma Exclusão do item " + item.item)
        confirmDialog.setPositiveButton("Sim") { _, _ ->
            lAdapter.removeLista(item)
        }
        confirmDialog.setNegativeButton("Não") { dialog, _ ->
            dialog.cancel()
        }
        confirmDialog.show()
    }

}