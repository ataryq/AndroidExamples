package com.example.melearning.fragments.moxy

import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class MoxyPresenter: MvpPresenter<MoxyView>() {
    var editText: String = "Hello moxy!"

    fun viewClicked() {
        viewState.clickedView()
    }
}