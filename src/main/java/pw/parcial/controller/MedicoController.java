package pw.parcial.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pw.parcial.model.Medico;
import pw.parcial.model.Usuario;
import pw.parcial.repository.MedicoRepo;
import pw.parcial.repository.UsuarioRepo;

@RestController
@RequestMapping("/api/medicos")
@Tag(name = "Medicos", description = "Gestion de medicos (lista desplegable de las consultas)")
public class MedicoController {

    @Autowired
    private MedicoRepo repo;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private PasswordEncoder encoder;

    @GetMapping
    @Operation(summary = "Listar medicos", description = "Devuelve todos los medicos. Lo usa cualquier usuario con sesion (llena el select del formulario).")
    public List<Medico> lista() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar medico", description = "Devuelve un medico por su id. Cualquier usuario con sesion.")
    public Medico buscar(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    @PostMapping
    @Operation(summary = "Crear medico", description = "Solo ADMIN. Crea el medico y su usuario de acceso con la clave dada (si va vacia queda medico123). Ej: {\"nombre\":\"Ana Gomez\",\"usuario\":\"medico2\",\"clave\":\"clave123\"}")
    public Medico crear(@Valid @RequestBody Medico m) {
        Medico res = repo.save(m);
        guardarClave(m);
        return res;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar medico", description = "Solo ADMIN. Si la clave va vacia no se cambia. Ej: {\"nombre\":\"Ana Gomez Diaz\",\"usuario\":\"medico2\",\"clave\":\"\"}")
    public Medico editar(@PathVariable Long id, @Valid @RequestBody Medico m) {
        m.setId(id);
        Medico res = repo.save(m);
        guardarClave(m);
        return res;
    }

    // crea o actualiza el usuario con el que entra el medico
    private void guardarClave(Medico m) {
        Usuario u = usuarioRepo.findByUsername(m.getUsuario());
        if (u == null) u = new Usuario(null, m.getUsuario(), encoder.encode("medico123"), "MEDICO");
        if (!u.getRol().equals("MEDICO")) return;
        if (m.getClave() != null && !m.getClave().isEmpty()) u.setPassword(encoder.encode(m.getClave()));
        usuarioRepo.save(u);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar medico", description = "Solo ADMIN. No deja borrar un medico que tenga consultas (400).")
    public void eliminar(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
