package com.projectWork.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.projectWork.model.Course;
import com.projectWork.model.Room;
import com.projectWork.model.Session;
import com.projectWork.model.WeekDay;
import com.projectWork.repository.CourseRepository;
import com.projectWork.repository.RoomRepository;
import com.projectWork.repository.SessionRepository;
import com.projectWork.repository.WeekDayRepository;

@Service
public class SessionService
{

	@Autowired
	private SessionRepository sessionRepository;

	@Autowired
	private WeekDayRepository weekDayRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private CourseRepository courseRepository;

	public boolean isRoomAvailable(Room room, LocalTime newStartTime, LocalTime newEndTime)
	{
		List<Session> conflicts = sessionRepository.findConflictingSessions(room, newStartTime, newEndTime);
		return conflicts.isEmpty();
	}

	// Potrebbero servire Optional su room e gym
	public ResponseEntity<String> createSession(Session session)
	{
		Room roomFromDB = roomRepository.findById(session.getRoom().getId()).orElseThrow(() -> new RuntimeException("Stanza non trovata"));
		Course courseFromDB = courseRepository.findByTitle(session.getCourse().getTitle()).orElseThrow(() -> new RuntimeException("Corso non trovato"));
		session.setCourse(courseFromDB);
		WeekDay weekDayFromDB = weekDayRepository.findById(session.getSessionDay().getId()).orElseThrow(() -> new RuntimeException("WeekDay non trovato"));
		session.setSessionDay(weekDayFromDB);
		String result;
		if (!isRoomAvailable(roomFromDB, session.getStartingTime(), session.getEndingTime()))
		{
			result = "La stanza è già occupata.";
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(result);
		}
		if (session.getMaxParticipants() > roomFromDB.getCapacity())
		{
			result = "La stanza non può ospitare tale numero di partecipanti.";
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(result);
		}
		// inizio palestra > inizio sessione, fine palestra < fine sessione
		if (roomFromDB.getGym().getStartTime().isAfter(session.getStartingTime()) || roomFromDB.getGym().getEndTime().isBefore(session.getEndingTime()))
		{
			result = "La sessione non è compatibile con gli orari di lavoro della palestra.";
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(result);
		}
		sessionRepository.save(session);
		result = "Sessione creata con successo.";
		return ResponseEntity.ok(result);
	}
}