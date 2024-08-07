package com.softuni.perfumes_shop.service.impl;


import com.softuni.perfumes_shop.init.Initializer;
import com.softuni.perfumes_shop.model.dto.outbound.ViewCartItemDTO;
import com.softuni.perfumes_shop.model.entity.*;
import com.softuni.perfumes_shop.model.enums.Gender;
import com.softuni.perfumes_shop.model.enums.ImageType;
import com.softuni.perfumes_shop.model.enums.ProductType;
import com.softuni.perfumes_shop.model.enums.UserRole;
import com.softuni.perfumes_shop.repository.*;
import com.softuni.perfumes_shop.service.CartService;
import com.softuni.perfumes_shop.service.JwtService;
import com.softuni.perfumes_shop.service.exception.ObjectNotFoundException;
import com.softuni.perfumes_shop.service.session.CurrentUserDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
public class CartServiceImplIT {

    @Autowired
    private CartItemRepository cartItemRepository;

    public static class ImageUtils {
        public static String encodeToBase64(byte[] imageData) {
            return Base64.getEncoder().encodeToString(imageData);
        }
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    @MockBean
    private CurrentUserDetails currentUserDetails;

    private User user;
    @Autowired
    private ImageRepository imageRepository;

    @MockBean
    private Initializer initializer;

    @MockBean
    private JwtService jwtService;

    @BeforeEach
    public void setUp() {
        user = createUser();
        createProduct();
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        cartRepository.deleteAll();
        roleRepository.deleteAll();
        productRepository.deleteAll();
        typeRepository.deleteAll();
        imageRepository.deleteAll();
        cartItemRepository.deleteAll();
    }


    @Test
    public void testAddItemToCartSuccessfully() {
        when(currentUserDetails.optCurrentUser()).thenReturn(Optional.of(user));

        Optional<Product> optProduct = productRepository.findByName("testName");

        Assertions.assertTrue(optProduct.isPresent());

        Product product = optProduct.get();

        Assertions.assertTrue(cartService.addItemToCart(product.getId()));
        Assertions.assertEquals(1, user.getCart().getCartItems().size());
        CartItem cartItem = user.getCart().getCartItems().getFirst();
        Assertions.assertEquals(product.getId(), cartItem.getProduct().getId());
        Assertions.assertEquals(1, cartItem.getQuantity());
        Assertions.assertEquals(user.getCart().getId(), cartItem.getCart().getId());
    }

    @Test
    public void testAddItemToCartProductAlreadyAdded() {
        when(currentUserDetails.optCurrentUser()).thenReturn(Optional.of(user));

        Optional<Product> optProduct = productRepository.findByName("testName");

        Assertions.assertTrue(optProduct.isPresent());

        Product product = optProduct.get();

        cartService.addItemToCart(product.getId());

        Assertions.assertFalse(cartService.addItemToCart(product.getId()));
    }

    @Test
    public void testAddItemToCartNoCurrentUser() {
        when(currentUserDetails.optCurrentUser()).thenReturn(Optional.empty());

        Optional<Product> optProduct = productRepository.findByName("testName");

        Assertions.assertTrue(optProduct.isPresent());

        Product product = optProduct.get();

        Assertions.assertFalse(cartService.addItemToCart(product.getId()));
    }

    @Test
    public void testAddNewCart() {
        Assertions.assertEquals(1L, cartRepository.count());
        cartService.addNewCart(new Cart());
        Assertions.assertEquals(2L, cartRepository.count());
    }

