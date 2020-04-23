package com.example.loginpoc

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.IOException
import java.util.*

class Timeline : AppCompatActivity(), ImageRequester.ImageRequesterResponse {

    private var photosList: ArrayList<Photo> = ArrayList()
    private lateinit var imageRequester: ImageRequester
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerAdapter
    private val lastVisibleItemPosition: Int
        get() = if (recyclerView.layoutManager == linearLayoutManager) {
            linearLayoutManager.findLastVisibleItemPosition()
        } else {
            gridLayoutManager.findLastVisibleItemPosition()
        }
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var recyclerView:RecyclerView;

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        recyclerView=findViewById(R.id.recyclerView)
        imageRequester = ImageRequester(this)
        linearLayoutManager = LinearLayoutManager(this)
        gridLayoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = linearLayoutManager
        adapter = RecyclerAdapter(photosList)
        recyclerView.adapter = adapter
        setRecyclerViewScrollListener()
        setRecyclerViewItemTouchListener()
    }

    override fun onStart() {
        super.onStart()
        if (photosList.size == 0) {
            requestPhoto()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_recycler_manager) {
            changeLayoutManager()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun requestPhoto() {
        try {
            imageRequester.getPhoto()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    override fun receivedNewPhoto(newPhoto: Photo) {
        runOnUiThread {
            photosList.add(newPhoto)
            adapter.notifyItemInserted(photosList.size)
        }
    }

    private fun setRecyclerViewScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = recyclerView.layoutManager!!.itemCount
                if (!imageRequester.isLoadingData && totalItemCount == lastVisibleItemPosition + 1) {
                    requestPhoto()
                }
            }
        })
    }

    private fun setRecyclerViewItemTouchListener() {
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                viewHolder1: RecyclerView.ViewHolder): Boolean {
                return false;
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                photosList.removeAt(position)
                recyclerView.adapter!!.notifyItemRemoved(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun changeLayoutManager() {
        if (recyclerView.layoutManager == linearLayoutManager) {
            recyclerView.layoutManager = gridLayoutManager
            if (photosList.size == 1) {
                requestPhoto()
            }
        } else {
            recyclerView.layoutManager = linearLayoutManager
        }
    }

}
