package com.mop.data.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.mop.base.base.fragment.BaseVBFragment
import com.mop.data.R
import com.mop.data.databinding.FragmentDataObjBinding
import com.mop.data.viewmodel.DataBaseVM

class ObjFragment : BaseVBFragment<DataBaseVM, FragmentDataObjBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_group, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.onNavDestinationSelected(findNavController())
        return true
    }

    override fun initData() {

    }

    override fun initView(savedInstanceState: Bundle?) {

    }
}