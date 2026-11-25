package io.github.precavidos.clinic.patient;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

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

	private final Map<Long, Patient> patients = new ConcurrentHashMap<>();

	private final AtomicLong ids = new AtomicLong();

	@PostMapping
	ResponseEntity<Patient> create(@RequestBody Patient patient) {
		Patient saved = patient.withId(ids.incrementAndGet());
		patients.put(saved.id(), saved);
		return ResponseEntity.created(URI.create("/patients/" + saved.id())).body(saved);
	}

	@GetMapping("/{id}")
	ResponseEntity<Patient> find(@PathVariable Long id) {
		Patient patient = patients.get(id);
		return patient == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(patient);
	}

	@PutMapping("/{id}")
	ResponseEntity<Patient> update(@PathVariable Long id, @RequestBody Patient patient) {
		if (!patients.containsKey(id)) {
			return ResponseEntity.notFound().build();
		}
		Patient updated = patient.withId(id);
		patients.put(id, updated);
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	ResponseEntity<Void> delete(@PathVariable Long id) {
		return patients.remove(id) == null ? ResponseEntity.notFound().build() : ResponseEntity.noContent().build();
	}

}
