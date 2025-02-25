package com.projectWork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projectWork.model.Gym;

@Repository
public interface GymRepository extends JpaRepository<Gym,Long>
{

}
