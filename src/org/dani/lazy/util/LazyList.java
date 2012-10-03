package org.dani.lazy.util;

import java.util.List;

public interface LazyList<T> extends List<T> {
    public void addOnLoadListener(OnLoadListener listener);
    public void removeOnLoadListener(OnLoadListener listener);
    public boolean isLoaded(int index);
    public void getAsynchronous(int index);
    public void close();
}
