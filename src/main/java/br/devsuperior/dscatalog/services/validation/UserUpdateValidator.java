package br.devsuperior.dscatalog.services.validation;



import br.devsuperior.dscatalog.DTO.UserUpdatetDTO;
import br.devsuperior.dscatalog.entities.User;
import br.devsuperior.dscatalog.exceptions.fieldMessage;
import br.devsuperior.dscatalog.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserUpdateValidator implements ConstraintValidator< UserUpdateValid, UserUpdatetDTO> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UserUpdateValid ann) {
    }

    @Override
    public boolean isValid(UserUpdatetDTO dto, ConstraintValidatorContext context) {

        var uri = (Map<String,String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        long userId =Long.parseLong(uri.get("id"));
        List<fieldMessage> list = new ArrayList<>();

        User user = userRepository.findByEmail(dto.getEmail());
        if (user != null && userId != user.getId()  ) {
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