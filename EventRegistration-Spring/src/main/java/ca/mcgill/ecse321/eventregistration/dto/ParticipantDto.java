package ca.mcgill.ecse321.eventregistration.dto;

import java.util.Collections;
import java.util.List;

public class ParticipantDto {

	private String name;
	private List<EventDto> events;

	public ParticipantDto() {
	}

	@SuppressWarnings("unchecked")
	public ParticipantDto(String name) {
		this(name, Collections.EMPTY_LIST);
	}

	public ParticipantDto(String name, List<EventDto> arrayList) {
		this.name = name;
		this.events = arrayList;
	}

	public String getName() {
		return name;
	}

	public List<EventDto> getEvents() {
		return events;
	}

	public void setEvents(List<EventDto> events) {
		this.events = events;
	}

}
