package br.devsuperior.dscatalog.services;

import br.devsuperior.dscatalog.entities.Category;

import br.devsuperior.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;


    @Transactional(readOnly = true)
    public List<Category> findAl(){
       return repository.findAll().stream().toList();
    }
}
