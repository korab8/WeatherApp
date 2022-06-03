package com.musala.weatherapp.main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import com.musala.weatherapp.R
import com.musala.weatherapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI(){

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.home_menu, menu)
        val menuItem = menu?.findItem(R.id.searchItem)

        initSearchView(menuItem ?: return super.onCreateOptionsMenu(menu))

        return super.onCreateOptionsMenu(menu)
    }

    private fun initSearchView(menuItem: MenuItem){
        val searchView = menuItem.actionView as SearchView
        searchView.setIconifiedByDefault(true)
        searchView.queryHint = getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //get city weather
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //do nothing
                return false
            }

        })
    }
}