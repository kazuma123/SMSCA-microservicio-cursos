package com.app.microservicioscursos.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservicio-respuestas")
public interface RespuestaFeingClient {
	
	@GetMapping("/alumno/{id}/exameenes-respondidos")
	public Iterable<Long> obtenerExamenesIdsConRespuestasAlumno(@PathVariable("id") Long alumnoId);
}
