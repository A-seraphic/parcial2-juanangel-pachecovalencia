package pw.parcial.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger {

    @Bean
    public OpenAPI api() {
        return new OpenAPI().info(new Info()
            .title("Consultas Medicas - Documentacion de servicios")
            .version("1.0")
            .description("Primero hay que iniciar sesion en /login.jsp (admin/admin123, medico/medico123 o paciente/paciente123) y luego abrir esta pagina."));
    }

    // agrega 401 y 403 a todos los servicios
    @Bean
    public OpenApiCustomizer respuestas() {
        return api -> api.getPaths().values().forEach(p -> p.readOperations().forEach(o -> o.getResponses()
            .addApiResponse("401", new ApiResponse().description("Unauthorized: no ha iniciado sesion"))
            .addApiResponse("403", new ApiResponse().description("Forbidden: su rol no tiene permiso para este servicio"))));
    }
}
