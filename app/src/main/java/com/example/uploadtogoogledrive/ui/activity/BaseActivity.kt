package com.example.uploadtogoogledrive.ui.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.uploadtogoogledrive.di.qualifier.ActivityContext
import com.example.uploadtogoogledrive.ui.viewmodel.BaseActivityViewModel
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

abstract class BaseActivity<D : ViewDataBinding, V : BaseActivityViewModel> : AppCompatActivity(),
    HasAndroidInjector {
    @Inject
    protected lateinit var supportFragmentInjector: DispatchingAndroidInjector<Any>

    @Inject
    @field:ActivityContext
    protected lateinit var viewModelProvider: ViewModelProvider

    @get:LayoutRes
    protected abstract val layoutViewRes: Int

    protected abstract val viewModelClass: Class<V>
    lateinit var viewModel: V
        private set

    protected lateinit var binding: D
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        //create view model
        viewModel = viewModelProvider.get(viewModelClass)
        viewModel.handleActivityContext(this)
        viewModel.handleCreate()
        viewModel.handleIntent(intent)

        //create data binding layout and set view model
        binding = DataBindingUtil.setContentView(this, layoutViewRes)
        binding.lifecycleOwner = this
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return supportFragmentInjector
    }
}
