package br.devsuperior.dscatalog.services;

import br.devsuperior.dscatalog.DTO.CategoryDTO;
import br.devsuperior.dscatalog.entities.Category;

import br.devsuperior.dscatalog.exceptions.NotFoundException;
import br.devsuperior.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {


    @Autowired
    private CategoryRepository repository;


    @Transactional(readOnly = true)
    public List<CategoryDTO> findAl() {
        List<Category> list = repository.findAll();
        return list.stream().map(x -> new CategoryDTO(x)).toList();
    }
    @Transactional(readOnly = true)
    public CategoryDTO findyById(Long id) {
        Optional<Category> result = repository.findById(id);
        Category entity = result.orElseThrow(() -> new NotFoundException("Entity not found"));
        return new CategoryDTO(entity);
    }

}



