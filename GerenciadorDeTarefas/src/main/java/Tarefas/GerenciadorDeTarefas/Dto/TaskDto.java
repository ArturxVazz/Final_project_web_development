package Tarefas.GerenciadorDeTarefas.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskDto {

    private Long id;
    private String titulo;
    private String responsavel;
    private String detalhamento;
    private LocalDate dataTermino;

}
