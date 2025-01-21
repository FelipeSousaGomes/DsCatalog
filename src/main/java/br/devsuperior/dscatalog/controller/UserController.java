package br.devsuperior.dscatalog.controller;

import br.devsuperior.dscatalog.DTO.UserDTO;
import br.devsuperior.dscatalog.DTO.UserInsertDTO;
import br.devsuperior.dscatalog.DTO.UserUpdatetDTO;
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
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
   private UserService service;

    @PreAuthorize("hasRole('ROLE_ADMIN')")

    @GetMapping
    public ResponseEntity<Page<UserDTO>>findAll(Pageable pageable){

        Page<UserDTO> listPage = service.findAllPaged(pageable);

        return ResponseEntity.ok().body(listPage);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")

    @GetMapping("/{id}" )
    public ResponseEntity<UserDTO>findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findyById(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")

    @PostMapping
    public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO UserDTO){
            UserDTO userDTO = service.insert(UserDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(UserDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(userDTO);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}" )
    public ResponseEntity<UserDTO> update(@Valid @RequestBody UserUpdatetDTO dto, @PathVariable Long id ){
        return ResponseEntity.ok().body(service.udpate(dto,id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> delete(@PathVariable Long id ){
        service.delete(id);
       return ResponseEntity.noContent().build();
    }

}
