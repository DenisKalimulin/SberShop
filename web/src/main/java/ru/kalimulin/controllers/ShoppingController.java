package ru.kalimulin.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kalimulin.service.ShoppingService;

@RestController
@RequestMapping("/shopping")
public class ShoppingController {
    private final ShoppingService shoppingService;

    @Autowired
    public ShoppingController(ShoppingService shoppingService) {
        this.shoppingService = shoppingService;
    }

    @PostMapping("/{listingId}")
    public ResponseEntity<String> buyProduct(@PathVariable Long listingId, HttpSession session) {
        shoppingService.buyListing(listingId, session);
        return ResponseEntity.ok("Покупка успешно завершена!");
    }
}
