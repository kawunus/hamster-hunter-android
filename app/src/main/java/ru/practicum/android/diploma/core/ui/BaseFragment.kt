package com.kawunus.playlistmaker.core.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * Базовый Fragment. Позволяет привести к единому виду все реализации Fragment'ов. Работает в связке с BaseViewModel.
 *
 * При реализации потребуется реализовать абстрактные методы для инициализации View и слушатели событий
 */
typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel>(
    private val inflate: Inflate<VB>,
) : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!

    abstract val viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribe()
    }

    abstract fun initViews()

    abstract fun subscribe()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}