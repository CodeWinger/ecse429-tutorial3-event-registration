package ca.mcgill.ecse321.eventregistration.controller;

import java.sql.Date;
import java.time.LocalTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

import ca.mcgill.ecse321.eventregistration.dto.EventDto;
import ca.mcgill.ecse321.eventregistration.dto.ParticipantDto;
import ca.mcgill.ecse321.eventregistration.dto.RegistrationDto;
import ca.mcgill.ecse321.eventregistration.model.Event;
import ca.mcgill.ecse321.eventregistration.model.Participant;
import ca.mcgill.ecse321.eventregistration.model.Registration;
import ca.mcgill.ecse321.eventregistration.service.EventRegistrationService;
import ca.mcgill.ecse321.eventregistration.service.InvalidInputException;

@RestController
public class EventRegistrationRestController {

	@Autowired
	private EventRegistrationService service;

	@Autowired
	private ModelMapper modelMapper;

	@RequestMapping("/")
	public String index() {
		return "Event registration application root. Use the REST API to manage events and participants.\n";
	}

	//@formatter:off
	// Turning off formatter here to ease comprehension of the sample code by
	// keeping the linebreaks
	//Example REST call:
	//http://localhost:8088/participants/John
	@PostMapping(value = { "/participants/{name}", "/participants/{name}/" })
	public ParticipantDto createParticipant(
			@PathVariable("name") String name
		) throws InvalidInputException {
		//@formatter:on
		Participant participant = service.createParticipant(name);
		return convertToDto(participant);
	}

	//@formatter:off
	//Example REST call:
	//http://localhost:8080/events/testevent?date=2013-10-23&startTime=00:00&endTime=23:59
	@PostMapping(value = { "/events/{name}", "/events/{name}/" })
	public EventDto createEvent(
			@PathVariable ("name") String name, 
			@RequestParam Date date,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern="HH:mm") LocalTime startTime, 
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern="HH:mm") LocalTime endTime
		) throws InvalidInputException {
		//@formatter:on
		Event event = service.createEvent(name, date, startTime, endTime);
		return convertToDto(event);
	}

	@GetMapping(value = { "/participants", "/participants/" })
	public List<ParticipantDto> findAllParticipants() {
		List<ParticipantDto> participants = Lists.newArrayList();
		for (Participant participant : service.findAllParticipants()) {
			participants.add(convertToDto(participant));
		}
		return participants;
	}

	@GetMapping(value = { "/events", "/events/" })
	public List<EventDto> findAllEvents() {
		List<EventDto> eventDtos = Lists.newArrayList();
		for (Event event : service.findAllEvents()) {
			eventDtos.add(convertToDto(event));
		}
		return eventDtos;
	}

	// @formatter:off
	@PostMapping(value = { "/register", "/register/" })
	public RegistrationDto registerParticipantForEvent(
			@RequestParam (name = "participant") ParticipantDto pDto,
			@RequestParam (name = "event") EventDto eDto
		) throws InvalidInputException {
		// @formatter:on

		// Both the participant and the event are identified by their names
		Participant p = service.getParticipantByName(pDto.getName());
		Event e = service.getEventByName(eDto.getName());

		Registration r = service.register(p, e);
		return convertToDto(r, p, e);
	}

	// Example REST call:
	// http://localhost:8088/registrations/participant/JohnDoe
	@GetMapping(value = { "/registrations/participant/{name}", "/registrations/participant/{name}/" })
	public List<EventDto> getEventsOfParticipant(@PathVariable("name") ParticipantDto pDto) {
		Participant p = convertToDomainObject(pDto);
		return createEventDtosForParticipant(p);
	}

	@GetMapping(value = { "/participants/{name}", "/participants/{name}/" })
	public ParticipantDto getParticipantByName(@PathVariable("name") String name) throws InvalidInputException {
		return convertToDto(service.getParticipantByName(name));
	}

	@GetMapping(value = { "/events/{name}", "/events/{name}/" })
	public EventDto getEventByName(@PathVariable("name") String name) throws InvalidInputException {
		return convertToDto(service.getEventByName(name));
	}

	// Conversion methods (not part of the API)

	private EventDto convertToDto(Event e) {
		// In simple cases, the mapper service is convenient
		return modelMapper.map(e, EventDto.class);
	}

	private ParticipantDto convertToDto(Participant p) {
		ParticipantDto participantDto = modelMapper.map(p, ParticipantDto.class);
		participantDto.setEvents(createEventDtosForParticipant(p));
		return participantDto;
	}

	private RegistrationDto convertToDto(Registration r, Participant p, Event e) {
		// Now using the mapper would not help too much
		// RegistrationDto registrationDto = modelMapper.map(r, RegistrationDto.class);
		// Manual conversion instead
		EventDto eDto = convertToDto(e);
		ParticipantDto pDto = convertToDto(p);
		return new RegistrationDto(pDto, eDto);
	}

	private Participant convertToDomainObject(ParticipantDto pDto) {
		// Mapping DTO to the domain object without using the mapper
		List<Participant> allParticipants = service.findAllParticipants();
		for (Participant participant : allParticipants) {
			if (participant.getName().equals(pDto.getName())) {
				return participant;
			}
		}
		return null;
	}

	// Other extracted methods (not part of the API)

	private List<EventDto> createEventDtosForParticipant(Participant p) {
		List<Event> eventsForParticipant = service.getEventsForParticipant(p);
		List<EventDto> events = Lists.newArrayList();
		for (Event event : eventsForParticipant) {
			events.add(convertToDto(event));
		}
		return events;
	}

}
