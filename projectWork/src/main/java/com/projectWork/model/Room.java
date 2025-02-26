package com.projectWork.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer capacity;
    
    @OneToMany(mappedBy = "room")
    private List<Session> sessions;
    
    @ManyToOne
    @JoinColumn(name = "gym_id")
    private Gym gym;
	
	public Room() {}
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public Integer getCapacity()
	{
		return capacity;
	}
	public void setCapacity(Integer capacity)
	{
		this.capacity = capacity;
	}
	public Gym getGym()
	{
		return gym;
	}
	public void setGym(Gym gym)
	{
		this.gym = gym;
	}

	public List<Session> getSessions()
	{
		return sessions;
	}

	public void setSessions(List<Session> sessions)
	{
		this.sessions = sessions;
	}
	
	
	
}
