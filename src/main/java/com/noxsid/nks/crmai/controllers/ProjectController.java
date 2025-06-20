package com.noxsid.nks.crmai.controllers;

import com.noxsid.nks.crmai.data.Project;
import com.noxsid.nks.crmai.data.Status;
import com.noxsid.nks.crmai.data.User;
import com.noxsid.nks.crmai.repository.UserRepository;
import com.noxsid.nks.crmai.service.JwtService;
import com.noxsid.nks.crmai.service.ProjectService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    @GetMapping
    public String getProjectPage(Model model, HttpServletRequest req){

        model.addAttribute("activeMenu", "project");
        Cookie[] cookies = req.getCookies();
        String token = null;
        for(Cookie cookie : cookies){
            if("access_token".equals(cookie.getName())){
                token=cookie.getValue();
            }
        }
        if(token==null){
            return "login";
        }
        User user = userRepository.findByUsername(jwtService.extractUsername(token)).orElseThrow();
        model.addAttribute("project", new Project());
        model.addAttribute("position", user.getPosition().toString());
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("statuses", Status.values());
        return "project";
    }

    @PostMapping
    public String postProject(@ModelAttribute Project project){
        if(project==null){
            throw new RuntimeException();
        }
        projectService.saveProject(project);
        return "project";
    }
}
