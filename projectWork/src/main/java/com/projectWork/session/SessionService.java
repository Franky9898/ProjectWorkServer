package com.projectWork.session;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	public Session createSession(Session session)
	{
		if (!isRoomAvailable(session.getRoom(), session.getStartingTime(), session.getEndingTime()))
		{
			throw new IllegalArgumentException("La stanza è già occupata.");
		}
		if (session.getMaxParticipants() > session.getRoom().getCapacity())
		{
			throw new IllegalArgumentException("La stanza non può ospitare tale numero di partecipanti.");
		}
		//inizio palestra < inizio sessione, fine palestra > fine sessione
		if(session.getRoom().getGym().getStartTime().isAfter(session.getStartingTime()) || session.getRoom().getGym().getEndTime().isBefore(session.getEndingTime()))
		{
			throw new IllegalArgumentException("La sessione non è compatibile con gli orari di lavoro della palestra.");
		}
		return sessionRepository.save(session);
	}
}