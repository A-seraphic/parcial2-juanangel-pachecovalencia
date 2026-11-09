package pw.parcial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.parcial.model.Medico;

public interface MedicoRepo extends JpaRepository<Medico, Long> {
}
