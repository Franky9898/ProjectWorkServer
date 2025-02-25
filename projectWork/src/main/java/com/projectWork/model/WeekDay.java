package com.projectWork.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class WeekDay
{
	public enum Day { LUNEDI, MARTEDI, MERCOLEDI, GIOVEDI, VENERDI, SABATO }
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private Day day;
	@ManyToMany(mappedBy = "openDays")
	private List<Gym> gyms;
	
	public WeekDay() {}
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public Day getDay()
	{
		return day;
	}
	public void setDay(Day day)
	{
		this.day = day;
	}
	public List<Gym> getGyms()
	{
		return gyms;
	}

	public void setGyms(List<Gym> gyms)
	{
		this.gyms = gyms;
	}
	
}
