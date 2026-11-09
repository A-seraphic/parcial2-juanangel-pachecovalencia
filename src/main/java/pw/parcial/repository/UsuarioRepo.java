package pw.parcial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pw.parcial.model.Usuario;

public interface UsuarioRepo extends JpaRepository<Usuario, Long> {

    Usuario findByUsername(String username);
}
