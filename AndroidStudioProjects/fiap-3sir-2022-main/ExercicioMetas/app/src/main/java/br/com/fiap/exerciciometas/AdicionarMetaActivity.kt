package br.com.fiap.exerciciometas

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.com.fiap.exerciciometas.Constants.META_RESULT
import br.com.fiap.exerciciometas.databinding.ActivityAdicionarMetaBinding

class AdicionarMetaActivity : AppCompatActivity() {
    private lateinit var databind: ActivityAdicionarMetaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databind = ActivityAdicionarMetaBinding.inflate(layoutInflater)
        setContentView(databind.root)

        databind.confirmBtn.setOnClickListener {
            val prioridade: Int = Integer.parseInt(databind.metaPrioridadeTxt.text.toString())
            Intent().apply {
                putExtra(
                    META_RESULT,
                    MetasModel(
                        prioridade,
                        databind.metaTituloTxt.text.toString(),
                        databind.metaDescricaoTxt.text.toString()
                    )
                )
                setResult(RESULT_OK, this)
            }
            this.finish()
        }
    }
}