    @Test
    @Transactional
    public void testGetAllCartItems() {
        when(currentUserDetails.optCurrentUser()).thenReturn(Optional.of(user));

        Optional<Product> optProduct = productRepository.findByName("testName");

        Assertions.assertTrue(optProduct.isPresent());

        Product product = optProduct.get();

        cartService.addItemToCart(product.getId());

        List<ViewCartItemDTO> cartItems = cartService.getAllCartItems();
        
        Assertions.assertEquals(1L, cartItems.size());
        ViewCartItemDTO viewCartItem = cartItems.getFirst();
        Assertions.assertEquals(1L, user.getCart().getCartItems().size());
        CartItem cartItem = user.getCart().getCartItems().getFirst();

        Assertions.assertEquals(cartItem.getId(), viewCartItem.getId());
        Assertions.assertEquals(cartItem.getProduct().getBrand(), viewCartItem.getBrand());
        Assertions.assertEquals(cartItem.getProduct().getName(), viewCartItem.getName());
        Assertions.assertEquals(cartItem.getProduct().getGender().getGender(), viewCartItem.getGender());
        Assertions.assertEquals(cartItem.getProduct().getPrice(), viewCartItem.getPrice());
        Assertions.assertEquals(cartItem.getProduct().getPackaging(), viewCartItem.getPackaging());
        Assertions.assertEquals(ImageUtils.encodeToBase64(cartItem.getProduct().getImage().getImage()), viewCartItem.getImage());
        Assertions.assertEquals(cartItem.getQuantity(), viewCartItem.getQuantity());
    }

    @Test
    public void testGetAllCartItemsNoCurrentUser() {
        when(currentUserDetails.optCurrentUser()).thenReturn(Optional.empty());

        Optional<Product> optProduct = productRepository.findByName("testName");

        Assertions.assertTrue(optProduct.isPresent());

        Product product = optProduct.get();

        cartService.addItemToCart(product.getId());

        List<ViewCartItemDTO> cartItems = cartService.getAllCartItems();
        Assertions.assertEquals(0L, cartItems.size());
    }

    @Test
    public void testGetSubtotal() {
        when(currentUserDetails.optCurrentUser()).thenReturn(Optional.of(user));

        Optional<Product> optProduct = productRepository.findByName("testName");

        Assertions.assertTrue(optProduct.isPresent());

        Product product = optProduct.get();

        Assertions.assertEquals(0L, user.getCart().getCartItems().size());

        cartService.addItemToCart(product.getId());

        BigDecimal result = cartService.getSubtotal();

        Assertions.assertEquals(product.getPrice(), result);
    }

