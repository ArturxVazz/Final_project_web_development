package Tarefas.GerenciadorDeTarefas.Controller;

import Tarefas.GerenciadorDeTarefas.Dto.TaskDto;
import Tarefas.GerenciadorDeTarefas.Service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Gerenciador-Tarefas")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<String> addNewTask(@RequestBody TaskDto novaTarefa){

        // Agora taskService não é mais null, então essa linha vai funcionar
        TaskDto newTask = taskService.adicionarTarefa(novaTarefa);

        String mensagem = "A tarefa " + novaTarefa.getTitulo() + " foi adicionada com sucesso";

        return ResponseEntity.status(HttpStatus.CREATED).body(mensagem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteByTask (@PathVariable Long id){
        taskService.deleteTask(id);

        return ResponseEntity.ok("A task " + id + "foi deletada com sucesso");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateByTask(@PathVariable Long id, @RequestBody TaskDto updateTask){
        taskService.updateTask(id, updateTask);

        return ResponseEntity.ok("A task " + updateTask.getTitulo() + " foi atualizada com sucesso");
     }
}