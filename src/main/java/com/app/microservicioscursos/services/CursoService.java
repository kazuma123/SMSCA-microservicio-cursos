package com.app.microservicioscursos.services;

import com.app.commonsmicroservicios.services.CommonService;
import com.app.microservicioscursos.models.Curso;

public interface CursoService extends CommonService<Curso> {
	public Curso findCursoByAlumnoId(Long id);
	public Iterable<Long> obtenerExamenesIdsConRespuestasAlumno(Long alumnoId);
}
