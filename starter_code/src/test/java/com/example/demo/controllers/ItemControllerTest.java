package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.repositories.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemController itemController;

    @Test
    public void test_GetItems_Success() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item 1");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Item 2");

        when(itemRepository.findAll()).thenReturn(Arrays.asList(item1, item2));

        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void test_GetItemById_Success() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Item 1");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Item 1", response.getBody().getName());
    }

    @Test
    public void test_GetItemById_NotFound() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void test_GetItemsByName_Success() {
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Item 1");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Item 1");

        when(itemRepository.findByName("Item 1")).thenReturn(Arrays.asList(item1, item2));

        ResponseEntity<List<Item>> response = itemController.getItemsByName("Item 1");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void test_GetItemsByName_NotFound() {
        when(itemRepository.findByName("NonExistentItem")).thenReturn(Collections.emptyList());

        ResponseEntity<List<Item>> response = itemController.getItemsByName("NonExistentItem");
        assertEquals(404, response.getStatusCodeValue());
    }
}