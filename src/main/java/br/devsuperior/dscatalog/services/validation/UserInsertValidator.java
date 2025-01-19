package br.devsuperior.dscatalog.services.validation;


import br.devsuperior.dscatalog.DTO.UserInsertDTO;
import br.devsuperior.dscatalog.entities.User;
import br.devsuperior.dscatalog.exceptions.fieldMessage;
import br.devsuperior.dscatalog.repositories.UserRepository;
import br.devsuperior.dscatalog.services.UserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class UserInsertValidator implements ConstraintValidator< UserInsertValid, UserInsertDTO> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UserInsertValid ann) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

        List<fieldMessage> list = new ArrayList<>();

        User user = userRepository.findByEmail(dto.getEmail());
        if (user != null) {
            list.add(new fieldMessage("email", "Email already in use"));
        }


        for (fieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}