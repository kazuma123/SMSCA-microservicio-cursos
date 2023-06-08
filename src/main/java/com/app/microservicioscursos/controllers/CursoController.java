package com.app.microservicioscursos.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.commonsalumnos.models.Alumno;
import com.app.commonsexamenes.models.Examen;
import com.app.commonsmicroservicios.controllers.CommonController;
import com.app.microservicioscursos.models.Curso;
import com.app.microservicioscursos.models.CursoAlumno;
import com.app.microservicioscursos.services.CursoService;

@RestController
public class CursoController extends CommonController<Curso, CursoService> {

	@Value("${config.balanceador.test}")
	private String balanceadorTest;
	
	@GetMapping("/")
	@Override
	public ResponseEntity<?> listar(){
		List<Curso> cursos = ((List<Curso>) service.findAll()).stream().map(c -> {
			c.getCursoAlumnos().forEach(ca -> {
				Alumno alumno = new Alumno();
				alumno.setId(ca.getAlumnoId());
				c.addAlumno(alumno);
			});
			return c;
		}).collect(Collectors.toList());
		return ResponseEntity.ok().body(cursos);
	}
	
	@GetMapping("/balanceador-test")
	public ResponseEntity<?> balanceadorTest() {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("balanceador", balanceadorTest);
		response.put("cursos", service.findAll());
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@Valid @RequestBody Curso curso, BindingResult result, @PathVariable("id") Long id) {
		
		if(result.hasErrors()) {
			return validar(result);
		}
		
		Optional<Curso> o = this.service.findbyId(id);

		if (!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		Curso cursodb = o.get();
		cursodb.setNombre(curso.getNombre());

		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(cursodb));

	}

	@PutMapping("/{id}/asignar-alumnos")
	public ResponseEntity<?> asignarAlumnos(@RequestBody List<Alumno> alumnos, @PathVariable("id") Long id) {
		Optional<Curso> o = this.service.findbyId(id);

		if (!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso cursodb = o.get();
		alumnos.forEach(a -> {
			CursoAlumno cursoAlumno = new CursoAlumno();
			cursoAlumno.setAlumnoId(a.getId());
			cursoAlumno.setCurso(cursodb);
			cursodb.addCursoAlumno(cursoAlumno);
		});
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(cursodb));
	}
	
	@PutMapping("/{id}/eliminar-alumno")
	public ResponseEntity<?> eliminarAlumno(@RequestBody Alumno alumno, @PathVariable("id") Long id) {
		Optional<Curso> o = this.service.findbyId(id);

		if (!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso cursodb = o.get();
		CursoAlumno cursoAlumno = new CursoAlumno();
		cursoAlumno.setAlumnoId(alumno.getId());
		cursodb.removeCursoAlumno(cursoAlumno);
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(cursodb));
	}
	
	@GetMapping("/alumno/{id}")
	public ResponseEntity<?> buscarAlumnoById(@PathVariable("id") Long id){
		Curso curso = service.findCursoByAlumnoId(id);
		
		if(curso != null) {
			List<Long> examenesIds = (List<Long>) service.obtenerExamenesIdsConRespuestasAlumno(id);
			List<Examen> examenes = curso.getExamenes().stream().map(examen ->{
				if(examenesIds.contains(examen.getId())) {
					examen.setRespondido(true);
				}
				return examen;
			}).collect(Collectors.toList());
			
			curso.setExamenes(examenes);
			
		}
		
		return ResponseEntity.ok(curso);
	}
	
	@PutMapping("/{id}/asignar-examenes")
	public ResponseEntity<?> asignarExamenes(@RequestBody List<Examen> examenes, @PathVariable("id") Long id) {
		Optional<Curso> o = this.service.findbyId(id);

		if (!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso cursodb = o.get();
		examenes.forEach(e -> {
			cursodb.addExamen(e);
		});
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(cursodb));
	}
	
	@PutMapping("/{id}/eliminar-examen")
	public ResponseEntity<?> eliminarExamen(@RequestBody Examen examen, @PathVariable("id") Long id) {
		Optional<Curso> o = this.service.findbyId(id);

		if (!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso cursodb = o.get();
		cursodb.removeExamen(examen);
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(cursodb));
	}
	
	
}
