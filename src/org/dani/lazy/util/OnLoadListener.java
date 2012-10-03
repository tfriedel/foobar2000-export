package org.dani.lazy.util;

import java.util.EventListener;

public interface OnLoadListener extends EventListener {
    public void elementLoaded(OnLoadEvent event);
}
