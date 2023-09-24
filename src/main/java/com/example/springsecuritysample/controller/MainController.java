package com.example.springsecuritysample.controller;

import com.example.springsecuritysample.model.entity.Users;
import com.example.springsecuritysample.service.UsersService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class MainController {
    private final UsersService usersService;

    public MainController(UsersService usersService) {
        this.usersService = usersService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('OP_ACCESS_USER')")
    public String user() {
        return "user";
    }
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('OP_ACCESS_ADMIN')")
    public String admin(Model model) {
        model.addAttribute("users", usersService.findAll());
        return "admin";
    }

    @RequestMapping("/user/get/{id}")
    @PostAuthorize("returnObject.get().email == authentication.name")
    public @ResponseBody
    Optional<Users> getUser(@PathVariable("id") Long id) {
        return usersService.findById(id);
    }

    @RequestMapping(value="/admin/register")
    @PreAuthorize("hasAuthority('OP_NEW_USER')")
    public String registerPage(Model model) {
        model.addAttribute("user", new Users());
        return "redirect:/admin";
    }

    @RequestMapping(value = "/admin/edit/{id}")
    public String editPage(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", usersService.findById(id));
        return "registerUser";
    }

    @RequestMapping(value = "/admin/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        usersService.deleteById(usersService.findById(id));
        return "redirect:/admin";
    }

    /*@RequestMapping(value = "/admin/register")
    public String register(@ModelAttribute(name = "user")Users users) {
        usersService.registerUser(users);
        return "redirect:/admin";
    }*/

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String errorPage() {
        return "error";
    }
}
