package pw.parcial.config;

import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import pw.parcial.model.Usuario;
import pw.parcial.repository.UsuarioRepo;

@Configuration
public class Seguridad {

    @Autowired
    private UsuarioRepo repo;

    @Bean
    public SecurityFilterChain filtro(HttpSecurity http) throws Exception {
        http.csrf(c -> c.disable())
            .authorizeHttpRequests(a -> a
                .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                .requestMatchers("/login.jsp", "/sinpermiso.jsp", "/estilos.css").permitAll()
                .requestMatchers("/admin.jsp").hasRole("ADMIN")
                .requestMatchers("/medico.jsp").hasRole("MEDICO")
                .requestMatchers("/paciente.jsp").hasRole("PACIENTE")
                .requestMatchers(HttpMethod.GET, "/api/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/consultas/*/horario").hasRole("PACIENTE")
                .requestMatchers("/api/**").hasRole("ADMIN")
                .anyRequest().authenticated())
            .formLogin(f -> f
                .loginPage("/login.jsp")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login.jsp?error")
                .permitAll())
            .logout(l -> l.logoutSuccessUrl("/login.jsp"))
            // la api responde 401/403, las paginas van al login o a sinpermiso.jsp
            // (no se usa accessDeniedPage porque un jsp no acepta PUT ni DELETE y daba 405)
            .exceptionHandling(e -> e
                .accessDeniedHandler((req, res, ex) -> {
                    if (req.getRequestURI().startsWith("/api/")) res.sendError(403);
                    else {
                        res.setStatus(403);
                        req.getRequestDispatcher("/sinpermiso.jsp").forward(req, res);
                    }
                })
                .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                    PathPatternRequestMatcher.withDefaults().matcher("/api/**")));
        return http.build();
    }

    @Bean
    public UserDetailsService usuarios() {
        return username -> {
            Usuario u = repo.findByUsername(username);
            if (u == null) throw new UsernameNotFoundException("no existe");
            return User.withUsername(u.getUsername()).password(u.getPassword()).roles(u.getRol()).build();
        };
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}
