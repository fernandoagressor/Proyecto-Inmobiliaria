package com.inmobiliaria.inmobiliaria_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PruebaController {

    @GetMapping("/api/prueba")
    public String prueba() {
        System.out.println("ENTRO AL CONTROLADOR");
        return "FUNCIONA";
    }
}

