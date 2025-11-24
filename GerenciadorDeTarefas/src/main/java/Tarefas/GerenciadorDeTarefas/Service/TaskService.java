package Tarefas.GerenciadorDeTarefas.Service;

import Tarefas.GerenciadorDeTarefas.Dto.TaskDto;
import Tarefas.GerenciadorDeTarefas.Entity.TaskEntity;
import Tarefas.GerenciadorDeTarefas.Exception.TaskException;
import Tarefas.GerenciadorDeTarefas.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public TaskDto adicionarTarefa (TaskDto taskDto){

        if(taskDto.getDataTermino().isBefore(LocalDate.now())){
            throw new TaskException("A data de término não pode ser no passado");
        }

        boolean jaExiste=  taskRepository.existsByTituloAndResponsavel(taskDto.getTitulo(),taskDto.getResponsavel());

        if(jaExiste){
            throw new TaskException("O usuario não pode ter duas tarefas com o mesmo nome");
        }


        TaskEntity taskEntity = new TaskEntity();

        taskEntity.setTitulo(taskDto.getTitulo());
        taskEntity.setResponsavel(taskDto.getResponsavel());
        taskEntity.setDetalhamento(taskDto.getDetalhamento());
        taskEntity.setDataTermino(taskDto.getDataTermino());

        TaskEntity newTask = taskRepository.save(taskEntity);

        taskDto.setId(newTask.getId());

        return taskDto;
    }

    public void updateTask (Long id, TaskDto taskDto){

        if(taskDto.getDataTermino().isBefore(LocalDate.now())){
            throw new TaskException("A data de término não pode ser no passado  ");
        }

        TaskEntity updateTask = taskRepository.findById(id).orElseThrow(null);

        updateTask.setTitulo(taskDto.getTitulo());
        updateTask.setResponsavel(taskDto.getResponsavel());
        updateTask.setDetalhamento(taskDto.getDetalhamento());
        updateTask.setDataTermino(taskDto.getDataTermino());

        taskRepository.save(updateTask);
    }

    public void deleteTask (Long id){
        if(!taskRepository.existsById(id)){
            throw new TaskException("Id não encontrado" + id);
        }

        taskRepository.deleteById(id);
    }




}
