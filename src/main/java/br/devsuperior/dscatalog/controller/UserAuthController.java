package br.devsuperior.dscatalog.controller;

import br.devsuperior.dscatalog.DTO.EmailDTO;
import br.devsuperior.dscatalog.DTO.UserDTO;
import br.devsuperior.dscatalog.DTO.UserInsertDTO;
import br.devsuperior.dscatalog.DTO.UserUpdatetDTO;
import br.devsuperior.dscatalog.services.AuthService;
import br.devsuperior.dscatalog.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/auth")
public class UserAuthController {

    @Autowired
   private AuthService service;


  @PostMapping(value = "/recorver-token")
    public ResponseEntity<Void> createRecoverToken(@Valid @RequestBody EmailDTO body){

        service.createRecoverToken(body);

        return ResponseEntity.noContent().build();
    }


}
