package pw.parcial.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Data
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // el nombre del paciente es su usuario, asi el paciente ve sus citas
    @NotNull(message = "El nombre del paciente es obligatorio. Ej: paciente")
    @Pattern(regexp = "^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñ][A-Za-z0-9ÁÉÍÓÚáéíóúÑñ ]{0,39}$", message = "El nombre del paciente solo admite letras, numeros y espacios (maximo 40). Ej: paciente")
    private String paciente;

    @NotNull(message = "El motivo es obligatorio. Ej: Dolor de cabeza")
    @Pattern(regexp = "^[A-Za-z0-9ÁÉÍÓÚáéíóúÑñ][A-Za-z0-9ÁÉÍÓÚáéíóúÑñ ,.]{2,99}$", message = "El motivo debe tener de 3 a 100 caracteres (letras, numeros, comas o puntos). Ej: Dolor de cabeza")
    private String motivo;

    @NotNull(message = "El numero de consultorio es obligatorio. Ej: 101")
    @Min(value = 1, message = "El numero de consultorio debe ser 1 o mayor. Ej: 101")
    @Max(value = 999, message = "El numero de consultorio no puede pasar de 999. Ej: 101")
    private Integer consultorio;

    @NotNull(message = "La hora de inicio es obligatoria. Ej: 08:00")
    @Pattern(regexp = "^([01][0-9]|2[0-3]):[0-5][0-9]$", message = "La hora de inicio debe tener formato HH:mm entre 00:00 y 23:59. Ej: 08:00")
    private String horaInicio;

    @NotNull(message = "La hora de fin es obligatoria. Ej: 08:30")
    @Pattern(regexp = "^([01][0-9]|2[0-3]):[0-5][0-9]$", message = "La hora de fin debe tener formato HH:mm entre 00:00 y 23:59. Ej: 08:30")
    private String horaFin;

    @NotNull(message = "Seleccione un medico de la lista")
    @ManyToOne
    private Medico medico;

    @JsonIgnore
    @AssertTrue(message = "La hora de fin debe ser mayor que la de inicio. Ej: 08:00 a 08:30")
    public boolean isHorarioOk() {
        // si alguna hora esta mal ya sale su propio mensaje
        String r = "^([01][0-9]|2[0-3]):[0-5][0-9]$";
        if (horaInicio == null || horaFin == null || !horaInicio.matches(r) || !horaFin.matches(r)) return true;
        return horaFin.compareTo(horaInicio) > 0;
    }
}
