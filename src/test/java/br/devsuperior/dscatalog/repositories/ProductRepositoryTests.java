package br.devsuperior.dscatalog.repositories;

import br.devsuperior.dscatalog.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
private ProductRepository productRepository;

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        productRepository.deleteById(1L);

        Optional<Product> product = productRepository.findById(1L);

        Assertions.assertFalse(product.isPresent());

    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIsNull() {

        Long longId = 26L;

        Product product =FactoryProduct.CreateProduct();
        product.setId(null);

        productRepository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(longId, product.getId());

    }


    @Test
    public void findByIdshoulReturnOptionalNotEmptyWhenIdExists() {

        Long longId = 36L;

        Optional<Product> product = productRepository.findById(longId);

        Assertions.assertFalse(product.isPresent());
    }

    @Test
    public void findByIdshoulReturnOptionalEmptyWhenIdNotExists() {

        Long longId = 1L;

        Optional<Product> product = productRepository.findById(longId);

        Assertions.assertTrue(product.isPresent());
    }


}
