package Tarefas.GerenciadorDeTarefas.Controller;

import Tarefas.GerenciadorDeTarefas.Exception.TaskException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandlerController {

    @ExceptionHandler(TaskException.class)
    public ResponseEntity<String> handlerTaskException(TaskException e){

        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
