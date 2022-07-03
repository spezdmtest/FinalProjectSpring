package com.griddynamics.finalprojectspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.griddynamics.finalprojectspring.dto.CartDTO;
import com.griddynamics.finalprojectspring.services.CartService;
import com.griddynamics.finalprojectspring.services.SessionData;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    @Autowired
    HttpSession httpSession;

    private final CartService service;
    private final SessionData sessionData;

    public CartController(CartService service, SessionData sessionData)
    {
        this.service = service;
        this.sessionData = sessionData;
    }

    @GetMapping(path = "/cart", produces = "application/json")
    public CartDTO ViewCart(Principal principal) throws Exception {
        if (principal == null) {
              return new CartDTO();
        } else {
            CartDTO cartDTO = service.getCartByUser(principal.getName());
            if (service.isNotQuantity()) {
                throw new Exception();
            }
            return cartDTO;
        }
    }

    @GetMapping(path = "/cart/session", produces = "application/json")
    public CartDTO ViewSessionCart(Principal principal, HttpSession httpSession) throws Exception {
        if (!httpSession.isNew()) {
            sessionData.setPrincipal(principal);
            System.out.println(sessionData.getPrincipal());
        }
        if (principal == null || sessionData.getPrincipal() == null) {
            CartDTO cartDTO = new CartDTO();
            cartDTO.setOrder("Wrong order");
            return cartDTO;
        } else {
            CartDTO cartDTO = service.getCartByUser(principal.getName());
            httpSession.invalidate();
            if (service.isNotQuantity()) {
                throw new Exception();
            }
            return cartDTO;
        }
    }

    @ExceptionHandler
    public ResponseEntity<String> ExistExceptionHandler(Exception e) {
        return new ResponseEntity<>("\"Don't available quantity in store\"", HttpStatus.CONFLICT);
    }
}

