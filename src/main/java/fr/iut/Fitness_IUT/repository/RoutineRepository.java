package fr.iut.Fitness_IUT.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fr.iut.Fitness_IUT.model.Routine;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
    @Query("SELECT  r FROM Routine r WHERE r.name LIKE :x OR r.description LIKE :x")
    Page<Routine> rechercher(@Param(value = "x") String mc, Pageable pageable); 
}
