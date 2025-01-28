package br.devsuperior.dscatalog.controller;

import br.devsuperior.dscatalog.DTO.ProductDTO;
import br.devsuperior.dscatalog.repositories.projections.ProductProjection;
import br.devsuperior.dscatalog.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
   private ProductService service;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>>findAll(
            @RequestParam(value = "name",defaultValue = "") String name,
            @RequestParam(value = "categoryId",defaultValue = "0") String categoryId,
            Pageable pageable){

        Page<ProductDTO> listPage = service.findAllPaged(name,categoryId,pageable);

        return ResponseEntity.ok().body(listPage);
    }

    @GetMapping("/{id}" )
    public ResponseEntity<ProductDTO>findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findyById(id));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
    @PostMapping
    public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO ProductDTO){
        ProductDTO = service.insert(ProductDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(ProductDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(ProductDTO);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
    @PutMapping("/{id}" )
    public ResponseEntity<ProductDTO> update(@Valid @RequestBody ProductDTO dto, @PathVariable Long id ){
        return ResponseEntity.ok().body(service.udpate(dto,id));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTO> delete(@PathVariable Long id ){
        service.delete(id);
       return ResponseEntity.noContent().build();
    }

}
