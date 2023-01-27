package com.aplikasi.apptoko.adapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.aplikasi.apptoko.LoginActivity
import com.aplikasi.apptoko.R
import com.aplikasi.apptoko.api.BaseRetrofit
import com.aplikasi.apptoko.response.produk.Produk
import com.aplikasi.apptoko.response.produk.ProdukResponsePost
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProdukAdapter(val listProduk: List<Produk>):RecyclerView.Adapter<ProdukAdapter.ViewHolder>() {

    private val api by lazy { BaseRetrofit().endpoint }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_produk, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produk = listProduk[position]
        holder.txtNamaProduk.text = produk.nama
        holder.txtHarga.text = produk.harga

        val token = LoginActivity.sessionManager.getString("TOKEN")

        holder.btnDelete.setOnClickListener {
            api.deleteProduk(token.toString(), produk.id.toInt()).enqueue(object :
                Callback<ProdukResponsePost> {
                override fun onResponse(
                    call: Call<ProdukResponsePost>,
                    response: Response<ProdukResponsePost>
                ) {
                    Log.d("ProdukError",response.toString())
                    Toast.makeText(holder.itemView.context,"Data berhasil di hapus",Toast.LENGTH_LONG).show()

                    holder.itemView.findNavController().navigate(R.id.produkFragment)
                }

                override fun onFailure(call: Call<ProdukResponsePost>, t: Throwable) {
                    Log.e("Data",t.toString())
                }

            })

        }

        holder.btnEdit.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("produk", produk)
            bundle.putString("status","edit")

            holder.itemView.findNavController().navigate(R.id.produkFormFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return listProduk.size
    }

    class ViewHolder(itemViem: View) : RecyclerView.ViewHolder(itemViem) {
        val txtNamaProduk = itemViem.findViewById(R.id.txtNamaProduk) as TextView
        val txtHarga = itemViem.findViewById(R.id.txtHarga) as TextView
        val btnDelete = itemViem.findViewById(R.id.btnDelete) as ImageButton
        val btnEdit = itemViem.findViewById(R.id.btnEdit) as ImageButton
    }
}