package br.devsuperior.dscatalog.DTO;

import br.devsuperior.dscatalog.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;
import java.util.HashSet;


public class UserDTO {

    private Long id;
    @NotBlank(message = "Campo obrigátorio")
    private String firstName;
    private String lastName;
    @Email(message = "Favor entrar com email válido")
    private String email;
    private Set<RoleDTO> roles = new HashSet<>();

    public UserDTO() {}

    public UserDTO(Long id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;

    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        user.getRoles().forEach(role -> roles.add(new RoleDTO(role)));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }
}
