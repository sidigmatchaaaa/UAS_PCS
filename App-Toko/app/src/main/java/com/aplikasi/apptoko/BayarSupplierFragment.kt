package com.aplikasi.apptoko

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aplikasi.apptoko.api.BaseRetrofit
import com.aplikasi.apptoko.response.cart.Cart
import java.text.NumberFormat
import java.util.*

class BayarSupplierFragment : Fragment() {

    private val api by lazy { BaseRetrofit().endpoint }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_bayar_supplier, container, false)

        val total = arguments?.getString("TOTAL")
        val my_cart = arguments?.getParcelableArrayList<Cart>("MY_CART")

        val localeID = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)

        val txtKembalian = view.findViewById<TextView>(R.id.txtKembalian)
        val txtTotalTransaksiBayar = view.findViewById<TextView>(R.id.txtTotalTransaksiBayar)
        txtTotalTransaksiBayar.setText(numberFormat.format(total.toString().toDouble()).toString())

        val txtPenerimaan = view.findViewById<EditText>(R.id.txtPenerimaan)
        txtPenerimaan.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if(txtPenerimaan.text.toString() != ""){
                    val penerimaan:Int = txtPenerimaan.text.toString().toInt()
                    val kembalian = penerimaan - total.toString().toInt()

                    if(kembalian > 0){
                        txtKembalian.setText(numberFormat.format(kembalian.toDouble()).toString())
                    }else{
                        txtKembalian.setText(numberFormat.format(0).toString())
                    }
                }
            }

        })

        val token = LoginActivity.sessionManager.getString("TOKEN")
        val adminId = LoginActivity.sessionManager.getString("ADMIN_ID")

        val btnSimpanBayar = view.findViewById<Button>(R.id.btnSimpanBayar)
        btnSimpanBayar.setOnClickListener{

            Toast.makeText(view.context, "Pembelian Berhasil", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.supplierFragment)
        }

        return view

    }

}