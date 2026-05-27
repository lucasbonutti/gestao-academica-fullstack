package com.controle.monitoria_api.config;

import com.controle.monitoria_api.exceptions.ErroResponseBuilder;
import com.controle.monitoria_api.security.SecurityFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final SecurityFilter securityFilter;
    private final ErroResponseBuilder erroResponseBuilder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, e) ->
                                erroResponseBuilder.write(response, HttpStatus.FORBIDDEN,
                                        "Acesso negado!",
                                        "Você não tem permissão para acessar este recurso!",
                                        request.getRequestURI())
                        )
                        .authenticationEntryPoint((request, response, e) ->
                                erroResponseBuilder.write(response, HttpStatus.UNAUTHORIZED,
                                        "Erro de autenticação!",
                                        "Credenciais inválidas ou token não informado!",
                                        request.getRequestURI())
                        )
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers(HttpMethod.POST, "/login").permitAll()
                            .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()

                            .requestMatchers(HttpMethod.POST, "/monitorias").hasAnyRole("ADMIN", "PROFESSOR")
                            .requestMatchers(HttpMethod.POST, "/formacoes").hasAnyRole("ADMIN", "PROFESSOR")
                            .requestMatchers(HttpMethod.POST, "/relatorios-monitoria").hasAnyRole("ADMIN", "PROFESSOR")
                            .requestMatchers(HttpMethod.PUT, "/formacoes").hasAnyRole("ADMIN", "PROFESSOR")
                            .requestMatchers(HttpMethod.PATCH, "/monitorias/*/finalizar").hasAnyRole("ADMIN", "PROFESSOR")

                            .requestMatchers(HttpMethod.GET, "/**").hasAnyRole("ADMIN", "PROFESSOR")

                            .requestMatchers(HttpMethod.POST, "/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PUT, "/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
                            .requestMatchers(HttpMethod.PATCH, "/**").hasRole("ADMIN")
                            .anyRequest().authenticated();
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
