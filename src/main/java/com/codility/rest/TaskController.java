package com.codility.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.codility.domain.Tarefa;
import com.codility.domain.TaskStatus;
import com.codility.domain.Usuario;
import com.codility.dto.AtualizarTarefaDto;
import com.codility.dto.CriarTarefaDto;

@RestController
public class TaskController {

	private List<Tarefa> tarefasDb = new ArrayList<>();
	private Long geradorId = 1L;

	@PostMapping("/tarefa")
	public ResponseEntity<Tarefa> createTarefa(@RequestBody CriarTarefaDto criarTarefaDto) {

		if (criarTarefaDto.getTitulo() != null || criarTarefaDto.getUsuario() != null
				|| criarTarefaDto.getUsuario().getNome() != null || criarTarefaDto.getUsuario().getTime() != null) {
			Tarefa tarefa = new Tarefa();
			tarefa.setStatus(TaskStatus.TODO);
			tarefa.setId(geradorId++);
			tarefa.setDescricao(criarTarefaDto.getDescricao());
			tarefa.setTitulo(criarTarefaDto.getTitulo());
			Usuario novoUsuario = new Usuario(criarTarefaDto.getUsuario().getNome(),
					criarTarefaDto.getUsuario().getTime());
			tarefa.setUsuario(novoUsuario);
			tarefasDb.add(tarefa);
			return ResponseEntity.ok().body(tarefa);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	@PutMapping("/tarefa/{id}")
	public ResponseEntity<?> updateTarefa(@PathVariable long id, @RequestBody AtualizarTarefaDto tarefaDto) {

		Tarefa tarefaAtualizada = null;

		Optional<Tarefa> updateTarefa = tarefasDb.stream().filter(t -> t.getId().equals(id)).findFirst();

		if (updateTarefa.isPresent()) {
			tarefaAtualizada = updateTarefa.get();
			tarefaAtualizada.setTitulo(tarefaDto.getTitulo());
			tarefaAtualizada.setDescricao(tarefaDto.getDescricao());
			Usuario novoUsuario = new Usuario(tarefaDto.getUsuario().getNome(), tarefaDto.getUsuario().getTime());
			tarefaAtualizada.setUsuario(novoUsuario);
			try {
				tarefaAtualizada.setStatus(validaStatus(tarefaDto.getStatus(), tarefaAtualizada.getStatus()));
			} catch (Exception e) {
				return ResponseEntity.badRequest().body("Status possíveis para a tarefa: " + e.getMessage());
			}
		} else {
			return ResponseEntity.noContent().build();
		}
		tarefasDb.set((int) (tarefaAtualizada.getId() - 1), tarefaAtualizada);
		return ResponseEntity.ok().body(tarefaAtualizada);
	}

	private TaskStatus validaStatus(TaskStatus statusNovo, TaskStatus statusAntigo) throws Exception {
		TaskStatus statusAtualizado = statusAntigo;
		if (TaskStatus.DONE.equals(statusAntigo)) {
			statusAtualizado = TaskStatus.DONE;
		} else if (TaskStatus.TODO.equals(statusAntigo) && TaskStatus.DOING.equals(statusNovo)) {
			statusAtualizado = TaskStatus.DOING;
		} else if (TaskStatus.DOING.equals(statusAntigo) && TaskStatus.DONE.equals(statusNovo)) {
			statusAtualizado = TaskStatus.DONE;
		} else if (TaskStatus.DOING.equals(statusAntigo) && TaskStatus.TODO.equals(statusNovo)) {
			statusAtualizado = TaskStatus.TODO;
		} else if (TaskStatus.TODO.equals(statusAntigo) && TaskStatus.DONE.equals(statusNovo)) {
			throw new Exception("DOING");
		}

		return statusAtualizado;
	}

	@GetMapping("/tarefa/{status}")
	public ResponseEntity<List<Tarefa>> getTarefasByStatus(@PathVariable("status") String status) {

		if (TaskStatus.TODO.toString().equals(status) || TaskStatus.DOING.toString().equals(status)
				|| TaskStatus.DONE.toString().equals(status)) {
			List<Tarefa> tarefas = tarefasDb.stream().filter(t -> t.getStatus().toString().equals(status.toUpperCase()))
					.collect(Collectors.toList());

			if (tarefas != null && !tarefas.isEmpty()) {
				return ResponseEntity.ok().body(tarefas);
			} else {
				return ResponseEntity.noContent().build();
			}
		} else {
			return ResponseEntity.badRequest().build();
		}

	}
}
