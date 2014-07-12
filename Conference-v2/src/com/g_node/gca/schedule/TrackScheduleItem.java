package com.g_node.gca.schedule;


public class TrackScheduleItem {

	private String title;

    private String subtitle;
    
    private String chair;
    
    private EventScheduleItem[] eventsInTrack;

	public TrackScheduleItem(String title, String subtitle, String chair, int trackEventsCount) {
		this.title = title;
		this.subtitle = subtitle;
		this.chair = chair;
		this.eventsInTrack = new EventScheduleItem[trackEventsCount];
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getChair() {
		return chair;
	}

	public void setChair(String chair) {
		this.chair = chair;
	}

	public EventScheduleItem[] getEventsInTrack() {
		return eventsInTrack;
	}

	public void setEventsInTrack(EventScheduleItem[] eventsInTrack) {
		this.eventsInTrack = eventsInTrack;
	}
	
	public void setEventsInTrack(int index, EventScheduleItem eventToAdd) {
		this.eventsInTrack[index] = eventToAdd;
	}
	
}