package com.projectWork.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.projectWork.model.Room;
import com.projectWork.model.Session;
import com.projectWork.repository.SessionRepository;

@Service
public class SessionService
{

	@Autowired
	private SessionRepository sessionRepository;

	public boolean isRoomAvailable(Room room, LocalTime newStartTime, LocalTime newEndTime)
	{
		List<Session> conflicts = sessionRepository.findConflictingSessions(room, newStartTime, newEndTime);
		return conflicts.isEmpty();
	}

	//Potrebbero servire Optional su room e gym
	public ResponseEntity<String> createSession(Session session)
	{
		String result;
		if (!isRoomAvailable(session.getRoom(), session.getStartingTime(), session.getEndingTime()))
		{
			result = "La stanza è già occupata.";
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(result);
		}
		if (session.getMaxParticipants() > session.getRoom().getCapacity())
		{
			result = "La stanza non può ospitare tale numero di partecipanti.";
					return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(result);
		}
		//inizio palestra > inizio sessione, fine palestra < fine sessione
		if(session.getRoom().getGym().getStartTime().isAfter(session.getStartingTime()) || session.getRoom().getGym().getEndTime().isBefore(session.getEndingTime()))
		{
			result = "La sessione non è compatibile con gli orari di lavoro della palestra.";
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(result);
		}
		sessionRepository.save(session);
		result = "Sessione creata con successo.";
		return ResponseEntity.ok(result);
	}
}