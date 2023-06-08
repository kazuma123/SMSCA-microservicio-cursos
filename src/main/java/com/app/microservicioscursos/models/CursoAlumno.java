package com.app.microservicioscursos.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@Entity
@Table(name = "curso_alumno")
public class CursoAlumno {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "alumno_id", unique = true)
	private Long alumnoId;
	
	@JsonIgnoreProperties(value = {"cursoAlumnos"})
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "curso_id")
	private Curso curso;	
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(!(obj instanceof CursoAlumno)) {
			return false;
		}
		CursoAlumno a = (CursoAlumno) obj;
		return this.alumnoId != null && this.alumnoId.equals(a.getAlumnoId());
	}
}
