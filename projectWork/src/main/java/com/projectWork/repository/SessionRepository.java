package com.projectWork.repository;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.projectWork.model.Room;
import com.projectWork.model.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long>
{
	@Query("SELECT session FROM Session session WHERE session.room = ?1 AND session.endingTime > ?2 AND session.startingTime < ?3")
	List<Session> findConflictingSessions(Room room, LocalTime newStartTime, LocalTime newEndTime);
}
