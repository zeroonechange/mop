package com.mop.ui.utils

import com.mop.base.mvvm.EmptyVM
import com.mop.base.ui.DataBindingBaseFragment
import com.mop.ui.BR
import com.mop.ui.R
import com.mop.ui.databinding.FragmentUiMainBinding
import com.mop.ui.databinding.FragmentUiMyBinding


class UIMyFragment : DataBindingBaseFragment<FragmentUiMyBinding, EmptyVM>(
    R.layout.fragment_ui_my,
    BR.viewModel
) {

}