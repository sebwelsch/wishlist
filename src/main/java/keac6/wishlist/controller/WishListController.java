package keac6.wishlist.controller;

import jakarta.servlet.http.HttpSession;
import keac6.wishlist.model.User;
import keac6.wishlist.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping()
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

    @PostMapping("/signup")
    public String saveNewUser(@ModelAttribute User newUser, Model model) {
        if (wishListService.findByEmail(newUser.getEmail()) != null) {
            model.addAttribute("error", "Denne email er allerede registreret.");
            return "signUp";
        }
        wishListService.saveNewUser(newUser);
        return "redirect:/login";
    }


    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String verifyLogin(@RequestParam String email, @RequestParam String password, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = wishListService.findByEmail(email);
        if (user != null && wishListService.authenticate(password, user.getPassword())) {
            session.setAttribute("loggedInUser", user);
            redirectAttributes.addFlashAttribute("success", "Du er nu logget ind.");
            return "redirect:/test";
        }
        model.addAttribute("error", "Email eller password er ikke korrekt. Prøv igen.");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/test")
    public String showTestPage(HttpSession session) {
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }
        return "test";
    }

    @GetMapping("/wishlist")
    public String showForm(Model model){
        return "index";
    }

    @PostMapping("/wishlist/create")
    public String createWishList(@RequestParam String wishlistName, Model model) {
        wishListRepository.createWishlist(wishlistName);
        model.addAttribute("message", "Wishlist created");
        return "index";
    }
}
