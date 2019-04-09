package com.demo.cloudevents.kafka;

import io.cloudevents.Extension;

public class PayExtensions implements Extension{
	private String header;
	
	public String getHeader() {
		return header;
	}
	
	public void setHeader(String header) {
		this.header = header;
	}
	
	@Override
	public String toString() {
		return "payExtensions{header:"+header+"}";
	}
}
