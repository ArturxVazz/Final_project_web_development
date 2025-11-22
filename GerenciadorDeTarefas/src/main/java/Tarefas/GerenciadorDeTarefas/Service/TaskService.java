package Tarefas.GerenciadorDeTarefas.Service;

import Tarefas.GerenciadorDeTarefas.Dto.TaskDto;
import Tarefas.GerenciadorDeTarefas.Entity.TaskEntity;
import Tarefas.GerenciadorDeTarefas.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public TaskDto adicionarTarefa (TaskDto taskDto){
        TaskEntity taskEntity = new TaskEntity();

        taskEntity.setTitulo(taskDto.getTitulo());
        taskEntity.setResponsavel(taskDto.getResponsavel());
        taskEntity.setDetalhamento(taskDto.getDetalhamento());
        taskEntity.setDataTermino(taskDto.getDataTermino());

        TaskEntity newTask = taskRepository.save(taskEntity);

        taskDto.setId(newTask.getId());

        return taskDto;
    }

    public TaskDto updateTask (TaskDto taskDto){
        return null;
    }

    public TaskDto deleteTask (TaskDto taskDto){
        return null;
    }




}
