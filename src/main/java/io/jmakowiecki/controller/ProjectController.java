package io.jmakowiecki.controller;

import io.jmakowiecki.logic.ProjectService;
import io.jmakowiecki.model.Project;
import io.jmakowiecki.model.ProjectStep;
import io.jmakowiecki.model.projection.ProjectWriteModel;
import io.micrometer.core.annotation.Timed;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService service;

    ProjectController(final ProjectService service) {
        this.service = service;
    }

    @GetMapping
    String showProjects(Model model, Authentication auth) {
        model.addAttribute("project", new ProjectWriteModel());
        return "projects";
    }

    @PostMapping
    String addProject(
            @ModelAttribute("project") @Valid ProjectWriteModel current,
            BindingResult bindingResult,
            Model model) {
        if (!bindingResult.hasErrors()) {
            service.saveProject(current.toProject());
            model.addAttribute("project", new ProjectWriteModel());
            model.addAttribute("projects", getProjects());
            model.addAttribute("message", "Project added!");
        }
        return "projects";
    }


    @PostMapping(params = "addStep")
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current) {
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

    @Timed(value = "project.create.group", histogram = true, percentiles = {0.5, 0.95, 0.99})
    @PostMapping("/{id}")
    String createGroup(
            @ModelAttribute("project") ProjectWriteModel current,
            Model model,
            @PathVariable int id,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime deadline
    ) {
        try {
            service.createGroup(id, deadline);
            model.addAttribute("message", "Group added!");
        } catch (IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("message", "Error while creating group!");
        }
        return "projects";
    }

    @ModelAttribute("projects")
    List<Project> getProjects() {
        return service.readAll();
    }
}



