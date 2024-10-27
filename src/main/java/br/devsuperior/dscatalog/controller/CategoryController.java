package br.devsuperior.dscatalog.controller;

import br.devsuperior.dscatalog.DTO.CategoryDTO;
import br.devsuperior.dscatalog.entities.Category;
import br.devsuperior.dscatalog.services.CategoryService;
import jakarta.servlet.Servlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    @Autowired
   private CategoryService service;

    @GetMapping
    public ResponseEntity<Page<CategoryDTO>>findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
                                                    @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
                                                    @RequestParam(value = "direction", defaultValue = "ASC") String direction){
        PageRequest pageRequest = PageRequest.of(page,linesPerPage, Sort.Direction.valueOf(direction),orderBy);

        Page<CategoryDTO> listPage = service.findAllPaged(pageRequest);

        return ResponseEntity.ok().body(listPage);
    }

    @GetMapping("/{id}" )
    public ResponseEntity<CategoryDTO>findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findyById(id));
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO categoryDTO){
        categoryDTO = service.insert(categoryDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(categoryDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(categoryDTO);
    }

    @PutMapping("/{id}" )
    public ResponseEntity<CategoryDTO> update(@RequestBody CategoryDTO dto, @PathVariable Long id ){
        return ResponseEntity.ok().body(service.udpate(dto,id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDTO> delete(@PathVariable Long id ){
        service.delete(id);
       return ResponseEntity.noContent().build();
    }

}
