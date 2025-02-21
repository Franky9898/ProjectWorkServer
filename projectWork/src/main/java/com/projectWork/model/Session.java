package com.projectWork.model;

import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalTime startingTime;
    private LocalTime endingTime;
    private Integer maxParticipants;
    
    // Many Sessions belong to one Course.
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    
    // Bidirectional ManyToMany with User.
    @ManyToMany
    @JoinTable(name = "session_user",
               joinColumns = @JoinColumn(name = "session_id"),
               inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;
	
	public Session() {}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public LocalTime getStartingTime()
	{
		return startingTime;
	}

	public void setStartingTime(LocalTime startingTime)
	{
		this.startingTime = startingTime;
	}

	public LocalTime getEndingTime()
	{
		return endingTime;
	}

	public void setEndingTime(LocalTime endingTime)
	{
		this.endingTime = endingTime;
	}

	public Integer getMaxParticipants()
	{
		return maxParticipants;
	}

	public void setMaxParticipants(Integer maxParticipants)
	{
		this.maxParticipants = maxParticipants;
	}

	public Course getCourse()
	{
		return course;
	}

	public void setCourse(Course course)
	{
		this.course = course;
	}

	public List<User> getUsers()
	{
		return users;
	}

	public void setUsers(List<User> users)
	{
		this.users = users;
	}
	
	
}
