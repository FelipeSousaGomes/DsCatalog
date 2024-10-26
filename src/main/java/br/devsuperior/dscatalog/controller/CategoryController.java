package br.devsuperior.dscatalog.controller;

import br.devsuperior.dscatalog.entities.Category;
import br.devsuperior.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.smartcardio.CardTerminal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    @Autowired
   private CategoryService service;

    @GetMapping
    public ResponseEntity<List<Category>>findAll(){
        List<Category> list = service.findAl();
        return ResponseEntity.ok().body(list);
    }

}
