package br.com.vitorruiz.marvelapp.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<V : ViewBinding> : Fragment() {
    private var mBinding: V? = null

    protected val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = getBindingComponent(inflater, container)
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initComponents(view, savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
    }

    protected open fun settings() {}
    protected abstract fun getBindingComponent(inflater: LayoutInflater, container: ViewGroup?): V
    protected abstract fun initComponents(rootView: View, savedInstanceState: Bundle?)
}