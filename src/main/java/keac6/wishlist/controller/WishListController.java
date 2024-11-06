package keac6.wishlist.controller;


import keac6.wishlist.repository.WishListRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WishListController {

    private final WishListRepository wishListRepository;

    public WishListController(WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
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
