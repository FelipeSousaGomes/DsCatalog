package br.devsuperior.dscatalog.services;

import br.devsuperior.dscatalog.DTO.ProductDTO;
import br.devsuperior.dscatalog.entities.Category;
import br.devsuperior.dscatalog.entities.Product;
import br.devsuperior.dscatalog.exceptions.DataBaseException;
import br.devsuperior.dscatalog.exceptions.NotFoundException;
import br.devsuperior.dscatalog.repositories.CategoryRepository;
import br.devsuperior.dscatalog.repositories.FactoryProduct;
import br.devsuperior.dscatalog.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.engine.support.hierarchical.ThrowableCollector;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository Carepository;


    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private ProductDTO productDTO;
    private Category category;


    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 2000L;
        dependentId = 4L;
        product = FactoryProduct.CreateProduct();
        productDTO = FactoryProduct.CreateProductDTO();
        page = new PageImpl<>(List.of(product));
        category = new Category(existingId,"teste");

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
        Mockito.when(repository.getReferenceById(existingId)).thenReturn(product);
        Mockito.when(Carepository.getReferenceById(existingId)).thenReturn(category);
        Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(NotFoundException.class);
        Mockito.when(Carepository.getReferenceById(nonExistingId)).thenThrow(NotFoundException.class);
        Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);


        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);;

        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(repository.existsById(dependentId)).thenReturn(true);


    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {

        Assertions.assertDoesNotThrow(() -> productService.delete(existingId));
    }

    @Test
    public void deleteShouldDoNothingWhenIdDoesNotExist() {

        Assertions.assertThrows(NotFoundException.class, () -> productService.delete(nonExistingId));

    }

    @Test
    public void findAllPagedShouldReturnPage(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = productService.findAllPaged(pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenIdDependentId() {

        Assertions.assertThrows(DataBaseException.class, () -> productService.delete(dependentId));
    }

  @Test
    public void findByIdShouldReturnProductDTOWhenIdExistsId() {


      ProductDTO result =  productService.findyById(existingId);

      Assertions.assertNotNull(result);

    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(NotFoundException.class, () -> productService.findyById(nonExistingId));


    }
    @Test
    public void UpdateShouldReturnProductDTOWhenIdExistsId() {

        ProductDTO result = productService.udpate(productDTO, existingId);
        Assertions.assertNotNull(result);
    }

    @Test
    public void UpdateShouldThrowNotFoundExceptionWhenIdDoesNotExist() {

        Assertions.assertThrows(NotFoundException.class, () -> productService.udpate(productDTO, nonExistingId));
    }

}
