package com.noxsid.nks.crmai.tools;

import com.noxsid.nks.crmai.data.Project;
import com.noxsid.nks.crmai.data.Status;
import com.noxsid.nks.crmai.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ProjectTool {

    private final Project project = new Project();
    private final ProjectService projectService;
    private final Logger logger = LoggerFactory.getLogger(ProjectTool.class);

    @Tool(
            name = "Project Creation Tool",
            description = """
                    Creates project, where project have title (Given in the prompt),
                    description (Generate depending on given prompt), deadline (Given in the prompt),
                    and can created only by CEO or Manger.
                    """
    )
    public String projectCreation(
            @ToolParam(description = "Take the title of the project from prompt") String title,
            @ToolParam(description = "Generate description by given prompt and generate recommendation for project") String description,
            @ToolParam(description = "Take deadline(yyyy-MM-dd) by given prompt, if deadline not given then unnecessary", required = false) LocalDate deadline
            )
    {
        logger.info("Project Creation tool called!");
        try {
            project.setTitle(title);
            project.setStatus(Status.TO_DO);
            project.setDescription(description);
            if(deadline!=null){
                project.setDeadline(deadline);
            }
            else{
                project.setDeadline(LocalDate.now().plusYears(1));
            }
            projectService.saveProject(project);
            return project.toString() + ". Project was created";
        }catch (Exception e){
            logger.warn("Project tool exc: " + e);
            return e.toString();
        }
    }
}
