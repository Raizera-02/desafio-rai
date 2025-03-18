package com.desafio_api.app.controller;

import com.desafio_api.app.domain.Product;
import com.desafio_api.app.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SuppressWarnings("deprecation")
class ProductControllerTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductController productController;

    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Criando o objeto Product com todos os campos obrigatórios preenchidos
        product = new Product(
                1L,
                "Product 1",
                "Descrição do produto",
                100.0,
                "Categoria A",
                10,
                LocalDateTime.now());
    }

    @Test
    void createProduct() {
        // Arrange: Definindo o comportamento do mock para salvar o produto
        when(productRepository.save(product)).thenReturn(product);

        // Act: Chamando o método do controller
        ResponseEntity<Product> response = productController.createProduct(product);

        // Assert: Verificando se o status é OK (200) e o produto salvo é o esperado
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(product, response.getBody());
    }

    @Test
    void updateProduct() {
        // Arrange: Mockando a busca do produto existente
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        product.setNome("Updated Product");
        product.setDescricao("Updated Description");
        product.setPreco(150.0);
        product.setCategoria("Categoria B");
        product.setQuantidadeEmEstoque(20);

        // Act: Chamando o método do controller para atualizar o produto
        ResponseEntity<Product> response = productController.updateProduct(1L, product);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void updateProductNotFound() {
        // Arrange: Mockando a busca do produto que não existe
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act: Chamando o método do controller para atualizar o produto
        ResponseEntity<Product> response = productController.updateProduct(1L, product);

        // Assert: Verificando se o status é NOT FOUND (404)
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void deleteProduct() {
        // Arrange: Mockando o comportamento de delete
        doNothing().when(productRepository).deleteById(1L);

        // Act: Chamando o método do controller para deletar o produto
        ResponseEntity<String> response = productController.deleteProduct(1L);

        // Assert: Verificando se o status é NO CONTENT (204)
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void deleteProductWithException() {
        // Arrange: Mockando a exceção DataIntegrityViolationException
        doThrow(new org.springframework.dao.DataIntegrityViolationException("")).when(productRepository).deleteById(1L);

        // Act: Chamando o método do controller para deletar o produto
        ResponseEntity<String> response = productController.deleteProduct(1L);

        // Assert: Verificando se o status é BAD REQUEST (400)
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().contains("Não é possível excluir o produto"));
    }

    @Test
    void getAllProducts() {
        // Arrange: Mockando a lista de produtos
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        // Act: Chamando o método do controller para obter todos os produtos
        ResponseEntity<List<Product>> response = productController.getAllProducts();

        // Assert: Verificando se o status é OK (200) e se a lista não está vazia
        assertEquals(200, response.getStatusCodeValue());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getProductById() {
        // Arrange: Mockando a busca do produto
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act: Chamando o método do controller para buscar o produto
        ResponseEntity<Product> response = productController.getProductById(1L);

        // Assert: Verificando se o status é OK (200) e o produto encontrado é o
        // esperado
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(product, response.getBody());
    }

    @Test
    void getProductByIdNotFound() {
        // Arrange: Mockando a busca de um produto não existente
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act: Chamando o método do controller para buscar o produto
        ResponseEntity<Product> response = productController.getProductById(1L);

        // Assert: Verificando se o status é NOT FOUND (404)
        assertEquals(404, response.getStatusCodeValue());
    }
}
