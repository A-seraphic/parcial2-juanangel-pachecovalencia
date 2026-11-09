package pw.parcial.config;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Errores {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> validacion(MethodArgumentNotValidException e) {
        String res = "";
        for (FieldError f : e.getBindingResult().getFieldErrors()) res += "- " + f.getDefaultMessage() + "\n";
        return ResponseEntity.badRequest().body(res);
    }

    // por ejemplo borrar un medico que tiene consultas
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> datos(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().body("- No se pudo guardar o eliminar: revise que el medico exista, que el usuario no este repetido y que el medico no tenga consultas asignadas\n");
    }
}
