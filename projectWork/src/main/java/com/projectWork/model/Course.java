package com.projectWork.model;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;


@Entity
public class Course
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique=true)
	private String title;
	private String description;

	@ManyToMany
	@JoinTable(name = "user_course", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	@JsonManagedReference
	private List<User> users;

	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
	private List<Session> sessions;

	@ManyToOne
    @JoinColumn(name = "gym_id") 
    private Gym gym;

	public Course()
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
		this.users = users.stream().filter(user -> user.getRole() != User.Role.USER).collect(Collectors.toList());
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

	public Gym getGym()
	{
		return gym;
	}

	public void setGyms(Gym gym)
	{
		this.gym = gym;
	}

}
