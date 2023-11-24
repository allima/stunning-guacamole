package com.codility.dto;

import com.codility.domain.TaskStatus;

public class AtualizarTarefaDto {

	private Long id;
	private String titulo;
	private String descricao;
	private TaskStatus status;
	private UsuarioDto usuario;

	public AtualizarTarefaDto(Long id, String titulo, String descricao, TaskStatus status, UsuarioDto usuario) {
		this.id = id;
		this.titulo = titulo;
		this.descricao = descricao;
		this.status = status;
		this.usuario = usuario;
	}

	public AtualizarTarefaDto() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public UsuarioDto getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDto usuario) {
		this.usuario = usuario;
	}

}
