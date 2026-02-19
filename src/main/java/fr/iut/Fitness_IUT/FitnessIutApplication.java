package fr.iut.Fitness_IUT;

import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import fr.iut.Fitness_IUT.model.Exercise;
import fr.iut.Fitness_IUT.model.Routine;
import fr.iut.Fitness_IUT.repository.RoutineRepository;

@SpringBootApplication
public class FitnessIutApplication {

	public static void main(String[] args) {
		SpringApplication.run(FitnessIutApplication.class, args);
	}

	@Bean
	CommandLineRunner initBDD(RoutineRepository repo) {
		return args -> {
			if (repo.count() > 0) {
				return;
			}
			/*  
			
			Routine r1 = new Routine(null, "Routine 1", "Description de la routine 1", LocalDate.now(), "active", new ArrayList<>());
			r1.addExercise(new Exercise(null, "Exercice 1", 10, 50.0, null));
			r1.addExercise(new Exercise(null, "Exercice 2", 15, 30.0, null));
			repo.save(r1);
			
			Routine r2 = new Routine(null, "Routine 2", "Description de la routine 2", LocalDate.now(), "inactive", new ArrayList<>());
			repo.save(r2);
			*/
		};
	}
}
