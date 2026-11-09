package pw.parcial.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El nombre del medico es obligatorio. Ej: Ana Gomez")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ][A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,39}$", message = "El nombre del medico solo admite letras y espacios (3 a 40). Ej: Ana Gomez")
    private String nombre;

    // usuario con el que el medico entra al sistema
    @NotNull(message = "El usuario del medico es obligatorio. Ej: medico2")
    @Pattern(regexp = "^[a-z][a-z0-9]{2,19}$", message = "El usuario debe empezar por letra minuscula y tener de 3 a 20 letras o numeros, sin espacios. Ej: medico2")
    @Column(unique = true)
    private String usuario;

    // clave de acceso, no se guarda en esta tabla sino en usuario
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Pattern(regexp = "^$|^\\S{6,20}$", message = "La contraseña debe tener de 6 a 20 caracteres sin espacios. Ej: clave123")
    private String clave;
}
