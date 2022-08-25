package com.example.melearning.fragments.testing

import android.text.Editable
import android.text.TextWatcher
import androidx.core.view.isVisible
import com.example.melearning.databinding.EspressoTestFragmentBinding
import com.example.melearning.fragments.BaseBindFragment2


class EspressoUiTestsFragment:
    BaseBindFragment2<EspressoTestFragmentBinding>(EspressoTestFragmentBinding::inflate) {

    override fun initViews() {
        binding.button6.setOnClickListener {
            it.isVisible = false
        }

        binding.editTextTextPersonName4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                binding.editTextTextPersonName5.text = binding.editTextTextPersonName4.text
            }
            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

            }
        })
    }
}