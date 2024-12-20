package br.devsuperior.dscatalog.services;

import br.devsuperior.dscatalog.DTO.ProductDTO;
import br.devsuperior.dscatalog.entities.Product;
import br.devsuperior.dscatalog.exceptions.NotFoundException;
import br.devsuperior.dscatalog.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@SpringBootTest
@Transactional
public class ProductServiceIntegration {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 2111L;
        countTotalProducts = 25L;

    }

    @Test
    public void deleteShouldDeleteResourceWhenIdExists() throws Exception{
        productService.delete(existingId);

        Assertions.assertEquals(countTotalProducts -1, productRepository.count());
    }



    @Test
    public void deleteShouldDeleteThrowNotFoundExeceptionWhenIdDoesNotExist() throws Exception{
       Assertions.assertThrows(NotFoundException.class, () -> productService.delete(nonExistingId));
    }


    @Test
    public void findAllPagedShouldReturnPageWhenPage0Size10() throws Exception{
        PageRequest pageRequest = PageRequest.of(0, 10);

      Page<ProductDTO> result  = productService.findAllPaged(pageRequest);

                Assertions.assertEquals(0,result.getNumber());
                Assertions.assertFalse(result.isEmpty());
                Assertions.assertEquals(10,result.getSize());
                Assertions.assertEquals(result.getTotalElements(), countTotalProducts);

    }


    @Test
    public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExist() throws Exception{
        PageRequest pageRequest = PageRequest.of(50, 10);

        Page<ProductDTO> result  = productService.findAllPaged(pageRequest);

        Assertions.assertTrue(result.isEmpty());
    }


    @Test
    public void findAllPagedShouldReturnSortedPageWhenSortName() throws Exception{
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));

        Page<ProductDTO> result  = productService.findAllPaged(pageRequest);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
        Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
        Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
    }




}
