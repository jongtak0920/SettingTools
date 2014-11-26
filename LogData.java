package com.lge.concoleutils;

public class LogData {
	private String filePath;
	private String logData;
	
	public LogData(String filePath, String logData) {
		super();
		this.filePath = filePath;
		this.logData = logData;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getLogData() {
		return logData;
	}

	public void setLogData(String logData) {
		this.logData = logData;
	}
}
