package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderController orderController;

    @Test
    public void test_SubmitOrder_Success() {
        User user = new User();
        user.setUsername("testUser");

        Cart cart = new Cart();
        Item item = new Item();
        item.setId(1L);
        item.setPrice(BigDecimal.valueOf(10.00));

        cart.addItem(item);
        user.setCart(cart);

        when(userRepository.findByUsername("testUser")).thenReturn(user);

        UserOrder order = UserOrder.createFromCart(cart);

        ResponseEntity<UserOrder> response = orderController.submit("testUser");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void test_SubmitOrder_UserNotFound() {
        when(userRepository.findByUsername("unknownUser")).thenReturn(null);

        ResponseEntity<UserOrder> response = orderController.submit("unknownUser");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void test_GetOrdersForUser_Success() {
        User user = new User();
        user.setUsername("testUser");

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(Collections.emptyList());

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("testUser");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void test_GetOrdersForUser_UserNotFound() {
        when(userRepository.findByUsername("unknownUser")).thenReturn(null);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("unknownUser");
        assertEquals(404, response.getStatusCodeValue());
    }
}