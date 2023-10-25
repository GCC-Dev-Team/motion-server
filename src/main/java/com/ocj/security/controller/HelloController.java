package com.ocj.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/hello")
    /*@PreAuthorize("@ex.hasAuthority('abc')")*/
    @PreAuthorize("hasAuthority('test')")
    public String sayHello(){

        return "hello world";
    }


}
