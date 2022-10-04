package com.example.listareciclavel

import com.example.listareciclavel.model.ItemListaModel

interface IcrudItem {
    fun AddItem()
    fun EditItem(item:ItemListaModel, position:Int)
    fun RemoveItem(item:ItemListaModel, position:Int)
}