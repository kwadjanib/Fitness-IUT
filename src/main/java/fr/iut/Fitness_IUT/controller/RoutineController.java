package fr.iut.Fitness_IUT.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fr.iut.Fitness_IUT.model.Routine;
import fr.iut.Fitness_IUT.repository.RoutineRepository;

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
        Long id, int p, int s, String mc, 
        RedirectAttributes redirectAttributes
    ){
        // TODO on pourrait vérifier si le produit existe
        this.routineRepo.deleteById(id);
        redirectAttributes.addAttribute("p", p);
        redirectAttributes.addAttribute("s", s);
        redirectAttributes.addAttribute("mc", mc);
        return "redirect:/routines";
    }     
}
