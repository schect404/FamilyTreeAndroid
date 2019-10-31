package com.atitto.familytree.base

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.atitto.familytree.common.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein

abstract class BaseFragment: Fragment(), KodeinAware {

    abstract val localModule: Kodein.Module

    abstract val layoutId: Int

    private val textWatchers = HashMap<Int, TextWatcher>()

    override val kodein: Kodein = Kodein.lazy {
        val parentKodein: Kodein by closestKodein()
        extend(parentKodein, allowOverride = true)
        import(localModule)
    }

    fun context() = requireContext()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        textWatchers.entries.forEach { entry ->
            view?.findViewById<TextInputEditText>(entry.key)?.removeTextChangedListener(entry.value)
        }
        textWatchers.clear()
    }

    fun TextView.textChangedListener(maxLines: Int? = null, listener: TextWatcher.(text: String) -> Unit) {
        textWatchers[this.id] = addTextChangedListener { listener.invoke(this, it) }
    }

}