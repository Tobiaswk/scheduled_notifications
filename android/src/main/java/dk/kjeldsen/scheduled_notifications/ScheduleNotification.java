package dk.kjeldsen.scheduled_notifications;

import java.io.Serializable;

public class ScheduleNotification implements Serializable {
	
	private String key;
	private int requestCode;
	private long triggerInMillis;
	private String ticker;
	private String contentTitle;
	private String content;

	
	public ScheduleNotification(long triggerInMillis, String ticker, String contentTitle, String content) {
		this.requestCode = (int) System.currentTimeMillis();
		this.triggerInMillis = triggerInMillis;
		this.ticker = ticker;
		this.contentTitle = contentTitle;
		this.content = content;
		this.key = ticker;
	}


	public String getKey() {
		return key;
	}

	public int getRequestCode() {
		return requestCode;
	}

	public long getTriggerInMillis() {
		return triggerInMillis;
	}

	public String getTicker() {
		return ticker;
	}

	public String getContentTitle() {
		return contentTitle;
	}

	public String getContent() {
		return content;
	}
}
