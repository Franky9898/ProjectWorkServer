package com.projectWork.model;

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

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String description;
    
    @ManyToMany
    @JoinTable(name = "user_course",
               joinColumns = @JoinColumn(name = "course_id"),
               inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;
    
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Session> sessions;
    
    @ManyToMany(mappedBy = "courses")
    private List<Gym> gyms;
	
	public Course() {}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public List<User> getUsers()
	{
		return users;
	}

	public void setUsers(List<User> users)
	{
		this.users = users;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public List<Session> getSessions()
	{
		return sessions;
	}

	public void setSessions(List<Session> sessions)
	{
		this.sessions = sessions;
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
