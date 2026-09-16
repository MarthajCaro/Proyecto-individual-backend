package org.example.controllers;

import jakarta.validation.Valid;
import org.example.models.Task;
import org.example.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*") // Paso 5.1: Habilitar peticiones desde el frontend
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    // GET: Recupera todas las tareas
    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // POST: Crea una nueva tarea
    @PostMapping
    public Task createTask(@Valid @RequestBody Task task) {
        return taskRepository.save(task);
    }

    // PUT: Actualiza los datos de una tarea existente
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody Task taskDetails) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setName(taskDetails.getName());
                    task.setDescription(taskDetails.getDescription());
                    task.setFechaInicio(taskDetails.getFechaInicio());
                    task.setCategoria(taskDetails.getCategoria());
                    task.setDueDate(taskDetails.getDueDate());
                    task.setStatus(taskDetails.getStatus());

                    Task updatedTask = taskRepository.save(task);
                    return ResponseEntity.ok(updatedTask);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE: Elimina una tarea específica
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        return taskRepository.findById(id)
                .map(task -> {
                    taskRepository.delete(task);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}