    @Test
    public void testGetSubtotalNoCurrentUser() {
        when(currentUserDetails.optCurrentUser()).thenReturn(Optional.empty());

        Optional<Product> optProduct = productRepository.findByName("testName");

        Assertions.assertTrue(optProduct.isPresent());

        Product product = optProduct.get();

        cartService.addItemToCart(product.getId());

        BigDecimal result = cartService.getSubtotal();
        Assertions.assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    @Transactional
    public void testRemoveCartItem() {
        when(currentUserDetails.optCurrentUser()).thenReturn(Optional.of(user));

        Optional<Product> optProduct = productRepository.findByName("testName");

        Assertions.assertTrue(optProduct.isPresent());

        Product product = optProduct.get();

        Assertions.assertEquals(0L, user.getCart().getCartItems().size());

        cartService.addItemToCart(product.getId());

        Assertions.assertEquals(1L, user.getCart().getCartItems().size());

        Long id = user.getCart().getCartItems().getFirst().getId();

        cartService.removeCartItem(id);

        Assertions.assertEquals(0L, user.getCart().getCartItems().size());
    }

    @Test
    public void testRemoveCartItemThrowsObjectNotFoundException() {
        when(currentUserDetails.optCurrentUser()).thenReturn(Optional.of(user));
        
        Assertions.assertEquals(0L, user.getCart().getCartItems().size());
        Long notExistingId = 8L;
        Assertions.assertThrows(ObjectNotFoundException.class, () -> cartService.removeCartItem(notExistingId));
    }

    @Test
    public void testChangeQuantityById() {
        when(currentUserDetails.optCurrentUser()).thenReturn(Optional.of(user));
        Optional<Product> optProduct = productRepository.findByName("testName");

        Assertions.assertTrue(optProduct.isPresent());

        Product product = optProduct.get();

        cartService.addItemToCart(product.getId());

        Assertions.assertEquals(1L, user.getCart().getCartItems().size());
        Assertions.assertEquals(1, user.getCart().getCartItems().getFirst().getQuantity());
        Long id = user.getCart().getCartItems().getFirst().getId();

        cartService.changeQuantityById(id, 5);

        Assertions.assertEquals(5, user.getCart().getCartItems().getFirst().getQuantity());
    }

    @Test
    public void testContainsProductWithIdSuccess() {
        when(currentUserDetails.optCurrentUser()).thenReturn(Optional.of(user));

        Optional<Product> optProduct = productRepository.findByName("testName");

        Assertions.assertTrue(optProduct.isPresent());

        Product product = optProduct.get();

        Assertions.assertEquals(0L, user.getCart().getCartItems().size());

        cartService.addItemToCart(product.getId());

        Assertions.assertTrue(cartService.containsProductWithId(product.getId()));
    }

    @Test
    public void testContainsProductWithIdUnsuccess() {
        when(currentUserDetails.optCurrentUser()).thenReturn(Optional.of(user));

        Optional<Product> optProduct = productRepository.findByName("testName");

        Assertions.assertTrue(optProduct.isPresent());

        Product product = optProduct.get();

        Assertions.assertEquals(0L, user.getCart().getCartItems().size());

        cartService.addItemToCart(product.getId());

        Assertions.assertEquals(1L, user.getCart().getCartItems().size());

        Long notExistingId = 7L;

        Assertions.assertFalse(cartService.containsProductWithId(notExistingId));
    }

    @Test
    public void testGetShippingPrice() {
        Assertions.assertEquals(BigDecimal.valueOf(10), cartService.getShippingPrice());
    }

    @Test
    public void testGetTotalPrice() {
        when(currentUserDetails.optCurrentUser()).thenReturn(Optional.of(user));

        Optional<Product> optProduct = productRepository.findByName("testName");

        Assertions.assertTrue(optProduct.isPresent());

        Product product = optProduct.get();

        Assertions.assertEquals(0L, user.getCart().getCartItems().size());

        cartService.addItemToCart(product.getId());

        BigDecimal result = cartService.getTotalPrice();
        BigDecimal expectedResult = product.getPrice().add(BigDecimal.valueOf(10));

        Assertions.assertEquals(expectedResult, result);
    }

    @Test
    @Transactional
    public void testEmptyCart() {
        when(currentUserDetails.optCurrentUser()).thenReturn(Optional.of(user));

        Optional<Product> optProduct = productRepository.findByName("testName");

        Assertions.assertTrue(optProduct.isPresent());

        Product product = optProduct.get();

        Assertions.assertEquals(0L, user.getCart().getCartItems().size());

        cartService.addItemToCart(product.getId());

        Assertions.assertEquals(1L, user.getCart().getCartItems().size());

        cartService.emptyCart(user.getCart().getId());

        Assertions.assertEquals(0L, user.getCart().getCartItems().size());
    }

    private User createUser() {
        Role role = new Role();
        role.setUserRole(UserRole.USER);
        roleRepository.save(role);

        Cart cart = new Cart();
        cartRepository.save(cart);

        User user = new User();
        user.setUsername("testUser");
        user.setEmail("testEmail");
        user.setFirstName("testFirstName");
        user.setLastName("testLastName");
        user.setPassword("testPassword");
        user.setCart(cart);
        user.addRole(role);

        userRepository.save(user);

        return user;
    }


    private void createProduct() {

        Image image = new Image();
        image.setName("img");
        image.setImageType(ImageType.PRODUCT_IMAGE);
        image.setImage(new byte[]{1, 2, 3});

        Type type = new Type();
        type.setProductType(ProductType.PERFUME);
        type.setDescription("testDescription");
        typeRepository.save(type);


        Product product = new Product();
        product.setBrand("testBrand");
        product.setName("testName");
        product.setDescription("testDescription");
        product.setPrice(new BigDecimal("3.00"));
        product.setStock(5);
        product.setPackaging("80");
        product.setGender(Gender.MALE);
        product.setImage(image);
        product.setType(type);

        productRepository.save(product);
    }
}