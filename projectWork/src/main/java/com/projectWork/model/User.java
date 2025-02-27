package com.projectWork.model;

import java.util.List;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
public class User
{
	public enum Role { USER, COACH, ADMIN }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Nome obbligatorio.")
    private String firstName;
    
    @NotBlank(message = "Cognome obbligatorio.")
    private String lastName;
    
    @Email(message = "L'email deve essere valida.")
    @Column(unique=true)
    private String email;
    
    @Length(min=8, message="La password deve essere composta da almeno otto (8) caratteri.")
    @NotBlank(message = "Password obbligatoria")
    private String password;
    
    private Integer secretCode;
    private String token;
    private Role role;
    
    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    private List<Course> courses;
    
    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    private List<Session> sessions;
    
    @ManyToOne
    @JoinColumn(name = "gym_id")
    private Gym gym;

    public User()
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

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public Role getRole()
	{
		return role;
	}

	public void setRole(Role role)
	{
		this.role = role;
	}

	public Gym getGym()
	{
		return gym;
	}

	public void setGym(Gym gym)
	{
		this.gym = gym;
	}

	public List<Course> getCourses()
	{
		return courses;
	}

	public void setCourses(List<Course> courses)
	{
		this.courses = courses;
	}

	public List<Session> getSessions()
	{
		return sessions;
	}

	public void setSessions(List<Session> sessions)
	{
		this.sessions = sessions;
	}

	public Integer getSecretCode()
	{
		return secretCode;
	}

	public void setSecretCode(Integer secretCode)
	{
		this.secretCode = secretCode;
	}

}
