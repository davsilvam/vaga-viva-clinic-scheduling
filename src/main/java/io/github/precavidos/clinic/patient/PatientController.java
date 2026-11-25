package io.github.precavidos.clinic.patient;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients")
class PatientController {

	private final PatientRepository patients;

	PatientController(PatientRepository patients) {
		this.patients = patients;
	}

	@PostMapping
	ResponseEntity<Patient> create(@RequestBody Patient patient) {
		PatientEntity saved = patients.save(toEntity(patient, null));
		return ResponseEntity.created(URI.create("/patients/" + saved.id)).body(toPatient(saved));
	}

	@GetMapping("/{id}")
	ResponseEntity<Patient> find(@PathVariable Long id) {
		return patients.findById(id).map(PatientController::toPatient).map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	ResponseEntity<Patient> update(@PathVariable Long id, @RequestBody Patient patient) {
		if (!patients.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(toPatient(patients.save(toEntity(patient, id))));
	}

	// TODO: remover do banco
	@DeleteMapping("/{id}")
	ResponseEntity<Void> delete(@PathVariable Long id) {
		return ResponseEntity.noContent().build();
	}

	private static PatientEntity toEntity(Patient patient, Long id) {
		PatientEntity entity = new PatientEntity();
		entity.id = id;
		entity.name = patient.name();
		entity.email = patient.email();
		entity.phone = patient.phone();
		entity.birthDate = patient.birthDate();
		return entity;
	}

	private static Patient toPatient(PatientEntity entity) {
		return new Patient(entity.id, entity.name, entity.email, entity.phone, entity.birthDate);
	}

}
