package com.projectWork.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projectWork.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long>
{

}
