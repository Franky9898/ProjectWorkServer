package com.projectWork;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.projectWork.model.Course;
import com.projectWork.model.Gym;
import com.projectWork.model.Room;
import com.projectWork.model.Session;
import com.projectWork.model.User;
import com.projectWork.model.WeekDay;
import com.projectWork.repository.CourseRepository;
import com.projectWork.repository.GymRepository;
import com.projectWork.repository.RoomRepository;
import com.projectWork.repository.SessionRepository;
import com.projectWork.repository.UserRepository;
import com.projectWork.repository.WeekDayRepository;

@Component
public class DataLoader implements CommandLineRunner
{

	private UserRepository userRepository;
	private GymRepository gymRepository;
	private RoomRepository roomRepository;
	private CourseRepository courseRepository;
	private SessionRepository sessionRepository;
	private WeekDayRepository weekDayRepository;

	DataLoader(UserRepository userRepository, GymRepository gymRepository, RoomRepository roomRepository, CourseRepository courseRepository, SessionRepository sessionRepository, WeekDayRepository weekDayRepository)
	{
		this.userRepository = userRepository;
		this.gymRepository = gymRepository;
		this.roomRepository = roomRepository;
		this.courseRepository = courseRepository;
		this.sessionRepository = sessionRepository;
		this.weekDayRepository = weekDayRepository;
	}

	@Override
	public void run(String... args) throws Exception
	{
		if (userRepository.count() == 0)
		{
			WeekDay monday = new WeekDay();
			monday.setDay(WeekDay.Day.LUNEDI);
			WeekDay tuesday = new WeekDay();
			tuesday.setDay(WeekDay.Day.MARTEDI);
			WeekDay wednesday = new WeekDay();
			wednesday.setDay(WeekDay.Day.MERCOLEDI);
			WeekDay thursday = new WeekDay();
			thursday.setDay(WeekDay.Day.GIOVEDI);
			WeekDay friday = new WeekDay();
			friday.setDay(WeekDay.Day.VENERDI);
			WeekDay saturday = new WeekDay();
			saturday.setDay(WeekDay.Day.SABATO);
			weekDayRepository.saveAll(new ArrayList<WeekDay> (Arrays.asList(monday, tuesday, wednesday, thursday, friday, saturday)));
			
			User admin = new User();
			admin.setFirstName("admin");
			admin.setLastName("admin");
			admin.setEmail("admin@admin.com");
			admin.setPassword("adminPassword");
			admin.setRole(User.Role.ADMIN);
			admin = userRepository.save(admin);

			User user = new User();
			user.setFirstName("Maio");
			user.setLastName("Ross");
			user.setEmail("mario.rossi@mail.com");
			user.setPassword("password");
			user.setRole(User.Role.USER);
			user = userRepository.save(user);

			User coach = new User();
			coach.setFirstName("Vanni");
			coach.setLastName("Giorgio");
			coach.setEmail("giorno@example.com");
			coach.setPassword("password2");
			coach.setRole(User.Role.COACH);
			coach = userRepository.save(coach);

			User user2 = new User();
			user2.setFirstName("Luigi");
			user2.setLastName("Bianco");
			user2.setEmail("luigi.bianchi@mail.com");
			user2.setPassword("password1");
			user2.setRole(User.Role.USER);
			user2 = userRepository.save(user2);

			Gym gym = new Gym();
			gym.setUsers(new ArrayList<>(Arrays.asList(user, admin)));
			gym.setOpenDays(new ArrayList<>(Arrays.asList(monday, wednesday, saturday)));
			gym.setStartTime(LocalTime.of(8, 0));
			gym.setEndTime(LocalTime.of(22, 0));
			gym = gymRepository.save(gym);

			Gym gym2 = new Gym();
			gym2.setUsers(new ArrayList<>(Arrays.asList(user2, coach)));
			gym2.setOpenDays(new ArrayList<>(Arrays.asList(monday, tuesday, friday)));
			gym2.setStartTime(LocalTime.of(8, 0));
			gym2.setEndTime(LocalTime.of(22, 0));
			gym2 = gymRepository.save(gym2);

			user.setGym(gym);
			userRepository.save(user);
			user2.setGym(gym2);
			userRepository.save(user2);
			coach.setGym(gym2);
			userRepository.save(coach);
			admin.setGym(gym);
			userRepository.save(admin);

			Room room = new Room();
			room.setCapacity(20);
			room.setGym(gym);
			room = roomRepository.save(room);

			Room room2 = new Room();
			room2.setCapacity(20);
			room2.setGym(gym);
			room2 = roomRepository.save(room2);

			Room room3 = new Room();
			room3.setCapacity(20);
			room3.setGym(gym2);
			room3 = roomRepository.save(room3);

			Course course = new Course();
			course.setTitle("Yoga");
			course.setDescription("Si usano le scarpe.");
			course.setUsers(new ArrayList<>(Arrays.asList(coach, admin)));
			course = courseRepository.save(course);

			Course course2 = new Course();
			course2.setTitle("HIIT");
			course2.setDescription("Daje.");
			course2.setUsers(new ArrayList<>(Arrays.asList(coach)));
			course2 = courseRepository.save(course2);

			gym.setCourses(new ArrayList<>(Arrays.asList(course, course2)));
			gymRepository.save(gym);
			gym2.setCourses(new ArrayList<>(Arrays.asList(course2)));
			gymRepository.save(gym2);

			course.setGym(gym);
			course = courseRepository.save(course);
			course2.setGym(gym);
			course2 = courseRepository.save(course2);

			Session session = new Session();
			session.setStartingTime(LocalTime.of(9, 0));
			session.setEndingTime(LocalTime.of(10, 0));
			session.setMaxParticipants(15);
			session.setCourse(course);
			session.setRoom(room);
			session.setUsers(new ArrayList<>(Arrays.asList(user)));
			session = sessionRepository.save(session);

			Session session2 = new Session();
			session2.setStartingTime(LocalTime.of(19, 0));
			session2.setEndingTime(LocalTime.of(20, 0));
			session2.setMaxParticipants(13);
			session.setRoom(room3);
			session2.setCourse(course);
			session2.setUsers(new ArrayList<>(Arrays.asList(user, user2)));
			session2 = sessionRepository.save(session2);

			course.setSessions(new ArrayList<>(Arrays.asList(session, session2)));
			courseRepository.save(course);

			coach.setCourses(new ArrayList<>(Arrays.asList(course, course2)));
			userRepository.save(coach);
			admin.setCourses(new ArrayList<>(Arrays.asList(course)));
			userRepository.save(admin);
			user2.setSessions(new ArrayList<>(Arrays.asList(session)));
			userRepository.save(user2);
			user.setSessions(new ArrayList<>(Arrays.asList(session, session2)));
			userRepository.save(user);
		}

	}
}
