package com.digitalgoetz;

public class StreamRunner implements Runnable{

	Stream stream;
	
	public StreamRunner(String queryString){
		
		stream = new Stream(queryString);
		
	}
	
	@Override
	public void run() {
		stream.start();
	}

}
