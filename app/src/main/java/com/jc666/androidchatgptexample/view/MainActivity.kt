package com.jc666.androidchatgptexample.view

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import coil.Coil
import coil.ImageLoader
import coil.decode.ImageDecoderDecoder
import coil.load
import com.jc666.androidchatgptexample.R
import com.jc666.androidchatgptexample.adapter.ContentAdapter
import com.jc666.androidchatgptexample.database.entity.ContentEntity
import com.jc666.androidchatgptexample.databinding.ActivityMainBinding
import com.jc666.androidchatgptexample.utils.xLog
import com.jc666.androidchatgptexample.viewModel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var branch : Int = 1 // # 1 -> First time loading
    private lateinit var binding : ActivityMainBinding
    private val viewModel : MainViewModel by viewModels()
    private var contentDataList = ArrayList<ContentEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * SplashScreen start
         * */
        installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * gif ImageLoader build
         * */
        val imageLoader = this.let {
            ImageLoader.Builder(it)
                .components {
                    add(ImageDecoderDecoder.Factory())
                }
                .build()
        }
        Coil.setImageLoader(imageLoader)

        initViewModelCallback()
        initViewComponentsListener()
        initDataAndView()

    }

    private fun initDataAndView() {
        binding.imageLoading.visibility = View.INVISIBLE
        binding.imageLoading.load(R.drawable.loading3)
        viewModel.getContentDataFromDatabase()
    }

    private fun initViewComponentsListener() {
        binding.sendImageBtn.setOnClickListener {
            binding.imageLoading.visibility = View.VISIBLE
            viewModel.postResponse(binding.editViewText.text.toString())
            viewModel.insertContent(binding.editViewText.text.toString(), 2, "") // 1: Gpt, 2: User
            binding.editViewText.setText("")
            branch = 2
            viewModel.getContentDataFromDatabase()
        }
    }

    private fun initViewModelCallback() {
        viewModel.contentList.observe(this, Observer {
            contentDataList.clear()
            for (entity in it) {
                contentDataList.add(entity)
            }
            setContentListOnRecyclerView(branch)
        })

        viewModel.deleteCheck.observe(this, Observer {
            if (it == true) {
                viewModel.getContentDataFromDatabase()
                branch = 1
            }
        })

        viewModel.gptInsertCheck.observe(this, Observer {
            if (it == true) {
                viewModel.getContentDataFromDatabase()
                binding.imageLoading.visibility = View.INVISIBLE
            }
            branch = 2
        })
    }

    private fun setContentListOnRecyclerView(branch : Int) {
        val contentAdapter = ContentAdapter(this, contentDataList)
        binding.rvContainer.adapter = contentAdapter
        binding.rvContainer.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }

        CoroutineScope(Dispatchers.Main).launch {
            delay(100)
            binding.svContainer.fullScroll(ScrollView.FOCUS_DOWN);
            if (branch != 1) {
                binding.editViewText.requestFocus()
            }
        }

        contentAdapter.delChatLayoutClick = object : ContentAdapter.DelChatLayoutClick {
            override fun onLongClick(view : View, position: Int) {
                xLog.e("${contentDataList[position].id}")
                // alertDialog
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("Delete")
                    .setMessage("Are you sure delete this msg.")
                    .setPositiveButton("Yes",
                        DialogInterface.OnClickListener { dialog, id ->
                            viewModel.deleteSelectedContent(contentDataList[position].id)
                        })
                    .setNegativeButton("No",
                        DialogInterface.OnClickListener { dialog, id ->
                        })
                builder.show()
            }
        }
    }
}