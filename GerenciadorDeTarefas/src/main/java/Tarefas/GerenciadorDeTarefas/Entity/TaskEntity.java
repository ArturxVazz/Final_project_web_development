package Tarefas.GerenciadorDeTarefas.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tarefas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O titulo é obrigatorio")
    @Size(min = 3, max = 100, message = "O titulo deve ter entre 3 a 100 caracteres")
    private String titulo;

    @NotBlank(message = "O responsavel é obrigatorio")
    private String responsavel;

    @Column(columnDefinition = "TEXT")
    private String detalhamento;

    @NotNull(message = "A data de término é obrigatória")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd/MM/yyyy")
    private LocalDate dataTermino;
}
