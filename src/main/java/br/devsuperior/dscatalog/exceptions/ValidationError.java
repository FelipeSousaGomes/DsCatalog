package br.devsuperior.dscatalog.exceptions;

import org.springframework.validation.FieldError;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError{


    private List<fieldMessage> errors = new ArrayList<>();

    public List<fieldMessage> getErrors() {
        return errors;
    }

    public void addFieldError(String fieldName, String message) {
        errors.add(new fieldMessage(fieldName,message));
    }
}
