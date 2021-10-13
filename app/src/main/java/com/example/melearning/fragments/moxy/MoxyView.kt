package com.example.melearning.fragments.moxy

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndStrategy
import moxy.viewstate.strategy.StateStrategyType

/*

AddToEndStrategy — выполнить команду и добавить команду в конец очереди
AddToEndSingleStrategy — выполнить команду, добавить ее в конец очереди и удалить все ее предыдущие экземпляры
SingleStateStrategy — выполнить команду, очистить очередь и добавить в нее команду
SkipStrategy — выполнить команду
OneExecuteStrategy — выполнить команду при первой возможности

*/

interface MoxyView: MvpView {
    @StateStrategyType(value = AddToEndStrategy::class)
    fun clickedView()
}