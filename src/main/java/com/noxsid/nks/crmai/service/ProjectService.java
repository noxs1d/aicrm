package com.noxsid.nks.crmai.service;

import com.noxsid.nks.crmai.data.Project;
import com.noxsid.nks.crmai.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public List<Project> getAllProjects(){
        return projectRepository.findAll();
    }

    public List<Project> sortProjectsBy(String sort){
        return projectRepository.findAll(Sort.by(sort));
    }

    public Project findByTitle(String title){
        return projectRepository.findByTitle(title).orElse(null);
    }
    public void saveProject(Project project){
        projectRepository.save(project);
    }
}
