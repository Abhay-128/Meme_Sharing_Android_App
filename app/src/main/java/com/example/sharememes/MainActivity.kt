package com.example.sharememes

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.sharememes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private var currentImageURL :String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadMeme()
        binding.buttonShare.setOnClickListener { shareMeme(binding.buttonShare)}

        binding.buttonNext.setOnClickListener { nextMeme(binding.MemeImageView) }
    }

    private fun loadMeme(){
        binding.progressBarForAPIFetch.visibility = View.VISIBLE

        val url = "https://meme-api.com/gimme"


// Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            { response ->
                currentImageURL = response.getString("url")
                val imageView = binding.MemeImageView

                Glide.with(this).load(currentImageURL).listener(object :RequestListener<Drawable>{

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean

                    ): Boolean {
                        binding.progressBarForAPIFetch.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBarForAPIFetch.visibility = View.GONE
                        return false
                    }

                }).into(imageView)


//                Glide.with(this).load(currentImageURL).into(imageView)

            },
            {
//                Log.d("error", it.localizedMessage)
                Log.e("error",it.toString().substring(0,500))
            })

// Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    private fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Check this cool meme $currentImageURL")
        val chooser = Intent.createChooser(intent, "Share this meme using any of the below...")
        startActivity(chooser)
    }
    private fun nextMeme(view: View) {
        loadMeme()
    }

}