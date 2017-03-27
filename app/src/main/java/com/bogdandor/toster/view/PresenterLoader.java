package com.bogdandor.toster.view;

import android.content.Context;
import android.support.v4.content.Loader;

public class PresenterLoader<T extends Presenter> extends Loader<T> {
    private T presenter;

    public PresenterLoader(Context context, T presenter) {
        super(context);
        this.presenter = presenter;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        deliverResult(presenter);
    }

    @Override
    protected void onReset() {
        presenter.onDestroyed();
        presenter = null;
    }
}
