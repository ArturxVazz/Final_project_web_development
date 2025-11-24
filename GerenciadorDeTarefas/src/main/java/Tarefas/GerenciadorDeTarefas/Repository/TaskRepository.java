package Tarefas.GerenciadorDeTarefas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import Tarefas.GerenciadorDeTarefas.Entity.TaskEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    Optional<TaskEntity> findByTitulo(String title);

    List<TaskEntity> findByResponsavel(String Responsability);

    boolean existsByTituloAndResponsavel(String titulo, String responsavel);
}
