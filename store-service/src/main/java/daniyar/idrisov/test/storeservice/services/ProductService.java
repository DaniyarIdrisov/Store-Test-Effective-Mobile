package daniyar.idrisov.test.storeservice.services;

import daniyar.idrisov.test.storeservice.exceptions.*;
import daniyar.idrisov.test.storeservice.models.dto.ProductCreateDTO;
import daniyar.idrisov.test.storeservice.models.dto.ProductDTO;
import daniyar.idrisov.test.storeservice.models.dto.ProductUpdateDTO;
import daniyar.idrisov.test.storeservice.models.dto.ProductWithChildsDTO;
import daniyar.idrisov.test.storeservice.models.enumerated.OrganizationState;
import daniyar.idrisov.test.storeservice.models.enumerated.ProductState;
import daniyar.idrisov.test.storeservice.models.jpa.Organization;
import daniyar.idrisov.test.storeservice.models.jpa.Product;
import daniyar.idrisov.test.storeservice.models.jpa.User;
import daniyar.idrisov.test.storeservice.models.mappers.ProductMapper;
import daniyar.idrisov.test.storeservice.repositories.OrganizationRepository;
import daniyar.idrisov.test.storeservice.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final OrganizationRepository organizationRepository;
    private final UserService userService;

    @Transactional
    public ProductDTO createProduct(ProductCreateDTO dto, Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(OrganizationNotFoundException::new);
        checkForAvailability(organization);
        Product createProduct = Product.builder()
                .name(dto.getName())
                .description(dto.getName())
                .price(dto.getPrice())
                .characteristics(dto.getCharacteristics())
                .state(ProductState.CREATED)
                .keywords(dto.getKeywords())
                .organization(organization)
                .build();
        Product product = productRepository.save(createProduct);
        return productMapper.toProductDTO(product);
    }

    private void checkForAvailability(Organization organization) {
        User user = userService.getCurrentUser();
        checkForOrganizationIsActive(organization);
        if (!organization.getCreatedBy().getId().equals(user.getId())) {
            throw new OrganizationNotBelongException();
        }
    }

    @Transactional
    public ProductDTO updateProduct(ProductUpdateDTO dto, Long productId) {
        Product updateProduct = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        checkForOrganizationIsActive(updateProduct.getOrganization());
        if (StringUtils.isNotBlank(dto.getName())) {
            updateProduct.setName(dto.getName());
        }
        if (StringUtils.isNotBlank(dto.getDescription())) {
            updateProduct.setDescription(dto.getDescription());
        }
        if (dto.getPrice() != null) {
            updateProduct.setPrice(dto.getPrice());
        }
        if (dto.getQuantity() != null) {
            updateProduct.setQuantity(dto.getQuantity());
        }
        if (dto.getCharacteristics() != null) {
            updateProduct.setCharacteristics(dto.getCharacteristics());
        }
        if (dto.getKeywords() != null) {
            updateProduct.setKeywords(dto.getKeywords());
        }
        Product product = productRepository.save(updateProduct);
        return productMapper.toProductDTO(product);
    }

    private void checkForOrganizationIsActive(Organization organization) {
        if (!organization.getState().equals(OrganizationState.ACTIVE)) {
            throw new OrganizationUnavailableException();
        }
    }


    @Transactional
    public ProductDTO activateProduct(Long productId) {
        Product updateProduct = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        checkForAlreadyState(updateProduct.getState(), ProductState.ACTIVE);
        updateProduct.setState(ProductState.ACTIVE);
        Product product = productRepository.save(updateProduct);
        return productMapper.toProductDTO(product);
    }

    @Transactional
    public ProductDTO freezeProduct(Long productId) {
        Product updateProduct = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        checkForAlreadyState(updateProduct.getState(), ProductState.FROZEN);
        updateProduct.setState(ProductState.FROZEN);
        Product product = productRepository.save(updateProduct);
        return productMapper.toProductDTO(product);
    }

    @Transactional
    public ProductDTO banProduct(Long productId) {
        Product updateProduct = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        checkForAlreadyState(updateProduct.getState(), ProductState.BANNED);
        updateProduct.setState(ProductState.BANNED);
        Product product = productRepository.save(updateProduct);
        return productMapper.toProductDTO(product);
    }

    private void checkForAlreadyState(ProductState currentState, ProductState state) {
        if (currentState.equals(state)) {
            throw new ProductAlreadyStateException();
        }
    }

    @Transactional
    public List<ProductWithChildsDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toProductWithChildsDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ProductWithChildsDTO> getAllActiveProducts(Pageable pageable) {
        return productRepository
                .findAllByStateAndOrganizationState(ProductState.ACTIVE,
                        OrganizationState.ACTIVE,
                        pageable).stream()
                .map(productMapper::toProductWithChildsDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductWithChildsDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        checkForAvailability(product);
        return productMapper.toProductWithChildsDTO(product);
    }

    private void checkForAvailability(Product product) {
        if ((!product.getState().equals(ProductState.ACTIVE)
                || !product.getOrganization().getState().equals(OrganizationState.ACTIVE))
                && !userService.isAdmin()) {
            throw new ProductUnavailableException();
        }
    }

}
