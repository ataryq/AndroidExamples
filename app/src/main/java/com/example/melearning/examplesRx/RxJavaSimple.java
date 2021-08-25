package com.example.melearning.examplesRx;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

@SuppressWarnings({"SameParameterValue", "unused", "ResultOfMethodCallIgnored"})
public class RxJavaSimple {
    private RxKotlin stab;

    public void main(RxKotlin _stab) {
        stab = _stab;
        stab.log("hello");

//        simpleRx()
        rxOperators();
        //bufferObserver();
    }

    void bufferObserver() {
        DisposableObserver<List<String>> bufferObserver = new DisposableObserver<List<String>>() {
            @Override
            public void onNext(@NonNull List<String> strings) {
                stab.log("onNext: " + strings);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                stab.log("onError");
            }

            @Override
            public void onComplete() {
                stab.log("onComplete");
            }
        };

        Observable<Integer> myObservable = createCustomObservableRangeSquare(0, 5);
        myObservable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                .map(num -> "" + num)
                .buffer(2)
                .subscribeWith(bufferObserver);

        sleep(2000);
        bufferObserver.dispose();
    }

    void rxOperators() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        DisposableObserver<String> myObserver = createObserver();
//        Observable<String> myObservable = Observable.just("hello", "world!");
//        String[] list = {"A", "B", "C"};
//        Observable<String> myObservable = Observable.fromArray(list);

        //emit numbers from 0 to 10
//        Observable<Integer> myObservable = Observable.range(0, 10);

        Observable<Integer> myObservable = createCustomObservableRangeSquare(0, 15);

        compositeDisposable.add(myObservable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.single())
                //filter - if it returns false the element will be skipped
                .filter(integer -> integer % 2 == 0)

                //remove duplicated elements
                .distinct()

                //just skip first N items
                .skip(2)
                //skip N items from end
                .skipLast(2)

                //map - apply operation to each elem
//                .map(integer -> { return "square result is " + integer; } )

                //flatMap - returns objects wrapped in observable object, can change order
//                .flatMap(i -> Observable.just("result is " + i, "item completed"))

                //concatMap - similar but returns elements in order as they get in,
                // it will wait until previous element is finished
                .concatMap(i -> Observable.just("result is " + i, "item completed"))

                .subscribeWith(myObserver)
        );

        //same as dispose for each observer
        //compositeDisposable.clear();
        sleep(2000);
    }

    Observable<Integer> createCustomObservableRangeSquare(Integer start, Integer end) {
        return Observable.create(emitter -> {
            for(int i = start; i < end; ++i) {
                emitter.onNext(i * i);
            }

            emitter.onComplete();
        });
    }

    void simpleRx() {
        Observable<String> myObservable = Observable.just("hello world!");
        DisposableObserver<Integer> myObserver = createObserver();
        myObservable.subscribe(createObserver());
        //stop receive signals
        myObserver.dispose();
    }

    <T> DisposableObserver<T> createObserver() {
        return new DisposableObserver<T>() {
            @Override
            public void onNext(@NonNull T s) {
                stab.log("onNext: " + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                stab.log("onError");
            }

            @Override
            public void onComplete() {
                stab.log("onComplete");
            }
        };
    }

    <T> DisposableObserver<T> createBufferObserver() {
        return new DisposableObserver<T>() {
            @Override
            public void onNext(@NonNull T s) {
                stab.log("onNext: " + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                stab.log("onError");
            }

            @Override
            public void onComplete() {
                stab.log("onComplete");
            }
        };
    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
