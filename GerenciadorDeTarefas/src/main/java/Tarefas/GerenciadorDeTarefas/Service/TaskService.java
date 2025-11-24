package Tarefas.GerenciadorDeTarefas.Service;

import Tarefas.GerenciadorDeTarefas.Dto.TaskDto;
import Tarefas.GerenciadorDeTarefas.Entity.TaskEntity;
import Tarefas.GerenciadorDeTarefas.Exception.TaskException;
import Tarefas.GerenciadorDeTarefas.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

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

    public List<TaskDto> listarTodas() {
        // 1. Busca tudo no banco (retorna Entities)
        List<TaskEntity> listaEntities = taskRepository.findAll();

        // 2. Cria uma lista vazia para guardar os DTOs
        List<TaskDto> listaDtos = new ArrayList<>();

        // 3. Converte cada Entity para DTO (Manual, mas didático)
        for (TaskEntity entity : listaEntities) {
            TaskDto dto = new TaskDto();
            dto.setId(entity.getId());
            dto.setTitulo(entity.getTitulo());
            dto.setResponsavel(entity.getResponsavel());
            dto.setDetalhamento(entity.getDetalhamento());
            dto.setDataTermino(entity.getDataTermino());

            listaDtos.add(dto);
        }

        return listaDtos;
    }

}
