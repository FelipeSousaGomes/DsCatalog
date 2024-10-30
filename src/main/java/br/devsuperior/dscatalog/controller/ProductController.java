package br.devsuperior.dscatalog.controller;

import br.devsuperior.dscatalog.DTO.ProductDTO;
import br.devsuperior.dscatalog.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
   private ProductService service;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>>findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
                                                    @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
                                                    @RequestParam(value = "direction", defaultValue = "ASC") String direction){
        PageRequest pageRequest = PageRequest.of(page,linesPerPage, Sort.Direction.valueOf(direction),orderBy);

        Page<ProductDTO> listPage = service.findAllPaged(pageRequest);

        return ResponseEntity.ok().body(listPage);
    }

    @GetMapping("/{id}" )
    public ResponseEntity<ProductDTO>findById(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.findyById(id));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO ProductDTO){
        ProductDTO = service.insert(ProductDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(ProductDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(ProductDTO);
    }

    @PutMapping("/{id}" )
    public ResponseEntity<ProductDTO> update(@RequestBody ProductDTO dto, @PathVariable Long id ){
        return ResponseEntity.ok().body(service.udpate(dto,id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTO> delete(@PathVariable Long id ){
        service.delete(id);
       return ResponseEntity.noContent().build();
    }

}
