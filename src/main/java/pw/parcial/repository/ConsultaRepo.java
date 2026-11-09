package pw.parcial.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pw.parcial.model.Consulta;

public interface ConsultaRepo extends JpaRepository<Consulta, Long> {

    List<Consulta> findByMedicoUsuario(String usuario);

    List<Consulta> findByPacienteIgnoreCase(String paciente);
}
