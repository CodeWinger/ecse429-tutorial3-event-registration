package ca.mcgill.ecse321.eventregistration.service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import ca.mcgill.ecse321.eventregistration.model.Event;
import ca.mcgill.ecse321.eventregistration.model.Participant;
import ca.mcgill.ecse321.eventregistration.model.Registration;
import ca.mcgill.ecse321.eventregistration.model.RegistrationManager;
import ca.mcgill.ecse321.eventregistration.persistence.PersistenceXStream;

@Service
public class EventRegistrationService {

	RegistrationManager rm;

	public EventRegistrationService(RegistrationManager aRegMan) {
		rm = aRegMan;
	}

	// Creations
	public Participant createParticipant(String name) throws InvalidInputException {
		throw new RuntimeException(String.format("TODO: implement method [%s#%s]!", new Throwable().getStackTrace()[0].getMethodName(), 
				this.getClass().getName()));
	}

	public Event createEvent(String name, Date date, LocalTime startTime, LocalTime endTime) throws InvalidInputException {
		throw new RuntimeException(String.format("TODO: implement method [%s#%s]!", new Throwable().getStackTrace()[0].getMethodName(), 
				this.getClass().getName()));
	}

	public Registration register(Participant participant, Event event) throws InvalidInputException {
		throw new RuntimeException(String.format("TODO: implement method [%s#%s]!", new Throwable().getStackTrace()[0].getMethodName(), 
				this.getClass().getName()));
	}

	// Queries
	public List<Participant> findAllParticipants() {
		return rm.getParticipants();
	}

	public List<Event> findAllEvents() {
		return rm.getEvents();
	}

	public Participant getParticipantByName(String name) throws InvalidInputException {
		List<Participant> participants = rm.getParticipants();
		for (Participant participant : participants) {
			if (participant.getName().equals(name)) {
				return participant;
			}
		}
		throw new InvalidInputException("No participant found with name " + name);
	}

	public Event getEventByName(String name) throws InvalidInputException {
		List<Event> events = rm.getEvents();
		for (Event event : events) {
			if (event.getName().equals(name)) {
				return event;
			}
		}
		throw new InvalidInputException("No event found with name " + name);
	}

	public List<Event> getEventsForParticipant(Participant p) {
		List<Event> eventsForParticipant = Lists.newArrayList();
		for (Registration reg : rm.getRegistrations()) {
			if (reg.getParticipant().equals(p)) {
				eventsForParticipant.add(reg.getEvent());
			}
		}
		return eventsForParticipant;
	}

}
