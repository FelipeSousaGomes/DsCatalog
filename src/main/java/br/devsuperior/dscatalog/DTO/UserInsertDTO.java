package br.devsuperior.dscatalog.DTO;

import br.devsuperior.dscatalog.services.validation.UserInsertValid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@UserInsertValid
public class UserInsertDTO extends UserDTO {

    @NotBlank(message = "Campo obrigátorio")
    @Size(min = 8 , message = "Deve ter no mínimo 8 carateres")
    private String password;


    public UserInsertDTO() {
        super();
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
