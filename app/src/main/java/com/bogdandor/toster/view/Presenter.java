package com.bogdandor.toster.view;

public interface Presenter<V> {
    void onViewAttached(V view);
    void onViewDetached();
    void onDestroyed();
}
