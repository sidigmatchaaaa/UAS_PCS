package com.aplikasi.apptoko

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aplikasi.apptoko.adapter.LaporanAdapter
import com.aplikasi.apptoko.api.BaseRetrofit
import com.aplikasi.apptoko.response.transaksi.TransaksiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class LaporanFragment : Fragment() {
    private val api by lazy { BaseRetrofit().endpoint }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_laporan, container, false)

        getLaporan(view)

        return view
    }
    fun getLaporan(view:View){
        val token = LoginActivity.sessionManager.getString("TOKEN")

        api.getTransaksi(token.toString()).enqueue(object : Callback<TransaksiResponse> {
            override fun onResponse(
                call: Call<TransaksiResponse>,
                response: Response<TransaksiResponse>
            ) {
                val rv = view.findViewById(R.id.rv_laporan) as RecyclerView

                val txtTotalPendapatan = view.findViewById(R.id.txtTotalPendapatan) as TextView
                val totalPendapatan = response.body()!!.data.total

                val localeID = Locale("in", "ID")
                val numberFormat = NumberFormat.getCurrencyInstance(localeID)

                txtTotalPendapatan.text = numberFormat.format(totalPendapatan.toDouble()).toString()

                rv.setHasFixedSize(true)
                rv.layoutManager = LinearLayoutManager(activity)
                val rvAdapter = LaporanAdapter(response.body()!!.data.transaksi)
                rv.adapter = rvAdapter
            }

            override fun onFailure(call: Call<TransaksiResponse>, t: Throwable) {
                Log.e("TransaksiResponseError",t.toString())
            }
        })
    }

}