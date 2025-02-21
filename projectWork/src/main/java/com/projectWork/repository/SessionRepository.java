package com.projectWork.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projectWork.model.Session;

public interface SessionRepository extends JpaRepository<Session, Long>
{

}
