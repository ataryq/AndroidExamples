package com.example.melearning.fragments.paging

interface ClickListener<T> {
    fun onClick(chosenItem: T)
}