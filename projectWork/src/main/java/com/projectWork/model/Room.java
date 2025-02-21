package com.projectWork.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer capacity;
    
    // Many Rooms can belong to one Gym.
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
	
	
}
