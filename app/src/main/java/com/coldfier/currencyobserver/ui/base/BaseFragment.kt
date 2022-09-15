package com.coldfier.currencyobserver.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.coldfier.currencyobserver.App
import com.coldfier.currencyobserver.R
import com.coldfier.currencyobserver.di.ViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseFragment<Binding: ViewBinding, VM: ViewModel> : Fragment() {

    private var _binding: Binding? = null
    protected val binding: Binding
        get() = _binding!!

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> Binding

    protected abstract val viewModel: VM

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Suppress("UNCHECKED_CAST")
    override fun onAttach(context: Context) {
        super.onAttach(context)

        (context.applicationContext as App).appComponent
            .inject(this as BaseFragment<ViewBinding, ViewModel>)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected fun showAlertDialog(@StringRes messageRes: Int) {
        AlertDialog.Builder(requireContext())
            .setMessage(messageRes)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    fun <T> Flow<T>.observeWithLifecycle(block: suspend (T) -> Unit): Job {
        return viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                this@observeWithLifecycle.collect {
                    block(it)
                }
            }
        }
    }
}