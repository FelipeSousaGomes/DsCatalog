package br.devsuperior.dscatalog.services;

import br.devsuperior.dscatalog.DTO.CategoryDTO;
import br.devsuperior.dscatalog.DTO.ProductDTO;
import br.devsuperior.dscatalog.entities.Category;
import br.devsuperior.dscatalog.entities.Product;
import br.devsuperior.dscatalog.exceptions.DataBaseException;
import br.devsuperior.dscatalog.exceptions.NotFoundException;
import br.devsuperior.dscatalog.repositories.CategoryRepository;
import br.devsuperior.dscatalog.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {


    @Autowired
    private ProductRepository repository;
    @Autowired
    private CategoryRepository categoryRepository;


    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Pageable pageable) {
        Page<Product> list = repository.findAll(pageable);
        return list.map(x -> new ProductDTO(x,x.getCategories()));
    }
    @Transactional(readOnly = true)
    public ProductDTO findyById(Long id) {
        Optional<Product> result = repository.findById(id);
        Product entity = result.orElseThrow(() -> new NotFoundException("Entity not found"));
        return new ProductDTO(entity,entity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO ProductDTO) {
            Product Product = new Product();
           copyDTOtoEntity(ProductDTO,Product);
             Product = repository.save(Product);
        return new ProductDTO(Product,Product.getCategories());

    }
    @Transactional
    public ProductDTO udpate(ProductDTO dto, Long id) {
        try {
        Product Product = repository.getReferenceById(id);
            copyDTOtoEntity(dto,Product);
        return new ProductDTO(Product,Product.getCategories());
    } catch (EntityNotFoundException e) {
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

    public void copyDTOtoEntity(ProductDTO dto, Product entity){
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());
        entity.getCategories().clear();
        for(CategoryDTO catDto :dto.getCategories()){
          Category category = categoryRepository.getReferenceById(catDto.getId());
          entity.getCategories().add(category);

        }
    }
}



