package com.example.melearning.examplesRx;

import com.example.melearning.examples.AlbumInfoItem;
import com.example.melearning.examples.AlbumService;
import com.example.melearning.examples.RetrofitHelper;
import com.example.melearning.examples.UserInfo;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.melearning.examplesRx.RxJavaSimple.sleep;

@SuppressWarnings("FieldCanBeLocal")
public class RxJavaRetrofit {
    private RxKotlin stab;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private AlbumService retrofit;

    public void main(RxKotlin _stab) {
        stab = _stab;
        stab.log("RxJavaRetrofit start");

        retrofit = RetrofitHelper
                .createRetrofitInstance(RetrofitHelper.BaseUrl)
                .create(AlbumService.class);

        loadAlbumsOfAllUsers();

        sleep(2000);
        compositeDisposable.dispose();
    }

    void loadAlbumsOfAllUsers() {
        compositeDisposable.add(retrofit.getUsersRx().flatMap(userList -> {
            UserInfo []items = userList.toArray(new UserInfo[0]);
            return Observable.fromArray(items);
        })
        .flatMap(userInfo -> retrofit.getUserAlbumsRx(userInfo.getId()))
        .flatMap(albumInfoItems -> {
            AlbumInfoItem []items = albumInfoItems.toArray(new AlbumInfoItem[0]);
            return Observable.fromArray(items);
        })
        .subscribe(albumInfo -> {
            stab.log("AlbumsLoading " + "album id: " + albumInfo.getId());
        }));
    }

    void loadAlbums() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(retrofit.getAlbumsRx()
                .subscribeOn(Schedulers.io())
                .flatMap(albumInfoItems -> {
                    AlbumInfoItem []items = albumInfoItems.toArray(new AlbumInfoItem[0]);
                    return Observable.fromArray(items);
                })
                .subscribe(albumItem -> {
                    stab.log("AlbumsLoading" + "name: " + albumItem.getTitle());
                }));
    }

}
