package com.example.listareciclavel.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize //adicionar plugin no Build.Gradle //necessário para usar o Intent
data class ItemListaModel(
    var item: String,
    var detalhe: String,
    var isDetailVisile: Boolean = false
) : Parcelable