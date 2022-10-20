package com.wizeline.learningjava.utils.singleton;

public class PortConfig {
	private static PortConfig instance;
	public int port;

	private PortConfig(int port) {
		this.port = port;
	}

	public static PortConfig getInstance(int port) {
		if (instance == null) {
			instance = new PortConfig(port);
		}
		return instance;
	}

	public int getPort() {
		return this.port;
	}
}
