package com.mop.base.utils.loadsir

import com.mop.base.BR
import com.mop.base.R
import com.mop.base.databinding.ActivityLoadsirBinding
import com.mop.base.ui.DataBindingBaseActivity
import com.mop.base.utils.LogUtil


class LoadSirActivity : DataBindingBaseActivity<ActivityLoadsirBinding, LoadSirViewModel>(
    R.layout.activity_loadsir, BR.viewModel
) {
    override fun getLoadSirTarget(): Any? {
        return mBinding.layoutRoot
    }

    override fun onLoadSirReload() {
        LogUtil.i("LoadSirActivity", "commonLog - onLoadSirReload: ")
    }
}