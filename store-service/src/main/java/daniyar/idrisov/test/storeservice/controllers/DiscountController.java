package daniyar.idrisov.test.storeservice.controllers;

import daniyar.idrisov.test.storeservice.models.dto.DiscountCreateDTO;
import daniyar.idrisov.test.storeservice.models.dto.DiscountDTO;
import daniyar.idrisov.test.storeservice.models.dto.DiscountUpdateDTO;
import daniyar.idrisov.test.storeservice.models.dto.DiscountWithChildsDTO;
import daniyar.idrisov.test.storeservice.services.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/discounts")
public class DiscountController {

    private final DiscountService service;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public DiscountWithChildsDTO createDiscount(@RequestBody DiscountCreateDTO dto) {
        return service.createDiscount(dto);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public DiscountWithChildsDTO updateDiscount(@RequestBody DiscountUpdateDTO dto,
                                                @PathVariable("id") Long discountId) {
        return service.updateDiscount(dto, discountId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public DiscountDTO deleteDiscount(@PathVariable("id") Long discountId) {
        return service.deleteDiscount(discountId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<DiscountWithChildsDTO> getAllDiscounts() {
        return service.getAllDiscounts();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public DiscountWithChildsDTO getDiscountById(@PathVariable("id") Long discountId) {
        return service.getDiscountById(discountId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}/product")
    public List<DiscountWithChildsDTO> getDiscountsByProductId(@PathVariable("id") Long productId) {
        return service.getDiscountsByProductId(productId);
    }

}
