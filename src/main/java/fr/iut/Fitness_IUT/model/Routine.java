package fr.iut.Fitness_IUT.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private LocalDate creationDate;

    private String status; // "active"/"inactive" (idéalement un enum)

    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Exercise> exercises = new ArrayList<>();

    // helpers (important pour garder la cohérence des deux côtés)
    public void addExercise(Exercise e) {
        if (exercises == null) {
            exercises = new ArrayList<>();
        }
        exercises.add(e);
        e.setRoutine(this);
    }

    public void removeExercise(Exercise e) {
        exercises.remove(e);
        e.setRoutine(null);
    }

}
