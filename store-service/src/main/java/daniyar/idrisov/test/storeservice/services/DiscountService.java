package daniyar.idrisov.test.storeservice.services;

import daniyar.idrisov.test.storeservice.exceptions.DiscountNotFoundException;
import daniyar.idrisov.test.storeservice.exceptions.ProductNotFoundException;
import daniyar.idrisov.test.storeservice.models.dto.DiscountCreateDTO;
import daniyar.idrisov.test.storeservice.models.dto.DiscountDTO;
import daniyar.idrisov.test.storeservice.models.dto.DiscountUpdateDTO;
import daniyar.idrisov.test.storeservice.models.dto.DiscountWithChildsDTO;
import daniyar.idrisov.test.storeservice.models.jpa.Discount;
import daniyar.idrisov.test.storeservice.models.jpa.Product;
import daniyar.idrisov.test.storeservice.models.mappers.DiscountMapper;
import daniyar.idrisov.test.storeservice.repositories.DiscountRepository;
import daniyar.idrisov.test.storeservice.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final DiscountMapper discountMapper;
    private final ProductRepository productRepository;

    @Transactional
    public DiscountWithChildsDTO createDiscount(DiscountCreateDTO dto) {
        List<Product> products = dto.getProductIds().stream()
                .map(id -> productRepository.findById(id)
                        .orElseThrow(ProductNotFoundException::new))
                .toList();
        Discount createDiscount = Discount.builder()
                .discountAmount(dto.getDiscountAmount())
                .validUntil(dto.getValidUntil())
                .products(products)
                .build();
        Discount discount = discountRepository.save(createDiscount);
        return discountMapper.toDiscountWithChildsDTO(discount);
    }

    @Transactional
    public DiscountWithChildsDTO updateDiscount(DiscountUpdateDTO dto, Long discountId) {
        Discount updateDiscount = discountRepository.findById(discountId)
                .orElseThrow(DiscountNotFoundException::new);
        if (dto.getDiscountAmount() != null) {
            updateDiscount.setDiscountAmount(dto.getDiscountAmount());
        }
        if (dto.getValidUntil() != null) {
            updateDiscount.setValidUntil(dto.getValidUntil());
        }
        if (dto.getProductIds() != null) {
            List<Product> products = dto.getProductIds().stream()
                    .map(id -> productRepository.findById(id)
                            .orElseThrow(ProductNotFoundException::new))
                    .toList();
            discountRepository.deleteDiscountProducts(discountId);
            updateDiscount.setProducts(products);
        }
        Discount discount = discountRepository.save(updateDiscount);
        return discountMapper.toDiscountWithChildsDTO(discount);
    }

    @Transactional
    public DiscountDTO deleteDiscount(Long discountId) {
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(DiscountNotFoundException::new);
        discountRepository.delete(discount);
        return discountMapper.toDiscountDTO(discount);
    }

    @Transactional
    public List<DiscountWithChildsDTO> getAllDiscounts() {
        return discountRepository.findAll().stream()
                .map(discountMapper::toDiscountWithChildsDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public DiscountWithChildsDTO getDiscountById(Long discountId) {
        Discount discount = discountRepository.findById(discountId)
                .orElseThrow(DiscountNotFoundException::new);
        return discountMapper.toDiscountWithChildsDTO(discount);
    }

    @Transactional
    public List<DiscountWithChildsDTO> getDiscountsByProductId(Long productId) {
        productRepository.findById(productId).orElseThrow(ProductNotFoundException::new);
        return discountRepository.findAllByProductId(productId).stream()
                .map(discountMapper::toDiscountWithChildsDTO)
                .collect(Collectors.toList());
    }

}
