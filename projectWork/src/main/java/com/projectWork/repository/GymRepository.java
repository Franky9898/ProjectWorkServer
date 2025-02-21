package com.projectWork.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projectWork.model.Gym;

public interface GymRepository extends JpaRepository<Gym,Long>
{

}
