package com.aplikasi.apptoko

import com.aplikasi.apptoko.response.cart.Cart

interface CallbackInterface {
    fun passResultCallback(total:String,cart:ArrayList<Cart>)
}