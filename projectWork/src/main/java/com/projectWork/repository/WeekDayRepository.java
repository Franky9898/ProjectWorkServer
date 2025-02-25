package com.projectWork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projectWork.model.WeekDay;

@Repository
public interface WeekDayRepository extends JpaRepository<WeekDay, Long>
{

}
