package com.example.melearning.examples

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.melearning.R
import com.example.melearning2.startFlow
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.merge
import reactivecircus.flowbinding.android.view.clicks

/** Examples
    - merge flow
    - flow binding
    - repeatOnLifecycle
*/

@FlowPreview
class RxBindingActivity : AppCompatActivity() {
    private var counterValue: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        flowBinding()
        startFlow()
    }

    private fun flowBinding() {
        setContentView(R.layout.activity_binding_view)

        
        val button: Button = findViewById(R.id.button)
        val button2: Button = findViewById(R.id.button2)
        val counter: TextView = findViewById(R.id.counter)

        lifecycleScope.launchWhenCreated {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                merge(button.clicks(), button2.clicks())
                    .debounce(500)
                    .collectLatest {
                        counterValue += 1
                        counter.post {
                            counter.text = counterValue.toString()
                        }
                    }
            }
        }
    }
}