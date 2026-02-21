package fr.iut.Fitness_IUT.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.iut.Fitness_IUT.model.Routine;
import fr.iut.Fitness_IUT.repository.RoutineRepository;
import jakarta.validation.Valid;

@Controller
public class RoutineController {

    private static final Logger log = LoggerFactory.getLogger(RoutineController.class);
    private RoutineRepository routineRepo;

    public RoutineController(RoutineRepository repo) {
        this.routineRepo = repo;
    }

    @GetMapping("/routines")
    public String listeRoutines(
            @RequestParam(value = "p", defaultValue = "0") int page,
            @RequestParam(value = "s", defaultValue = "5") int size,
            @RequestParam(value = "mc", defaultValue = "") String motCle,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Routine> pageRoutines;
        if (motCle.length() > 0) {
            pageRoutines = this.routineRepo.rechercher("%" + motCle + "%", pageable);
        } else {
            pageRoutines = this.routineRepo.findAll(pageable);
        }
        model.addAttribute("listRoutines", pageRoutines.getContent());
        model.addAttribute("pages", new int[pageRoutines.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("currentSize", size);
        model.addAttribute("motCle", motCle);
        log.info("Page={}, Size={}, MotCle='{}', NbRoutines={}", page, size, motCle, pageRoutines.getNumberOfElements());
        pageRoutines.getContent().forEach(r
                -> log.info("Routine id={}, name='{}', status='{}'", r.getId(), r.getName(), r.getStatus())
        );
        return "routines";
    }
    @RequestMapping(value = "/routineDelete", method = RequestMethod.GET)
    public String supprimerRoutine(
        @RequestParam("id") Long id,
        @RequestParam(value = "p", defaultValue = "0") Integer p,
        @RequestParam(value = "s", defaultValue = "5") Integer s,
        @RequestParam(value = "mc", defaultValue = "") String mc,
        RedirectAttributes redirectAttributes
    ){
        // TODO on pourrait vérifier si le produit existe
        this.routineRepo.deleteById(id);
        redirectAttributes.addAttribute("p", p);
        redirectAttributes.addAttribute("s", s);
        redirectAttributes.addAttribute("mc", mc);
        return "redirect:/routines";
    }     

    // TODO formulaire d'ajout et de modification d'une routine
    
    @GetMapping("/routineform")
    public String editRoutines(
            @RequestParam(defaultValue = "0") Long id,
            @RequestParam(value = "p", defaultValue = "0") int page,
            @RequestParam(value = "s", defaultValue = "5") int size,
            @RequestParam(value = "mc", defaultValue = "") String motCle,
            Model model) {
        if (id > 0) {
            Optional<Routine> optRout = this.routineRepo.findById(id);
            if (optRout.isPresent()) {
                model.addAttribute("routine", optRout.get());
            } else {
                return "redirect:/"; // routine non trouvée (mais id donnée) ??
            }
        } else {
            model.addAttribute("routine", new Routine());
        }

        model.addAttribute("p", page);
        model.addAttribute("s", size);
        model.addAttribute("mc", motCle);
        return "routineform";
    }

    @PostMapping("/routineSave")
    public String sauverRoutine(
            @Valid Routine routine,
            BindingResult bindingResult,
            @RequestParam(value = "p", defaultValue = "0") Integer p,
            @RequestParam(value = "s", defaultValue = "5") Integer s,
            @RequestParam(value = "mc", defaultValue = "") String mc,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("p", p);
            model.addAttribute("s", s);
            model.addAttribute("mc", mc);
            return "routineform";
        }
        if (routine.getCreationDate() == null) {
            if (routine.getId() != null) {
                Optional<Routine> existing = this.routineRepo.findById(routine.getId());
                if (existing.isPresent() && existing.get().getCreationDate() != null) {
                    routine.setCreationDate(existing.get().getCreationDate());
                } else {
                    routine.setCreationDate(LocalDate.now());
                }
            } else {
                routine.setCreationDate(LocalDate.now());
            }
        }
        this.routineRepo.save(routine);
        redirectAttributes.addAttribute("p", p);
        redirectAttributes.addAttribute("s", s);
        redirectAttributes.addAttribute("mc", mc);
        return "redirect:/routines";
    }
    //affichage d'une routine avec ses exercices (et possibilité de les ajouter/supprimer)
    @GetMapping("/showroutine")
    public String showRoutine(
            @RequestParam("id") Long id,
            Model model) {
        Optional<Routine> optRout = this.routineRepo.findById(id);
        if (optRout.isPresent()) {
            model.addAttribute("routine", optRout.get());
            
        } else {
            return "redirect:/"; // routine non trouvée (mais id donnée) ??
        }
        return "showroutine";
    }
}
