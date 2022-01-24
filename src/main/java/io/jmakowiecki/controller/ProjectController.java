package io.jmakowiecki.controller;

import io.jmakowiecki.logic.ProjectService;
import io.jmakowiecki.model.Project;
import io.jmakowiecki.model.ProjectStep;
import io.jmakowiecki.model.projection.ProjectWriteModel;
import jdk.jfr.MetadataDefinition;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    private ProjectService service;

    ProjectController(final ProjectService service) {
        this.service = service;
    }

    @GetMapping
    String showProjects(Model model) {
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



