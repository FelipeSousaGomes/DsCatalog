package br.devsuperior.dscatalog.services;

import br.devsuperior.dscatalog.DTO.CategoryDTO;
import br.devsuperior.dscatalog.entities.Category;

import br.devsuperior.dscatalog.exceptions.DataBaseException;
import br.devsuperior.dscatalog.exceptions.NotFoundException;
import br.devsuperior.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {


    @Autowired
    private CategoryRepository repository;


    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> list = repository.findAll();
        return list.stream().map(x -> new CategoryDTO(x)).toList();
    }
    @Transactional(readOnly = true)
    public CategoryDTO findyById(Long id) {
        Optional<Category> result = repository.findById(id);
        Category entity = result.orElseThrow(() -> new NotFoundException("Entity not found"));
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO categoryDTO) {
            Category category = new Category();
            category.setName(categoryDTO.getName());
             category = repository.save(category);
        return new CategoryDTO(category);

    }
    @Transactional
    public CategoryDTO udpate(CategoryDTO dto, Long id) {
        try {
        Category category = repository.getReferenceById(id);
        category.setName(dto.getName());
        category = repository.save(category);
        return new CategoryDTO(category);
    } catch (NotFoundException e) {
            throw new NotFoundException("Id not found " + id);
        }
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Recurso não encontrado");
        }
        try {
            repository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Falha de integridade referencial");
        }
    }
}



