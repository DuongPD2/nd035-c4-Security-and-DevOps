package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.repositories.CartRepository;
import com.example.demo.repositories.ItemRepository;
import com.example.demo.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private CartController cartController;

    @Test
    public void test_AddToCart_Success() {
        User user = new User();
        user.setUsername("testUser");
        Cart cart = new Cart();
        user.setCart(cart);
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("testUser");
        request.setItemId(1L);
        request.setQuantity(2);

        Item item = new Item();
        item.setId(1L);
        item.setPrice(BigDecimal.valueOf(10.00));

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> response = cartController.addToCart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, cart.getItems().size());
    }

    @Test
    public void test_AddToCart_UserNotFound() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("unknownUser");
        request.setItemId(1L);
        request.setQuantity(1);

        ResponseEntity<Cart> response = cartController.addToCart(request);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void test_AddToCart_ItemNotFound() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("testUser");
        request.setItemId(999L);
        request.setQuantity(1);

        User user = new User();
        user.setUsername("testUser");
        Cart cart = new Cart();
        user.setCart(cart);

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Cart> response = cartController.addToCart(request);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void test_RemoveFromCart_Success() {
        User user = new User();
        user.setUsername("testUser");
        Cart cart = new Cart();
        user.setCart(cart);

        Item item = new Item();
        item.setId(1L);
        item.setPrice(BigDecimal.valueOf(10.00));

        // First, let's add 2 items to the cart
        cart.addItem(item);
        cart.addItem(item);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("testUser");
        request.setItemId(1L);
        request.setQuantity(1); // Delete 1 item

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> response = cartController.removeFromCart(request);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, cart.getItems().size());
    }

    @Test
    public void test_RemoveFromCart_UserNotFound() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("unknownUser");
        request.setItemId(1L);
        request.setQuantity(1);

        ResponseEntity<Cart> response = cartController.removeFromCart(request);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void test_RemoveFromCart_ItemNotFound() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("testUser");
        request.setItemId(999L);
        request.setQuantity(1);

        User user = new User();
        user.setUsername("testUser");
        Cart cart = new Cart();
        user.setCart(cart);

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseEntity<Cart> response = cartController.removeFromCart(request);
        assertEquals(404, response.getStatusCodeValue());
    }
}