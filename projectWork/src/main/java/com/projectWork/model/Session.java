package com.projectWork.model;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalTime startingTime;
    private LocalTime endingTime;
    private Integer maxParticipants;
    
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    
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
		if (this.endingTime != null && startingTime.isAfter(this.endingTime)) {
            throw new IllegalArgumentException("L'orario di inizio non può essere dopo l'orario di fine.");
        }
        this.startingTime = startingTime;
	}

	public LocalTime getEndingTime()
	{
		return endingTime;
	}

	public void setEndingTime(LocalTime endingTime)
	{
		if (this.startingTime != null && this.startingTime.isAfter(endingTime)) {
            throw new IllegalArgumentException("L'orario di inizio non può essere dopo l'orario di fine.");
        }
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
		this.users = users.stream().filter(user -> user.getRole() == User.Role.USER).collect(Collectors.toList());
	}

	public Room getRoom()
	{
		return room;
	}

	public void setRoom(Room room)
	{
		this.room = room;
	}
	
	
	
}
