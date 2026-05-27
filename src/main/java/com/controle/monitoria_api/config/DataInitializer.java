package com.controle.monitoria_api.config;

import com.controle.monitoria_api.model.Usuario;
import com.controle.monitoria_api.model.enums.PerfilUsuario;
import com.controle.monitoria_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.findByLoginIgnoreCase("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setLogin("admin");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.setPerfil(PerfilUsuario.ADMIN);
            admin.setProfessor(null);
            usuarioRepository.save(admin);
            System.out.println("Usuário (ADMIN) criado com sucesso!");
        }

        if (usuarioRepository.findByLoginIgnoreCase("professor").isEmpty()) {
            Usuario professor = new Usuario();
            professor.setLogin("professor");
            professor.setSenha(passwordEncoder.encode("prof123"));
            professor.setPerfil(PerfilUsuario.PROFESSOR);
            usuarioRepository.save(professor);
            System.out.println("Usuário (PROFESSOR) criado!");
        }
    }
}
