package org.dani.lazy.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class SimpleLazyList<E> implements LazyList<E> {
	// @todo check synchronization of this class
	private final static Object MISSING_DATA_ENTRY = new Object();
	private final int pageSize;
	private int size;
	private LazyListService<E> lazyListPeer;
	private List<Object> listCache;
	private List<OnLoadListener> listeners;
	private List<IndexInterval> pendingRequests;
	private PendingRequestHandler pendingRequestsHandler;
	private final boolean fetchLastPageOnly;
	private IndexInterval currentRequest;

	public SimpleLazyList(int pageSize, LazyListService<E> lazyListPeer) {
		this(pageSize, lazyListPeer, true);
	}

	public SimpleLazyList(int pageSize, LazyListService<E> lazyListPeer, boolean fetchLastPageOnly) {
		this.pageSize = pageSize;
		this.lazyListPeer = lazyListPeer;
		this.fetchLastPageOnly = fetchLastPageOnly;
		listeners = new LinkedList<OnLoadListener>();
		size = lazyListPeer.getSize();
		listCache = createListCache(size);
		initListCache(listCache, size);
		pendingRequests = Collections.synchronizedList(new ArrayList<IndexInterval>());
		pendingRequestsHandler = new PendingRequestHandler();
		pendingRequestsHandler.start();
	}

	public void updateLazyListService() {
		// @todo wait until pending requests empty
		pendingRequestsHandler.terminate();
//		listeners = new LinkedList<>();
		currentRequest = null;
		size = lazyListPeer.getSize();
		listCache = createListCache(size);
		initListCache(listCache, size);
		pendingRequests = Collections.synchronizedList(new ArrayList<IndexInterval>());
		pendingRequestsHandler = new PendingRequestHandler();
		pendingRequestsHandler.start();
	}

	public void close() {
		pendingRequestsHandler.terminate();
	}

	public void addOnLoadListener(OnLoadListener listener) {
		listeners.add(listener);
	}

	public void removeOnLoadListener(OnLoadListener listener) {
		listeners.remove(listener);
	}

	protected void fireOnLoadEvent(OnLoadEvent event) {
		for (OnLoadListener listener : listeners) {
			listener.elementLoaded(event);
		}
	}

	public boolean isLoaded(int index) {
		return listCache.get(index) != MISSING_DATA_ENTRY;
	}

	protected List<Object> createListCache(int size) {
		return Collections.synchronizedList(new ArrayList<Object>(size));
	}

	@SuppressWarnings("unchecked")
	protected void initListCache(List<Object> listCache, int size) {
		for (int i = 0; i < size; i++) {
			listCache.add(MISSING_DATA_ENTRY);
		}
	}

	public void add(int index, E element) {
		lazyListPeer.add(index, element);
		listCache.add(index, element);
	}

	public boolean add(E o) {
		add(listCache.size(), o);
		return true;
	}

	public boolean addAll(Collection<? extends E> c) {
		Iterator<? extends E> iterator = c.iterator();
		while (iterator.hasNext()) {
			add(iterator.next());
		}
		return true;
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public boolean contains(Object o) {
		throw new UnsupportedOperationException();
	}

	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	protected IndexInterval getStartEndFromIndex(int index) {
		return getStartEndFromPage(getPage(index));
	}

	protected IndexInterval getStartEndFromPage(int page) {
		int startElement = page * pageSize;
		int endElement = Math.min(size, startElement + pageSize);

		return new IndexInterval(startElement, endElement);
	}

	protected int getPage(int index) {
		return index / pageSize;
	}

	public void getAsynchronous(final int index) {
		if (!isLoaded(index)) {
			if (!isRequested(index)) {
				pendingRequests.add(getStartEndFromIndex(index));
			}
			synchronized (pendingRequestsHandler) {
				pendingRequestsHandler.notify();
			}
		}
	}

	protected boolean isRequested(int index) {
		return isInPendingRequests(index) || (currentRequest != null && currentRequest.contains(index));
	}

	protected boolean isInPendingRequests(int index) {
		synchronized (pendingRequests) {
			for (IndexInterval startEnd : pendingRequests) {
				if (startEnd.contains(index)) {
					return true;
				}
			}
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	public E get(int index) {
		if (!isLoaded(index)) {
			fetch(getStartEndFromIndex(index));
		}
		return (E) listCache.get(index);
	}

	public void fetch(IndexInterval startEnd) {
		System.out.println("Fetching: " + startEnd);
		E[] data = lazyListPeer.getData(startEnd.getStart(), startEnd.getEnd());
		for (int i = startEnd.getStart(); i < startEnd.getEnd(); i++) {
			listCache.set(i, data[i - startEnd.getStart()]);
		}
		fireOnLoadEvent(new OnLoadEvent(this, startEnd));
	}

	public int indexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	public boolean isEmpty() {
		return listCache.isEmpty();
	}

	public Iterator<E> iterator() {
		throw new UnsupportedOperationException();
	}

	public int lastIndexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	public ListIterator<E> listIterator() {
		throw new UnsupportedOperationException();
	}

	public ListIterator<E> listIterator(int index) {
		throw new UnsupportedOperationException();
	}

	public E remove(int index) {
		throw new UnsupportedOperationException();
	}

	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public E set(int index, E element) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		return listCache.size();
	}

	public List<E> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}

	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException();
	}

	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	public class PendingRequestHandler extends Thread {

		private boolean terminate;

		public PendingRequestHandler() {
			terminate = false;
		}

		public synchronized void terminate() {
			terminate = true;
			notify();
		}

		public void run() {
			while (!terminate) {
				while (!pendingRequests.isEmpty()) {
					if (fetchLastPageOnly) {
						currentRequest = pendingRequests.get(pendingRequests.size() - 1);
						pendingRequests.clear();
					} else {
						currentRequest = pendingRequests.remove(0);
					}
					fetch(currentRequest);
				}
				synchronized (this) {
					try {
						wait();
					} catch (InterruptedException e) {
						throw new IllegalStateException(e);
					}
				}
			}
		}
	}
}
