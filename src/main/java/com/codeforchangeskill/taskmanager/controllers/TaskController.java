package com.codeforchangeskill.taskmanager.controllers;


import com.codeforchangeskill.taskmanager.dto.CreateTaskDTO;
import com.codeforchangeskill.taskmanager.dto.ErrorResponseDTO;
import com.codeforchangeskill.taskmanager.dto.UpdateTaskDTO;
import com.codeforchangeskill.taskmanager.entities.TaskEntity;
import com.codeforchangeskill.taskmanager.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController
{

private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
  }
    @GetMapping("")
    public ResponseEntity<List<TaskEntity>> getTasks() {
        var tasks=taskService.getTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskEntity> getTaskById(@PathVariable("id") Integer id) {
        var task=taskService.getTaskbyId(id);
        if (task==null) {
            return ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(task);
    }

    @PostMapping("")
    public ResponseEntity<TaskEntity> addTask(@RequestBody CreateTaskDTO body) throws ParseException{
    var task=taskService.addTask(body.getTitle(), body.getDescription(),body.getDeadline());
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskEntity> updateTask(@PathVariable("id") Integer id, @RequestBody UpdateTaskDTO body) throws ParseException{
        var task=taskService.updateTask(id, body.getDescription(), body.getDeadline(), body.getCompleted());
        if (task==null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);
    }




    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleException(Exception e) {
    if(e instanceof ParseException){
        return ResponseEntity.badRequest().body(new ErrorResponseDTO("Invalid Date Format"));
    }
    e.printStackTrace();

    return ResponseEntity.internalServerError().body(new ErrorResponseDTO("Internal Server Error"));
    }
}
