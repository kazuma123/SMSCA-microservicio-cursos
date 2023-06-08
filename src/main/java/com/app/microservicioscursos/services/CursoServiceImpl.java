package com.app.microservicioscursos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.commonsmicroservicios.services.CommonServiceImpl;
import com.app.microservicioscursos.clients.RespuestaFeingClient;
import com.app.microservicioscursos.models.Curso;
import com.app.microservicioscursos.repository.CursoRepository;

@Service
public class CursoServiceImpl extends CommonServiceImpl<Curso, CursoRepository> implements CursoService {

	@Autowired
	private RespuestaFeingClient feingClient;
	
	@Override
	@Transactional(readOnly = true)
	public Curso findCursoByAlumnoId(Long id) {
		// TODO Auto-generated method stub
		return repository.findCursoByAlumnoId(id);
	}

	@Override
	public Iterable<Long> obtenerExamenesIdsConRespuestasAlumno(Long alumnoId) {
		// TODO Auto-generated method stub
		return feingClient.obtenerExamenesIdsConRespuestasAlumno(alumnoId);
	}

}
