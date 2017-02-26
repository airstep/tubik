package com.tgs.tubik.tools;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxThreadingCallAdapterFactory extends CallAdapter.Factory {
    private final RxJavaCallAdapterFactory original;

    private RxThreadingCallAdapterFactory() {
        // Always call on background thread
        original = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());
    }

    public static CallAdapter.Factory create() {
        return new RxThreadingCallAdapterFactory();
    }

    @Override
    public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new RxCallAdapterWrapper(original.get(returnType, annotations, retrofit));
    }

    private static class RxCallAdapterWrapper implements CallAdapter<Observable<?>> {
        private final CallAdapter<?> wrapped;

        RxCallAdapterWrapper(CallAdapter<?> wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public Type responseType() {
            return wrapped.responseType();
        }

        @Override
        public <R> Observable<?> adapt(Call<R> call) {
            Observable observable = (Observable) wrapped.adapt(call);

            // Always handle result on main thread
            return observable.observeOn(AndroidSchedulers.mainThread());
        }
    }
}