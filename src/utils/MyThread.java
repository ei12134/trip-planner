package utils;

import algorithms.Search;

public class MyThread implements Runnable {
	public volatile Search search;
	private Thread thread;

	public MyThread(Search search) {
		this.search = search;
	}

	public synchronized void start() {
		this.thread = new Thread(this);
		this.thread.start();
	}

	public synchronized Search waitStop() throws InterruptedException {
		this.thread.join();
		return search;
	}

	@Override
	public void run() {
		this.search.run();
	}
}
