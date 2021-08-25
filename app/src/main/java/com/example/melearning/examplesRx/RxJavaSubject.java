package com.example.melearning.examplesRx;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.AsyncSubject;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.ReplaySubject;

@SuppressWarnings({"NullableProblems", "unused", "SameParameterValue"})
public class RxJavaSubject {
    private RxKotlin stab;

    public void main(RxKotlin _stab) {
        stab = _stab;
        stab.log("start RxJavaSubject");

        createAsyncSubject();
    }

    void createAsyncSubject() {
        Observable<Integer> myObservable = Observable.just (0);

        //re-emit to each new subscribers with last item
//        AsyncSubject<Integer> subject = AsyncSubject.create();

        //re-remit with all items start with previous
//        BehaviorSubject<Integer> subject = BehaviorSubject.create();

        //re-remit with all items, do not completing automatically
//        PublishSubject<Integer> subject = PublishSubject.create();

        //re-emit all items
        ReplaySubject<Integer> subject = ReplaySubject.create();

//        myObservable.subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.single())
//                .subscribe(subject);

        subject.onNext(1);

        subject.subscribe(getObserver(0));

        subject.onNext(2);
        subject.onNext(3);

        subject.subscribe(getObserver(1));

//        subject.onComplete();
        subject.onNext(4);

        subject.subscribe(getObserver(2));

        sleep();
    }

    private <T> Observer<T> getObserver(int index) {
        return new Observer<T>() {
            @Override
            public void onSubscribe(Disposable d) {
                stab.log("Observer(" + index + ") onSubscribe");
            }

            @Override
            public void onNext(T s) {
                stab.log("Observer(" + index + ") Received " + s);
            }

            @Override
            public void onError(Throwable e) {
                stab.log("Observer(" + index + ") onError");
            }

            @Override
            public void onComplete() {
                stab.log("Observer(" + index + ") onComplete");
            }
        };
    }

    public void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
