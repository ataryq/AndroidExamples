package com.example.melearning.fragments.paging.adapter

interface ClickListener<T> {
    fun onClick(chosenItem: T)
}