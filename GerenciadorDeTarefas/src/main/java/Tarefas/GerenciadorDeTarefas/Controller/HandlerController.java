package Tarefas.GerenciadorDeTarefas.Controller;

// --- IMPORTANTE: ESSAS SÃO AS LINHAS QUE GERALMENTE FALTAM ---
import Tarefas.GerenciadorDeTarefas.Exception.TaskException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;
// -------------------------------------------------------------

@RestControllerAdvice
public class HandlerController {

    // 1. Trata os SEUS erros (TaskException)
    // Note que mudamos de <String> para <Map<String, String>>
    @ExceptionHandler(TaskException.class)
    public ResponseEntity<Map<String, String>> handlerTaskException(TaskException e) {

        Map<String, String> resposta = new HashMap<>();
        resposta.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }

    // 2. Trata erros de validação do Spring (@NotBlank, @Size)
    // Caso você tenha esquecido de importar, o Java avisa aqui
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handlerValidacao(MethodArgumentNotValidException e) {

        Map<String, String> resposta = new HashMap<>();

        // Pega a primeira mensagem de erro que encontrar
        String erro = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        resposta.put("message", erro);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }
}