package keac6.wishlist.controller;

import keac6.wishlist.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class WishListController {
    private final WishListService wishListService;

    @Autowired
    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping("/signup")
    public String signUp() {
        return "signUp";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
