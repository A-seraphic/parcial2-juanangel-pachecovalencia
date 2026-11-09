package pw.parcial.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pw.parcial.model.Consulta;
import pw.parcial.repository.ConsultaRepo;

@RestController
@RequestMapping("/api/consultas")
@Tag(name = "Consultas", description = "Consultas medicas: el admin las registra, el medico ve las suyas y el paciente cambia el horario de las suyas")
public class ConsultaController {

    @Autowired
    private ConsultaRepo repo;

    @GetMapping
    @Operation(summary = "Listar consultas", description = "ADMIN ve todas, MEDICO solo las asignadas a el y PACIENTE solo las suyas.")
    public List<Consulta> lista(HttpServletRequest req) {
        if (req.isUserInRole("MEDICO")) return repo.findByMedicoUsuario(req.getRemoteUser());
        if (req.isUserInRole("PACIENTE")) return repo.findByPacienteIgnoreCase(req.getRemoteUser());
        return repo.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar consulta", description = "Devuelve una consulta por id si el usuario la puede ver, si no responde 403.")
    public ResponseEntity<Consulta> buscar(@PathVariable Long id, HttpServletRequest req) {
        Consulta c = lista(req).stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
        return c == null ? ResponseEntity.status(403).build() : ResponseEntity.ok(c);
    }

    @PostMapping
    @Operation(summary = "Registrar consulta", description = "Solo ADMIN. Ej: {\"paciente\":\"paciente\",\"motivo\":\"Dolor de cabeza\",\"consultorio\":101,\"horaInicio\":\"08:00\",\"horaFin\":\"08:30\",\"medico\":{\"id\":1}}")
    public Consulta crear(@Valid @RequestBody Consulta c) {
        return repo.save(c);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar consulta", description = "Solo ADMIN. Mismo cuerpo que registrar consulta.")
    public Consulta editar(@PathVariable Long id, @Valid @RequestBody Consulta c) {
        c.setId(id);
        return repo.save(c);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar consulta", description = "Solo ADMIN.")
    public void eliminar(@PathVariable Long id) {
        repo.deleteById(id);
    }

    @PutMapping("/{id}/horario")
    @Operation(summary = "Cambiar horario", description = "Solo PACIENTE y solo en sus propias citas. Valida HH:mm y que el fin sea mayor que el inicio (400). Ej: {\"horaInicio\":\"09:00\",\"horaFin\":\"09:30\"}")
    public ResponseEntity<Object> horario(@PathVariable Long id, @RequestBody Consulta h, HttpServletRequest req) {
        Consulta c = repo.findById(id).orElse(null);
        if (c == null || !c.getPaciente().equalsIgnoreCase(req.getRemoteUser())) return ResponseEntity.status(403).body("- Esta cita no es suya\n");
        String regex = "^([01][0-9]|2[0-3]):[0-5][0-9]$";
        if (h.getHoraInicio() == null || !h.getHoraInicio().matches(regex)) return ResponseEntity.badRequest().body("- La hora de inicio debe tener formato HH:mm entre 00:00 y 23:59. Ej: 08:00\n");
        if (h.getHoraFin() == null || !h.getHoraFin().matches(regex)) return ResponseEntity.badRequest().body("- La hora de fin debe tener formato HH:mm entre 00:00 y 23:59. Ej: 08:30\n");
        if (!h.isHorarioOk()) return ResponseEntity.badRequest().body("- La hora de fin debe ser mayor que la de inicio. Ej: 08:00 a 08:30\n");
        c.setHoraInicio(h.getHoraInicio());
        c.setHoraFin(h.getHoraFin());
        return ResponseEntity.ok(repo.save(c));
    }
}
