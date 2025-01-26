package br.devsuperior.dscatalog.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import br.devsuperior.dscatalog.DTO.ProductDTO;
import br.devsuperior.dscatalog.exceptions.DataBaseException;
import br.devsuperior.dscatalog.exceptions.NotFoundException;
import br.devsuperior.dscatalog.repositories.FactoryProduct;
import br.devsuperior.dscatalog.repositories.projections.ProductProjection;
import br.devsuperior.dscatalog.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ProductController.class , excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;
    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;
    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;


    @BeforeEach
    void setUp() throws Exception {

        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;



        productDTO = FactoryProduct.CreateProductDTO();

        page = new PageImpl<>(List.of(productDTO));


        when(productService.findyById(existingId)).thenReturn(productDTO);
        when(productService.findyById(nonExistingId)).thenThrow(NotFoundException.class);
        when(productService.udpate(any(), eq(existingId))).thenReturn(productDTO);
        when(productService.udpate(any(), eq(nonExistingId))).thenThrow(NotFoundException.class);
        when(productService.insert(any())).thenReturn(productDTO);


        doNothing().when(productService).delete(existingId);
        doThrow(NotFoundException.class).when(productService).delete(nonExistingId);
        doThrow(DataBaseException.class).when(productService).delete(dependentId);


        when(productService.findAllPaged(any())).thenReturn(page);

    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }


    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").exists());
        resultActions.andExpect(jsonPath("$.description").exists());
    }


    @Test
    public void UpdateShouldReturnProductWhenIdExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions resultActions = mockMvc.perform(put("/products/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").exists());
        resultActions.andExpect(jsonPath("$.name").exists());
        resultActions.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void UpdateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        ResultActions resultActions = mockMvc.perform(put("/products/{id}", nonExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isNotFound());

    }


    @Test
    public void findByIdShouldReturnNotFoundWhenNotIdExists() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }


    @Test
    public void saveShouldReturnCreatedWhenIdExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        ResultActions resultActions = mockMvc.perform(post("/products", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isCreated());


    }


    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception {
        ResultActions resultActions = mockMvc.perform(delete("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNoFoundWhenIdExists() throws Exception {
        ResultActions resultActions = mockMvc.perform(delete("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));
        resultActions.andExpect(status().isNotFound());
    }
}
