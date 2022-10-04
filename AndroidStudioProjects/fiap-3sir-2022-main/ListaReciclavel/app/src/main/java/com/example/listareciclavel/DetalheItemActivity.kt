package com.example.listareciclavel

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.listareciclavel.Constants.INDEX_INTENT
import com.example.listareciclavel.Constants.INDEX_RESULT
import com.example.listareciclavel.Constants.ITEM_INTENT
import com.example.listareciclavel.Constants.ITEM_RESULT
import com.example.listareciclavel.databinding.ActivityDetalheItemBinding
import com.example.listareciclavel.model.ItemListaModel

class DetalheItemActivity : AppCompatActivity() {
    lateinit var dataBind: ActivityDetalheItemBinding

    var item : ItemListaModel? = null
    var index : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBind = ActivityDetalheItemBinding.inflate(layoutInflater)
        setContentView(dataBind.root)

        item = intent.getParcelableExtra(ITEM_INTENT) //getParcelableExtra - pegar o valor do parâmetro
        index = intent.getIntExtra(INDEX_INTENT, -1) //-1 é o valor se nada for passado como param

        if(item != null){
            dataBind.editItem.setText(item?.item)
            dataBind.editItemDetail.setText(item?.detalhe)
        }

        dataBind.btnConfirmar.setOnClickListener {
            Intent().apply{ //aplica os putExtras no intent
                putExtra(
                    ITEM_RESULT,
                    ItemListaModel(
                        dataBind.editItem.text.toString(),
                        dataBind.editItemDetail.text.toString()
                    )
                )
                putExtra(INDEX_RESULT, index)
                setResult(Activity.RESULT_OK, this) //settar o estado dessa activity como OK
            }
            this.finish()
        }


    }
}