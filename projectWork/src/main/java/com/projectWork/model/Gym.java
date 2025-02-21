package com.projectWork.model;

import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
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
import jakarta.persistence.OneToOne;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Gym {

    public enum Day { LUNEDI, MARTEDI, MERCOLEDI, GIOVEDI, VENERDI, SABATO }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    
    private Day openDay;
    private LocalTime startTime;
    private LocalTime endTime;
    
    @ManyToMany
    @JoinTable(name = "gym_course",
               joinColumns = @JoinColumn(name = "gym_id"),
               inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> courses;
    
    @OneToMany(mappedBy = "gym", cascade = CascadeType.ALL)
    private List<Room> rooms;
	
	public Gym() {}
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public User getUser()
	{
		return user;
	}
	public void setUser(User user)
	{
		this.user = user;
	}
	public LocalTime getStartTime()
	{
		return startTime;
	}
	public void setStartTime(LocalTime startTime)
	{
		this.startTime = startTime;
	}
	public LocalTime getEndTime()
	{
		return endTime;
	}
	public void setEndTime(LocalTime endTime)
	{
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

	public Day getOpenDay()
	{
		return openDay;
	}

	public void setOpenDay(Day openDay)
	{
		this.openDay = openDay;
	}

	public List<Course> getCourses()
	{
		return courses;
	}

	public void setCourses(List<Course> courses)
	{
		this.courses = courses;
	}
	
	
	
	
}
