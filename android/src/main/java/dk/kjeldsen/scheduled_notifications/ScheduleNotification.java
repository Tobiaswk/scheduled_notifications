package dk.kjeldsen.scheduled_notifications;

import java.io.Serializable;

public class ScheduleNotification implements Serializable {
	
	private int notificationId;
	private long triggerInMillis;
	private String ticker;
	private String contentTitle;
	private String content;

	
	public ScheduleNotification(long triggerInMillis, String ticker, String contentTitle, String content) {
		this.notificationId = (int) System.currentTimeMillis();
		this.triggerInMillis = triggerInMillis;
		this.ticker = ticker;
		this.contentTitle = contentTitle;
		this.content = content;
	}

	public int getNotificationId() {
		return notificationId;
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
