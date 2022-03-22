package com.mop.data

import android.os.Bundle
import android.util.Log
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.alibaba.android.arouter.facade.annotation.Route
import com.mop.base.base.act.BaseVBAct
import com.mop.base.net.initNetUnsplash
import com.mop.base.util.ArouterTable
import com.mop.base.util.immersive
import com.mop.data.databinding.ActivityDataMainBinding
import com.mop.data.viewmodel.NetVM
import kotlinx.android.synthetic.main.activity_data_main.*

@Route(path = ArouterTable.DATA_MAIN_ACT)
class DataMainActivity : BaseVBAct<NetVM, ActivityDataMainBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        immersive(toolbar, false)
        val navController = findNavController(R.id.nav)
        toolbar.setupWithNavController(navController, AppBarConfiguration(navView.menu, drawer))
        navController.addOnDestinationChangedListener { _, destination, _ ->
            toolbar.subtitle = (destination as FragmentNavigator.Destination).className.substringAfterLast('.')
        }
        navView.setupWithNavController(navController)
    }

    override fun initData() {
        val name = intent.getStringExtra("NAME")
        Log.e(TAG, "initData: name=$name")
        initNetUnsplash()
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers()
        } else super.onBackPressed()
    }
}

