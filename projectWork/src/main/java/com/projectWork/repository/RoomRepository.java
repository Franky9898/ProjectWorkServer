package com.projectWork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projectWork.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>
{

}
