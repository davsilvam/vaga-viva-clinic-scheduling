package io.github.precavidos.clinic.patient;

import org.springframework.data.jpa.repository.JpaRepository;

// Ainda não ligado ao controller: falta transformar Patient em entidade.
interface PatientRepository extends JpaRepository<PatientEntity, Long> {
}
