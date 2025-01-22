package br.devsuperior.dscatalog.controller;


import br.devsuperior.dscatalog.DTO.ProductDTO;
import br.devsuperior.dscatalog.repositories.FactoryProduct;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIntegration {

    @Autowired
    private MockMvc mockMvc;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenUtil tokenUtil;

    private String username, password, bearerToken;




    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;


        username = "maria@gmail.com";
        password = "123456";

        bearerToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

    }

    @Test
    public void findAllShouldReturnSortedPageWhenSortyByName() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/products?page=0&size=12&sort=name,asc")
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
        resultActions.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
    }


    @Test
    public void UpdateShouldReturnProductWhenIdExists() throws Exception {
        ProductDTO productDTO = FactoryProduct.CreateProductDTO();
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        String expectedName = productDTO.getName();
        String expectedDescription = productDTO.getDescription();

        ResultActions resultActions = mockMvc.perform(put("/products/{id}", existingId)
                .header("Authorization", "Bearer " + bearerToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.id").value(existingId));
        resultActions.andExpect(jsonPath("$.name").value(expectedName));
        resultActions.andExpect(jsonPath("$.description").value(expectedDescription));
    }

    @Test
    public void UpdateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        ProductDTO productDTO = FactoryProduct.CreateProductDTO();
        String jsonBody = objectMapper.writeValueAsString(productDTO);


        ResultActions resultActions = mockMvc.perform(put("/products/{id}", nonExistingId)
                .header("Authorization", "Bearer " + bearerToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));


        resultActions.andExpect(status().isNotFound());
    }
}
