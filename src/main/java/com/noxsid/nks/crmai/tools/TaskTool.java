package com.noxsid.nks.crmai.tools;

import com.noxsid.nks.crmai.data.Project;
import com.noxsid.nks.crmai.data.Status;
import com.noxsid.nks.crmai.data.Task;
import com.noxsid.nks.crmai.service.ProjectService;
import com.noxsid.nks.crmai.service.TaskService;
import com.noxsid.nks.crmai.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class TaskTool {

    private final UserService userService;
    private final TaskService taskService;
    private final ProjectService projectService;
    private final Logger logger = LoggerFactory.getLogger(TaskTool.class);
    @Tool(
            name = "Create Task Tool",
            description = """
                    Assigns task to certain user, gives title, description and recommendation how to accomplish task, 
                    assigns deadline but it is optional.
                    """
    )
    public String createTask(
            @ToolParam(description = "Take username from given prompt to assign task to that person") String username,
            @ToolParam(description = "Generate from given prompt description and recommendation for task") String info,
            @ToolParam(description = "From given prompt generate title for task") String title,
            @ToolParam(description = "If in the given prompt is deadline then give it otherwise unnecessary", required = false)LocalDate deadline,
            @ToolParam(description = "If given project name, give it otherwise unnecessary") String project
    ){
        try{
            logger.info("Task tool called!");
            if(userService.existsByUsername(username)){
                Task task = new Task();
                task.setAssigned_date(LocalDateTime.now());
                task.setUser(userService.findByUsername(username));
                task.setTitle(title);
                task.setDescription(info);
                if(deadline != null){
                    task.setDeadline(deadline);
                }
                else {
                    task.setDeadline(LocalDate.now().plusYears(1));
                }
                if(project !=null){
                    Project found_project = projectService.findByTitle(title);
                    if(found_project == null){
                        throw new NoSuchElementException("Could not find the project by " + project + " name.");
                    }
                    else {
                        task.setProject(found_project);
                    }
                }
                task.set_AI(true);
                task.setStatus(Status.TO_DO);
                taskService.saveTask(task);
                return task.toString();
            }
            else{
                return "Given username is not found!, please check whether you typed right username";
            }
        }
        catch (Exception e){
            logger.warn("Task Tool through an exception: " + e);
            return e.toString();
        }
    }
}
