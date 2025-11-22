package Tarefas.GerenciadorDeTarefas.Controller;

import Tarefas.GerenciadorDeTarefas.Dto.TaskDto;
import Tarefas.GerenciadorDeTarefas.Service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Gerenciador-Tarefas")
public class TaskController {

    // 1. Declare como final para garantir que nunca mude
    private final TaskService taskService;

    // 2. O CONSTRUTOR É O SEGREDO!
    // É aqui que o Spring injeta o Service. Sem isso, a variável fica null.
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskDto> addNewTask(@RequestBody TaskDto novaTarefa){

        // Agora taskService não é mais null, então essa linha vai funcionar
        TaskDto newTask = taskService.adicionarTarefa(novaTarefa);

        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }

}