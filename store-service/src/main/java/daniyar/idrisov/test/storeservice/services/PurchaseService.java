package daniyar.idrisov.test.storeservice.services;

import daniyar.idrisov.test.storeservice.constants.NumericalConstants;
import daniyar.idrisov.test.storeservice.exceptions.*;
import daniyar.idrisov.test.storeservice.models.dto.PurchaseWithChildsDTO;
import daniyar.idrisov.test.storeservice.models.enumerated.OrganizationState;
import daniyar.idrisov.test.storeservice.models.enumerated.ProductState;
import daniyar.idrisov.test.storeservice.models.jpa.Discount;
import daniyar.idrisov.test.storeservice.models.jpa.Product;
import daniyar.idrisov.test.storeservice.models.jpa.Purchase;
import daniyar.idrisov.test.storeservice.models.jpa.User;
import daniyar.idrisov.test.storeservice.models.mappers.PurchaseMapper;
import daniyar.idrisov.test.storeservice.repositories.ProductRepository;
import daniyar.idrisov.test.storeservice.repositories.PurchaseRepository;
import daniyar.idrisov.test.storeservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseMapper purchaseMapper;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Value("${commission}")
    private Double commission;

    @Scheduled(fixedDelayString = "${schedule.update-period:30000}")
    @Transactional
    public void scheduleUpdate() {
        updatePurchase(Instant.now().minus(1, ChronoUnit.DAYS));
    }

    private void updatePurchase(Instant current) {
        List<Purchase> purchases = purchaseRepository
                .findAllByPossibleToReturnIsTrueAndReturnedIsFalseAndCreatedAtBefore(current);
        purchases = purchases.stream()
                .peek(purchase -> {
                    purchase.setPossibleToReturn(false);
                    User user = purchase.getProduct().getOrganization().getCreatedBy();
                    Double balance = user.getBalance();
                    user.setBalance(balance + purchase.getTotalCost() - (purchase.getTotalCost() * commission) / NumericalConstants.HUNDRED);
                })
                .collect(Collectors.toList());
        purchaseRepository.saveAll(purchases);
    }

    @Transactional
    public PurchaseWithChildsDTO makePurchase(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        checkProductQuantity(product.getQuantity());
        checkForAvailability(product);
        Double totalCoast = calculateTotalCost(product);
        User user = userService.getCurrentUser();
        checkBalance(totalCoast, user.getBalance());
        Double balance = user.getBalance();
        user.setBalance(balance - totalCoast);
        Long quantity = product.getQuantity();
        product.setQuantity(quantity - 1);
        Purchase newPurchase = Purchase.builder()
                .totalCost(totalCoast)
                .user(user)
                .product(product)
                .possibleToReturn(true)
                .returned(false)
                .build();
        Purchase purchase = purchaseRepository.save(newPurchase);
        return purchaseMapper.toPurchaseWithChildsDTO(purchase);
    }

    private void checkProductQuantity(Long quantity) {
        if (quantity == null || quantity <= 0) {
            throw new NotEnoughProductException();
        }
    }

    private void checkBalance(Double totalCoast, Double balance) {
        if (totalCoast > balance) {
            throw new NotEnoughMoneyException();
        }
    }

    private Double calculateTotalCost(Product product) {
        Instant now = Instant.now();
        Double totalCoast = product.getPrice();
        Double discountSum = product.getDiscounts().stream()
                .filter(discount -> {
                    return discount.getValidUntil().isAfter(now);
                })
                .mapToDouble(Discount::getDiscountAmount)
                .sum();
        totalCoast = totalCoast - (totalCoast * discountSum) / NumericalConstants.HUNDRED;
        if (totalCoast < NumericalConstants.ZERO) {
            return NumericalConstants.ZERO;
        }
        return totalCoast;
    }

    private void checkForAvailability(Product product) {
        if ((!product.getState().equals(ProductState.ACTIVE)
                || !product.getOrganization().getState().equals(OrganizationState.ACTIVE))) {
            throw new ProductUnavailableException();
        }
    }

    @Transactional
    public PurchaseWithChildsDTO returnPurchase(Long purchaseId) {
        Purchase updatePurchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(PurchaseNotFoundException::new);
        checkForAvailability(updatePurchase);
        User user = updatePurchase.getUser();
        Double balance = user.getBalance();
        user.setBalance(balance + updatePurchase.getTotalCost());
        Product product = updatePurchase.getProduct();
        Long quantity = product.getQuantity();
        product.setQuantity(quantity + 1);
        updatePurchase.setReturned(true);
        updatePurchase.setPossibleToReturn(false);
        Purchase purchase = purchaseRepository.save(updatePurchase);
        return purchaseMapper.toPurchaseWithChildsDTO(purchase);
    }

    private void checkForAvailability(Purchase purchase) {
        User user = userService.getCurrentUser();
        if (!user.getId().equals(purchase.getUser().getId())) {
            throw new NoAttachmentException();
        }
        if (!purchase.isPossibleToReturn()) {
            throw new PurchaseReturnTimeExpiredException();
        }
        if (purchase.isReturned()) {
            throw new PurchaseAlreadyReturned();
        }
    }

    @Transactional
    public List<PurchaseWithChildsDTO> getCurrentPurchase() {
        User user = userService.getCurrentUser();
        return purchaseRepository.findAllByUserId(user.getId()).stream()
                .map(purchaseMapper::toPurchaseWithChildsDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PurchaseWithChildsDTO> getAllPurchases() {
        return purchaseRepository.findAll().stream()
                .map(purchaseMapper::toPurchaseWithChildsDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PurchaseWithChildsDTO getPurchaseById(Long purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(PurchaseNotFoundException::new);
        return purchaseMapper.toPurchaseWithChildsDTO(purchase);
    }

    @Transactional
    public List<PurchaseWithChildsDTO> getPurchaseByUserId(Long userId) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return purchaseRepository.findAllByUserId(userId).stream()
                .map(purchaseMapper::toPurchaseWithChildsDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PurchaseWithChildsDTO> getPurchaseByProductId(Long productId) {
        productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        return purchaseRepository.findAllByProductId(productId).stream()
                .map(purchaseMapper::toPurchaseWithChildsDTO)
                .collect(Collectors.toList());
    }

}
