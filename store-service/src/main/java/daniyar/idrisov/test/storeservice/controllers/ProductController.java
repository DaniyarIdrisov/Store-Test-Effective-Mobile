package daniyar.idrisov.test.storeservice.controllers;

import daniyar.idrisov.test.storeservice.models.dto.ProductCreateDTO;
import daniyar.idrisov.test.storeservice.models.dto.ProductDTO;
import daniyar.idrisov.test.storeservice.models.dto.ProductUpdateDTO;
import daniyar.idrisov.test.storeservice.models.dto.ProductWithChildsDTO;
import daniyar.idrisov.test.storeservice.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService service;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ProductDTO createProduct(@Valid @RequestBody ProductCreateDTO dto,
                                    @RequestParam("organization_id") Long organizationId) {
        return service.createProduct(dto, organizationId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ProductDTO updateProduct(@RequestBody ProductUpdateDTO dto,
                                    @PathVariable("id") Long productId) {
        return service.updateProduct(dto, productId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}/activate")
    public ProductDTO activateProduct(@PathVariable("id") Long productId) {
        return service.activateProduct(productId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}/freeze")
    public ProductDTO freezeProduct(@PathVariable("id") Long productId) {
        return service.freezeProduct(productId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/{id}/ban")
    public ProductDTO banProduct(@PathVariable("id") Long productId) {
        return service.banProduct(productId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<ProductWithChildsDTO> getAllProducts() {
        return service.getAllProducts();
    }

    @PermitAll
    @GetMapping("/active")
    public List<ProductWithChildsDTO> getAllActiveProducts(Pageable pageable) {
        return service.getAllActiveProducts(pageable);
    }

    @PermitAll
    @GetMapping("/{id}")
    public ProductWithChildsDTO getProductById(@PathVariable("id") Long productId) {
        return service.getProductById(productId);
    }

}
