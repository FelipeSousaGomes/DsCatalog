package br.devsuperior.dscatalog.DTO;

import br.devsuperior.dscatalog.entities.Category;
import br.devsuperior.dscatalog.entities.Product;
import jakarta.persistence.Column;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imgUrl;
    private Instant date;

    private Set<CategoryDTO> categories = new HashSet<>();


    public ProductDTO(){}

    public ProductDTO(Long id ,String name, String description, Double price, String imgUrl, Instant date) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.date = date;
        this.id = id;
    }
    public ProductDTO(Product entity, Set<Category> categories) {
        this(entity);
        for (Category cat : entity.getCategories()) {
            this.categories.add(new CategoryDTO(cat));
        }

    }

    public ProductDTO(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.imgUrl = entity.getImgUrl();
        this.date = entity.getDate();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Set<CategoryDTO> getCategories() {
        return categories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCategories(Set<CategoryDTO> categories) {
        this.categories = categories;
    }
}

