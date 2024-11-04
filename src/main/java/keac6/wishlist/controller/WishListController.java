package keac6.wishlist.controller;

import keac6.wishlist.model.User;
import keac6.wishlist.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("")
public class WishListController {
    private final WishListService wishListService;

    @Autowired
    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping("/signup")
    public String showSignUpPage() {
        return "signUp";
    }

    @PostMapping("/signup/save")
    public String saveNewUser(@ModelAttribute User newUser) {
        wishListService.saveNewUser(newUser);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String verifyLogin(@RequestParam String email, @RequestParam String password, Model model) {
        User user = wishListService.findByEmail(email);
        if (user != null && wishListService.authenticate(password, user.getPassword())) {
            model.addAttribute("success", "Du er nu logget ind.");
            return "login";
        }
        model.addAttribute("error", "Email eller password er ikke korrekt. Prøv igen.");
        return "login";
    }
}
