package com.app.microservicioscursos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.microservicioscursos.models.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long>{

	@Query("select c from Curso c join fetch c.cursoAlumnos a where a.id=?1")
	public Curso findCursoByAlumnoId(Long id);
}
