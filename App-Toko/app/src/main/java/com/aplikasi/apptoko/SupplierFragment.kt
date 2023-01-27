package com.aplikasi.apptoko

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aplikasi.apptoko.adapter.SupplierAdapter
import com.aplikasi.apptoko.api.BaseRetrofit
import com.aplikasi.apptoko.response.cart.Cart
import com.aplikasi.apptoko.response.produk.ProdukResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SupplierFragment : Fragment() {

    private val api by lazy { BaseRetrofit().endpoint }
    private lateinit var my_cart : ArrayList<Cart>
    private lateinit var total_bayar : String

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_supplier, container, false)
        getProduk(view)

        val btnBeli = view.findViewById<Button>(R.id.btnBeli)
        btnBeli.setOnClickListener{
            val bundle = Bundle()
            bundle.putParcelableArrayList("MY_CART", my_cart)
            bundle.putString("TOTAL", total_bayar)

            findNavController().navigate(R.id.bayarSupplierFragment, bundle)
        }

        return view
    }

    fun getProduk(view:View){
        val token = LoginActivity.sessionManager.getString("TOKEN")

        api.getProduk(token.toString()).enqueue(object : Callback<ProdukResponse> {
            override fun onResponse( call: Call<ProdukResponse>, response: Response<ProdukResponse>) {
                Log.d("ProdukData",response.body().toString())

                val rv = view.findViewById(R.id.rv_supplier) as RecyclerView

                rv.setHasFixedSize(true)
                rv.layoutManager = LinearLayoutManager(activity)
                val rvAdapter = SupplierAdapter(response.body()!!.data.produk)
                rv.adapter = rvAdapter

                rvAdapter.callbackInterface = object : CallbackInterface{
                    override fun passResultCallback(total: String, cart: ArrayList<Cart>) {
                        val txtTotalBayar = activity?.findViewById<TextView>(R.id.txtTotalBayar)

                        total_bayar = total
                        my_cart = cart

                        txtTotalBayar?.setText(cart.size.toString() + " item")

                        Log.d("MyCart",cart.toString())
                    }
                }
            }

            override fun onFailure(call: Call<ProdukResponse>, t: Throwable) {
                Log.e("ProdukError",t.toString())
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TransaksiFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TransaksiFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}