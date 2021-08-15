package com.example.projeto001.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto001.R
import com.example.projeto001.adapter.Adapter
import com.example.projeto001.model.Products
import com.example.projeto001.service.RetrofitBuilder
import com.example.projeto001.utils.snackBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SegundoFragment : Fragment(R.layout.fragment_segundo),Callback<List<Products>> {
    private var listOfProducts : List<Products>?= null

    companion object {
        fun newInstance() = SegundoFragment()
    }
    lateinit var recyclerView: RecyclerView
    private val productsCall by lazy {
        RetrofitBuilder.getProductsServices().getProducts()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        productsCall.clone().enqueue(this)
    }

    private fun loadComponents(view: View){
        recyclerView = view.findViewById(R.id.recyclerViewProducts)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = Adapter(listOfProducts!!)
    }

    override fun onResponse(call: Call<List<Products>>, response: Response<List<Products>>) {
        response.body()?.apply {
            if (this != null){
                listOfProducts = this
                loadComponents(requireView())
            } else{
                productsCall.clone().enqueue(this)
            }
        }
    }

    override fun onFailure(call: Call<List<Products>>, t: Throwable) {
        view.let {
            snackBar(recyclerView, R.string.error_message_server)
        }
    }
}