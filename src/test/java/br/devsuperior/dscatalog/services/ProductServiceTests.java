package br.devsuperior.dscatalog.services;

import br.devsuperior.dscatalog.entities.Product;
import br.devsuperior.dscatalog.exceptions.DataBaseException;
import br.devsuperior.dscatalog.exceptions.NotFoundException;
import br.devsuperior.dscatalog.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository repository;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 2000L;
        dependentId = 4L;





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
    public void deleteShouldThrowDatabaseExceptionWhenIdDependentId() {

        Assertions.assertThrows(DataBaseException.class, () -> productService.delete(dependentId));
    }

}
