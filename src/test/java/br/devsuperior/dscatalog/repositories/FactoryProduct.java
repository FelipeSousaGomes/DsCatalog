package br.devsuperior.dscatalog.repositories;

import br.devsuperior.dscatalog.DTO.ProductDTO;
import br.devsuperior.dscatalog.entities.Category;
import br.devsuperior.dscatalog.entities.Product;

import java.time.Instant;

public class FactoryProduct {

    public static Product CreateProduct() {
        Product product = new Product(1L, "Phone","Good Phone", 800.0, "img.com", Instant.parse("2020-10-20T03:00:00Z"));
        product.getCategories().add(new Category(1L, "Livros"));
        return product;
    }

    public static ProductDTO CreateProductDTO() {
        Product product = CreateProduct();
        return new ProductDTO(product , product.getCategories());
    }
}
