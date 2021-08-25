package com.example.melearning.fragments.rx_fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.melearning.R;
import com.example.melearning.databinding.RxFragmentBinding;
import com.example.melearning.examplesRx.RxJavaSimple;
import com.example.melearning.fragments.BaseBindFragment;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

@SuppressWarnings({"unused", "CheckResult", "SetTextI18n"})
public class RxFragment extends BaseBindFragment<RxFragmentBinding> {
    @Override
    public int layoutId() { return R.layout.rx_fragment; }

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupTextRepeater();
        setupInputNumber();
    }

    Integer getNumber() {
        String numberText = binding.numberEditText.getText().toString();
        if(numberText.isEmpty())
            return 0;
        return Integer.parseInt(numberText);
    }

    @SuppressLint("SetTextI18n")
    void setupInputNumber() {
        PublishSubject<Integer> subject = PublishSubject.create();

        RxView.clicks(binding.plusButton)
                .map(o -> getNumber() + 1)
                .subscribe(subject);

        RxView.clicks(binding.minusButton)
                .map(o -> getNumber() - 1)
                .subscribe(subject);

        Flowable<Integer> flowableButtonsObserver = subject.toFlowable(BackpressureStrategy.LATEST);
        PublishSubject<Integer> changeUiSubject = PublishSubject.create();

        //imitate long internet request
        getDisposable().add(flowableButtonsObserver
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(number -> {
                            RxJavaSimple.sleep(1000);
                            changeUiSubject.onNext(number);
                        }));

        getDisposable().add(changeUiSubject
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(number -> binding.numberEditText.setText(number.toString())));

        //change text
        getDisposable().add(RxTextView.textChanges(binding.numberEditText)
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter(charSequence -> charSequence.length() > 0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(number -> {
                    Log.d("setupInputNumber", "start operation: " + number);
                    binding.launchOperationTextField.setText("launched operation with number: " + number);
                }));
    }

    void setupTextRepeater() {
        getDisposable().add(RxTextView.textChanges(binding.editTextField)
                .skipInitialValue()
                //emit only last item in time
                .debounce(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .map(CharSequence::toString)
                //will wait until the value changed
                .distinctUntilChanged()
                .subscribe(charSequence -> {
                    Log.d("RxFragment", "copy text: " + charSequence);
                    binding.textField.setText(charSequence);
                }));

        getDisposable().add(RxView.clicks(binding.clearButton)
                .subscribe(o -> binding.editTextField.setText("")));
    }
}
