package com.projectWork.model;

import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;


@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({"users", "courses", "rooms", "openDays"})
public class Gym
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToMany(mappedBy = "gym")
	private List<User> users;

	@ManyToMany
	@JoinTable(name = "gym_weekday", joinColumns = @JoinColumn(name = "gym_id"), inverseJoinColumns = @JoinColumn(name = "weekday_id"))
	private List<WeekDay> openDays;

	private LocalTime startTime;
	private LocalTime endTime;

	@OneToMany(mappedBy = "gym", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> courses;

	@OneToMany(mappedBy = "gym", cascade = CascadeType.ALL)
	private List<Room> rooms;

	public Gym()
	{
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public List<User> getUsers()
	{
		return users;
	}

	public void setUsers(List<User> users)
	{
		this.users = users;
	}

	public LocalTime getStartTime()
	{
		return startTime;
	}

	public void setStartTime(LocalTime startTime)
	{
		if (this.endTime != null && startTime.isAfter(this.endTime))
		{
			throw new IllegalArgumentException("L'orario di apertura non può essere dopo l'orario di chiusura");
		}
		this.startTime = startTime;
	}

	public LocalTime getEndTime()
	{
		return endTime;
	}

	public void setEndTime(LocalTime endTime)
	{

		if (this.startTime != null && this.startTime.isAfter(endTime))
		{
			throw new IllegalArgumentException("L'orario di apertura non può essere dopo l'orario di chiusura");
		}
		this.endTime = endTime;
	}

	public List<Room> getRooms()
	{
		return rooms;
	}

	public void setRooms(List<Room> rooms)
	{
		this.rooms = rooms;
	}

	public List<Course> getCourses()
	{
		return courses;
	}

	public void setCourses(List<Course> courses)
	{
		this.courses = courses;
	}

	public List<WeekDay> getOpenDays()
	{
		return openDays;
	}

	public void setOpenDays(List<WeekDay> openDays)
	{
		this.openDays = openDays;
	}

}
