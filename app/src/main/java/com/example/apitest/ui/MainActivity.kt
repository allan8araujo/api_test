package com.example.apitest.ui

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.apitest.api.ApiManage
import com.example.apitest.databinding.ActivityMainBinding
import com.example.apitest.model.Caracteristicas
import com.example.apitest.model.Item
import com.example.apitest.model.Posts
import com.example.apitest.network.NetworkUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var retrofit: Retrofit
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        retrofit = NetworkUtils.getRetrofitInstance("https://my-json-server.typicode.com/theylonf/jsonTest/")

        getData()

        setupButtons()

    }

    private fun setupButtons() {
        binding.swipeRefresh.setOnRefreshListener { getData() }

        binding.floatingActionButton.setOnClickListener { postData() }
    }

    private fun postData() {
        val list: MutableList<Item> = mutableListOf<Item>()
        val caracteristicas = Caracteristicas("Cadillac Eldorado",
            "Maior carro do mundo",
            "500000",
            "26",null,null,null,null)
        val item = Item(543,"carro","https://www.agoramotor.com.br/wp-content/uploads/2022/03/maior-carro-1024x576.jpg",caracteristicas)
        list.add(item)
        var post = Posts(200,"Xuxubeleza", list )

        retrofit.create(ApiManage::class.java)
            .putPosts(post)
            .enqueue(object : Callback<Posts>{
                override fun onResponse(call: Call<Posts>, response: Response<Posts>) {
                    if (response.isSuccessful){
                        post = response.body()!!
                        Log.d("post", "onResponse: ${post.toString()}")
                    }

                }

                override fun onFailure(call: Call<Posts>, t: Throwable) {
                    Log.d("post", "onFailure: $t")
                }

            })

    }

    private fun getData() {
        binding.progressBar.visibility = View.VISIBLE

//        GlobalScope.launch {
//            val result = produtosApi.getPosts()
//            if (result!= null){
//                result.body()?.let { setupUi(it) }
//            }
//        }

        retrofit.create(ApiManage::class.java)
            .getPosts()
            .enqueue(object :Callback<Posts>{
                override fun onResponse(call: Call<Posts>, response: Response<Posts>) {
                    if (response.isSuccessful){
                        val posts = response.body()
                        Log.d("Posts", "onResponse: "+posts.toString())
                        posts?.let { setupUi(it) }
                    }
                }

                override fun onFailure(call: Call<Posts>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun setupUi(posts: Posts) {
        val listSize = posts.item.size
        val random = Random.nextInt(listSize)
        val item = posts.item[random]
        val caracteristicas = item.caracteristicas
        supportActionBar?.title = item.tipo

        binding.tvEmpresa.text = posts.empresa

        when (item.tipo){
            "carro" -> binding.tvCaracteristicas.text = "modelo: ${caracteristicas.modelo}" +
                    "\nRodas: ${caracteristicas.rodas}" +
                    "\nValor: ${caracteristicas.valor}"
            "cadeira" -> binding.tvCaracteristicas.text = "modelo: ${caracteristicas.modelo}" +
                    "\nMaterial: ${caracteristicas.material}" +
                    "\nValor: ${caracteristicas.valor}"
            "casa" -> binding.tvCaracteristicas.text = "modelo: ${caracteristicas.modelo}" +
                    "\nQuartos: ${caracteristicas.quartos}" +
                    "\nBanheiros: ${caracteristicas.banheiros}" +
                    "\nEntrada: ${caracteristicas.entrada}" +
                    "\nValor: ${caracteristicas.valor}"
        }
        binding.tvDescricao.text = caracteristicas.descricao

        Glide.with(applicationContext)
            .load(item.url)
            .listener(object:RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.linearLayout.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefresh.isRefreshing = false

                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.linearLayout.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefresh.isRefreshing = false

                    return false
                }

            }).centerCrop()
            .into(binding.ivPhoto)
        binding.linearLayout.visibility = View.VISIBLE

    }
}