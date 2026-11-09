package pw.parcial.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pw.parcial.model.Usuario;
import pw.parcial.repository.UsuarioRepo;

@Component
public class Datos implements CommandLineRunner {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private PasswordEncoder encoder;

    // solo los usuarios de cada rol, los medicos los registra el admin
    @Override
    public void run(String... args) {
        if (usuarioRepo.count() == 0) {
            usuarioRepo.save(new Usuario(null, "admin", encoder.encode("admin123"), "ADMIN"));
            usuarioRepo.save(new Usuario(null, "medico", encoder.encode("medico123"), "MEDICO"));
            usuarioRepo.save(new Usuario(null, "paciente", encoder.encode("paciente123"), "PACIENTE"));
        }
    }
}
