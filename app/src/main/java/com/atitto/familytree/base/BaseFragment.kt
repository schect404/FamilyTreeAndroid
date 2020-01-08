package com.atitto.familytree.base

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.atitto.familytree.common.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein

abstract class BaseFragment: Fragment(), KodeinAware {

    abstract val localModule: Kodein.Module

    abstract val layoutId: Int

    abstract val navigator: BaseNavigator

    private val textWatchers = HashMap<Int, TextWatcher>()

    override val kodein: Kodein = Kodein.lazy {
        val parentKodein: Kodein by closestKodein()
        extend(parentKodein, allowOverride = true)
        import(localModule)
    }

    fun context() = requireContext()

    fun resetKeyboardMode() {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    fun adjustPanMode() {
        if (activity?.window?.attributes?.softInputMode != WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) {
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigator.attachFragmentManager(fragmentManager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        navigator.release()
        textWatchers.entries.forEach { entry ->
            view?.findViewById<TextInputEditText>(entry.key)?.removeTextChangedListener(entry.value)
        }
        textWatchers.clear()
    }

    fun TextView.textChangedListener(maxLines: Int? = null, listener: TextWatcher.(text: String) -> Unit) {
        textWatchers[this.id] = addTextChangedListener { listener.invoke(this, it) }
    }

    protected inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(aClass: Class<T>):T = f() as T
        }

    protected inline fun <reified VM : ViewModel> provideViewModelWithFactory(crossinline f: () -> VM) =
        ViewModelProviders.of(this, viewModelFactory(f)).get(VM::class.java)

}