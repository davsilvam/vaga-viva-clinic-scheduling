package io.github.precavidos.clinic.patient;

import java.time.LocalDate;

public record Patient(Long id, String name, String email, String phone, LocalDate birthDate) {

	Patient withId(Long newId) {
		return new Patient(newId, name, email, phone, birthDate);
	}

}
