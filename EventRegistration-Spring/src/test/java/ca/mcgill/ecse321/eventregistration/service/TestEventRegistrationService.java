package ca.mcgill.ecse321.eventregistration.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import org.junit.After;
//import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.mcgill.ecse321.eventregistration.model.Event;
import ca.mcgill.ecse321.eventregistration.model.Participant;
import ca.mcgill.ecse321.eventregistration.model.RegistrationManager;
import ca.mcgill.ecse321.eventregistration.persistence.PersistenceXStream;

public class TestEventRegistrationService {

	private RegistrationManager rm;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		PersistenceXStream.initializeModelManager("output"+File.separator+"data.xml");
	}

	@Before
	public void setUp() throws Exception {
		rm = new RegistrationManager();
	}

	@After
	public void tearDown() throws Exception {
		rm.delete();
	}

	@Test
	public void testCreateParticipant() {
		assertEquals(0, rm.getParticipants().size());
		
		String name = "Oscar";
		EventRegistrationService erc = new EventRegistrationService(rm);
		try {
			erc.createParticipant(name);
		} catch (InvalidInputException e) {
			// Check that no error occured
			fail();
		}

		RegistrationManager rm1 = rm;
		checkResultParticipant(name, rm1);

		RegistrationManager rm2 = (RegistrationManager) PersistenceXStream.loadFromXMLwithXStream();
		checkResultParticipant(name, rm2);

		rm2.delete();
	}
	
	@Test
	public void testCreateParticipantNull() {
		assertEquals(0, rm.getParticipants().size());
		String name = null;
		String error = null;
		
		EventRegistrationService erc = new EventRegistrationService(rm);
		try {
			erc.createParticipant(name);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		// check error
		assertEquals("Participant name cannot be empty!", error);
		
		// check no change in memory
		assertEquals(0, rm.getParticipants().size());
		
	}

	@Test
	public void testCreateParticipantEmpty() {	
		assertEquals(0, rm.getParticipants().size());
		
		String name = "";

		String error = null;
		EventRegistrationService erc = new EventRegistrationService(rm);
		try {
			erc.createParticipant(name);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		// check error
		assertEquals("Participant name cannot be empty!", error);
		
		// check no change in memory
		assertEquals(0, rm.getParticipants().size());

	}
	
	@Test
	public void testCreateParticipantSpaces() {
		assertEquals(0, rm.getParticipants().size());
		
		String name = " ";

		String error = null;
		EventRegistrationService erc = new EventRegistrationService(rm);
		try {
			erc.createParticipant(name);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		// check error
		assertEquals("Participant name cannot be empty!", error);
		
		// check no change in memory
		assertEquals(0, rm.getParticipants().size());

	}
	
	@Test
	public void testCreateEvent() {
		assertEquals(0, rm.getEvents().size());
		
		String name = "Soccer Game";
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 16, 9, 0, 0);
		Date eventDate = new Date(c.getTimeInMillis());
		LocalTime startTime = LocalTime.parse("09:00");
		c.set(2017, Calendar.MARCH, 16, 10, 30, 0);
		LocalTime endTime = LocalTime.parse("10:30");
		
		EventRegistrationService erc = new EventRegistrationService(rm);
		try {
			erc.createEvent(name, eventDate, startTime, endTime);
		} catch (InvalidInputException e) {
			fail();
		}
		
		checkResultEvent(name, eventDate, startTime, endTime,rm);
		
		RegistrationManager rm2 = (RegistrationManager) PersistenceXStream.loadFromXMLwithXStream();
		checkResultEvent(name, eventDate, startTime, endTime,rm2);
		rm2.delete();
	}

	@Test
	public void testRegister() {
		assertEquals(0, rm.getRegistrations().size());
		
		String nameP = "Oscar";
		Participant participant = new Participant(nameP);
		rm.addParticipant(participant);
		assertEquals(1, rm.getParticipants().size());
		
		String nameE = "Soccer Game";
		Calendar c = Calendar.getInstance();
		c.set(2017, Calendar.MARCH, 16, 9, 0, 0);
		Date eventDate = new Date(c.getTimeInMillis());
		Time startTime = new Time(c.getTimeInMillis());
		c.set(2017, Calendar.MARCH, 16, 10, 30, 0);
		Time endTime = new Time(c.getTimeInMillis());
		Event event = new Event(nameE, eventDate, startTime, endTime);
		rm.addEvent(event);
		assertEquals(1, rm.getEvents().size());
		
		EventRegistrationService erc = new EventRegistrationService(rm);
		try {
			erc.register(participant, event);
		} catch (InvalidInputException e) {
			fail();
		}
		
		checkResultRegister(nameP, nameE, eventDate, startTime, endTime, rm);
		
		RegistrationManager rm2 = (RegistrationManager) PersistenceXStream.loadFromXMLwithXStream();
		// check file contents
		checkResultRegister(nameP, nameE, eventDate, startTime, endTime, rm2);
		rm2.delete();

	}

	@Test
	public void testCreateEventNull() {
		assertEquals(0, rm.getRegistrations().size());

		String name = null;
		Date eventDate = null;
		LocalTime startTime = null;
		LocalTime endTime = null;
		
		String error = null;
		EventRegistrationService erc = new EventRegistrationService(rm);
		try {
			erc.createEvent(name, eventDate, startTime, endTime);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		// check error
		assertEquals("Event name cannot be empty! Event date cannot be empty! Event start time cannot be empty! Event end time cannot be empty!", error);
		// check model in memory
		assertEquals(0, rm.getEvents().size());
	}
	
	@Test
	public void testCreateEventEmpty() {
		assertEquals(0, rm.getEvents().size());

		String name = "";
		Calendar c = Calendar.getInstance();
		c.set(2017,Calendar.FEBRUARY,16,10,00,0);
		Date eventDate = new Date(c.getTimeInMillis());
		LocalTime startTime = LocalTime.parse("10:00");
//		Time startTime = new Time(c.getTimeInMillis());
		c.set(2017,Calendar.FEBRUARY,16,11,30,0);
//		Time endTime = new Time(c.getTimeInMillis());
		LocalTime endTime = LocalTime.parse("11:30");
		
		String error = null;
		EventRegistrationService erc = new EventRegistrationService(rm);
		try {
			erc.createEvent(name, eventDate, startTime, endTime);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

		// check error
		assertEquals("Event name cannot be empty!", error);
		// check model in memory
		assertEquals(0, rm.getEvents().size());
	}
	
	@Test
	public void testCreateEventSpaces() {
		assertEquals(0, rm.getEvents().size());

		String name = " ";
		Calendar c = Calendar.getInstance();
		c.set(2016,Calendar.OCTOBER,16,9,00,0);
		Date eventDate = new Date(c.getTimeInMillis());
//		Time startTime = new Time(c.getTimeInMillis());
		LocalTime startTime = LocalTime.parse("09:00");
		c.set(2016,Calendar.OCTOBER,16,10,30,0);
//		Time endTime = new Time(c.getTimeInMillis());
		LocalTime endTime = LocalTime.parse("10:30");
		
		String error = null;
		EventRegistrationService erc = new EventRegistrationService(rm);
		try {
			erc.createEvent(name, eventDate, startTime, endTime);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		// check error
		assertEquals("Event name cannot be empty!", error);
		// check model in memory
		assertEquals(0, rm.getEvents().size());

	}
	
	@Test
	public void testCreateEventEndTimeBeforeStartTime() {
		assertEquals(0, rm.getEvents().size());

		String name = "Soccer Game";
		Calendar c = Calendar.getInstance();
		c.set(2016,Calendar.OCTOBER,16,9,00,0);
		Date eventDate = new Date(c.getTimeInMillis());
//		Time startTime = new Time(c.getTimeInMillis());
		LocalTime startTime = LocalTime.parse("09:00");
		c.set(2016,Calendar.OCTOBER,16,8,59,59);
//		Time endTime = new Time(c.getTimeInMillis());
		LocalTime endTime = LocalTime.parse("08:59");
		
		String error = null;
		EventRegistrationService erc = new EventRegistrationService(rm);
		try {
			erc.createEvent(name, eventDate, startTime, endTime);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}

		// check error
		assertEquals("Event end time cannot be before event start time!", error);

		// check model in memory
		assertEquals(0, rm.getEvents().size());

	}
	
	@Test
	public void testRegisterNull() {
		assertEquals(0, rm.getRegistrations().size());
		
		Participant participant = null;
		assertEquals(0, rm.getParticipants().size());
		
		Event event = null;
		assertEquals(0, rm.getEvents().size());

		String error = null;
		EventRegistrationService erc = new EventRegistrationService(rm);
		try {
			erc.register(participant, event);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		// check error
		assertEquals("Participant needs to be selected for registration! Event needs to be selected for registration!", error);
		
		// check model in memory
		assertEquals(0, rm.getRegistrations().size());
		assertEquals(0, rm.getParticipants().size());
		assertEquals(0, rm.getEvents().size());

	}
	
	@Test
	public void testRegisterParticipantAndEventDoNotExist() {
		assertEquals(0, rm.getRegistrations().size());
		
		String nameP = "Oscar";
		Participant participant = new Participant(nameP);
		assertEquals(0, rm.getParticipants().size());
		
		String nameE = "Soccer Game";
		Calendar c = Calendar.getInstance();
		c.set(2016,Calendar.OCTOBER,16,9,00,0);
		Date eventDate = new Date(c.getTimeInMillis());
		Time startTime = new Time(c.getTimeInMillis());
		c.set(2016,Calendar.OCTOBER,16,10,30,0);
		Time endTime = new Time(c.getTimeInMillis());
		Event event = new Event(nameE, eventDate, startTime, endTime);
		assertEquals(0, rm.getEvents().size());

		String error = null;
		EventRegistrationService erc = new EventRegistrationService(rm);
		try {
			erc.register(participant, event);
		} catch (InvalidInputException e) {
			error = e.getMessage();
		}
		
		// check error
		assertEquals("Participant does not exist! Event does not exist!", error);
		
		// check model in memory
		assertEquals(0, rm.getRegistrations().size());
		assertEquals(0, rm.getParticipants().size());
		assertEquals(0, rm.getEvents().size());

	}
	
	private void checkResultRegister(String nameP, String nameE, Date eventDate, Time startTime, Time endTime,
			RegistrationManager rm2) {
		assertEquals(1, rm2.getParticipants().size());
		assertEquals(nameP, rm2.getParticipant(0).getName());
		assertEquals(1, rm2.getEvents().size());
		assertEquals(nameE, rm2.getEvent(0).getName());
		assertEquals(eventDate.toString(), rm2.getEvent(0).getEventDate().toString());
		assertEquals(startTime.toString(), rm2.getEvent(0).getStartTime().toString());
		assertEquals(endTime.toString(), rm2.getEvent(0).getEndTime().toString());
		assertEquals(1, rm2.getRegistrations().size());
		assertEquals(rm2.getEvent(0), rm2.getRegistration(0).getEvent());
		assertEquals(rm2.getParticipant(0), rm2.getRegistration(0).getParticipant());	
	}

	private void checkResultEvent(String name, Date eventDate, LocalTime startTime, LocalTime endTime, RegistrationManager rm2) {
		assertEquals(0, rm2.getParticipants().size());
		assertEquals(1, rm2.getEvents().size());
		assertEquals(name, rm2.getEvent(0).getName());
		assertEquals( eventDate.toString(), rm2.getEvent(0).getEventDate().toString());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		assertEquals(startTime.format(formatter).toString(), rm2.getEvent(0).getStartTime().toString());
		assertEquals(endTime.format(formatter).toString(), rm2.getEvent(0).getEndTime().toString());
		assertEquals(0, rm2.getRegistrations().size());		
	}

	private void checkResultParticipant(String name, RegistrationManager rm1) {
		assertEquals(1, rm1.getParticipants().size());
		assertEquals(name, rm1.getParticipant(0).getName());
		assertEquals(0, rm1.getEvents().size());
		assertEquals(0, rm1.getRegistrations().size());
	}


}
