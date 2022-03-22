package com.mop.data.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.mop.base.base.fragment.BaseDBFragment
import com.mop.data.R
import com.mop.data.databinding.FragmentDataMmkvBinding
import com.mop.data.viewmodel.MMKVVM

class MMKVFragment : BaseDBFragment<MMKVVM, FragmentDataMmkvBinding>() {

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
        mDatabind.viewModel = viewModel

    }

    override fun initView(savedInstanceState: Bundle?) {

    }
}