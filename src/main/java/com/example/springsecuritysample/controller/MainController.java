package com.example.springsecuritysample.controller;

import com.example.springsecuritysample.jwt.JwtAuth;
import com.example.springsecuritysample.jwt.JwtUtils;
import com.example.springsecuritysample.model.entity.Users;
import com.example.springsecuritysample.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Arrays;
import java.util.Optional;

@Controller
public class MainController {
    private final UsersService usersService;
    private final AuthenticationManager manager;
    private final JwtUtils jwtUtils;

    public MainController(UsersService usersService, AuthenticationManager manager, JwtUtils jwtUtils) {
        this.usersService = usersService;
        this.manager = manager;
        this.jwtUtils = jwtUtils;
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

    @RequestMapping(value = "/getCookie", method = RequestMethod.GET)
    public String getCookie(HttpServletRequest request, HttpSession session) {
        Arrays.stream(request.getCookies()).forEach((x)-> {
            System.out.println(x.getName() + " : " + x.getValue());
        });
        System.out.println("sessionId : " + session.getId());
        return "login";
    }

    @RequestMapping(value = "/setCookie", method = RequestMethod.GET)
    public String setCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("user", "test");
        cookie.setMaxAge(60);
        response.addCookie(cookie);
        return "login";
    }

    @RequestMapping(value = "/jwt/login", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<?> jwtLogin(@RequestBody JwtAuth jwtAuth, HttpServletResponse response) {
        try {
            manager.authenticate(new UsernamePasswordAuthenticationToken(jwtAuth.getUsername(),jwtAuth.getPassword()));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        response.addHeader("authorization", jwtUtils.generateToken(jwtAuth.getUsername()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/jwt/hello", method = RequestMethod.GET)
    public @ResponseBody String jwtHello() {
        return "Hello Jwt";
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public @ResponseBody Principal getInfo(Principal principal) {
        return principal;
    }
}
