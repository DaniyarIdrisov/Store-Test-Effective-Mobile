package daniyar.idrisov.test.storeservice.controllers;

import daniyar.idrisov.test.storeservice.models.dto.PurchaseWithChildsDTO;
import daniyar.idrisov.test.storeservice.services.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/purchases")
public class PurchaseController {

    private final PurchaseService service;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public PurchaseWithChildsDTO makePurchase(@RequestParam("product_id") Long productId) {
        return service.makePurchase(productId);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}")
    public PurchaseWithChildsDTO returnPurchase(@PathVariable("id") Long purchaseId) {
        return service.returnPurchase(purchaseId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/current")
    public List<PurchaseWithChildsDTO> getCurrentPurchase() {
        return service.getCurrentPurchase();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public List<PurchaseWithChildsDTO> getAllPurchases() {
        return service.getAllPurchases();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public PurchaseWithChildsDTO getPurchaseById(@PathVariable("id") Long purchaseId) {
        return service.getPurchaseById(purchaseId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}/user")
    public List<PurchaseWithChildsDTO> getPurchaseByUserId(@PathVariable("id") Long userId) {
        return service.getPurchaseByUserId(userId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}/product")
    public List<PurchaseWithChildsDTO> getPurchaseByProductId(@PathVariable("id") Long productId) {
        return service.getPurchaseByProductId(productId);
    }

}
