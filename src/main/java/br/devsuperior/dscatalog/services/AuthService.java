package br.devsuperior.dscatalog.services;


import br.devsuperior.dscatalog.DTO.EmailDTO;
import br.devsuperior.dscatalog.DTO.NewPasswordDTO;
import br.devsuperior.dscatalog.entities.PasswordRecover;
import br.devsuperior.dscatalog.entities.User;
import br.devsuperior.dscatalog.exceptions.NotFoundException;
import br.devsuperior.dscatalog.repositories.PasswordRecoverRepository;
import br.devsuperior.dscatalog.repositories.UserRepository;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    @Value("${email.password-recover.token.minutes}")
    private Long tokenMinutes;

    @Autowired
    PasswordRecoverRepository passwordRecoverRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Value("${email.password-recover.uri}")
        private String recoverUri;

    @Autowired
    private EmailService emailService;

    @Transactional
    public void createRecoverToken(@Valid EmailDTO body) {

        User user = userRepository.findByEmail(body.getEmail());
        if (user == null) {
            throw new NotFoundException("User not found");
        }

        String token = UUID.randomUUID().toString();

        PasswordRecover entity = new PasswordRecover();
        entity.setEmail(body.getEmail());
        entity.setToken(token);
        entity.setExpiryDate(Instant.now().plusSeconds(tokenMinutes * 60L));
        entity = passwordRecoverRepository.save(entity);

        String bodyMessage = "Acesse o link para definir uma nova senha \n\n" +
                recoverUri + token;

        emailService.sendEmail(body.getEmail(), "Recuperação de Senha" , bodyMessage);
    }


    @Transactional
    public void saveNewPassword(@Valid NewPasswordDTO body) {
        List<PasswordRecover> result = passwordRecoverRepository.searchValidTokens(body.getToken(), Instant.now());
        if (result.size() == 0) {
            throw new NotFoundException("Token not found");
        }
        User user =userRepository.findByEmail( result.get(0).getEmail());
        user.setPassword(passwordEncoder.encode( body.getPassword()));
        userRepository.save(user);

    }




}
