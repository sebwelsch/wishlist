package keac6.wishlist.controller;

import jakarta.servlet.http.HttpSession;
import keac6.wishlist.model.User;
import keac6.wishlist.model.Wish;
import keac6.wishlist.model.WishList;
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
    public String saveNewUser(@ModelAttribute User newUser, Model model, RedirectAttributes redirectAttributes) {
        if (wishListService.findByEmail(newUser.getEmail()) != null) {
            model.addAttribute("error", "Denne email er allerede registreret.");
            return "signUp";
        }
        wishListService.saveNewUser(newUser);
        redirectAttributes.addFlashAttribute("success", "Din konto blev oprettet. Du kan nu logge ind.");
        return "redirect:/login";
    }


    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String verifyLogin(HttpSession session, @RequestParam String email, @RequestParam String password, Model model, RedirectAttributes redirectAttributes) {
        User user = wishListService.findByEmail(email);
        if (user != null && wishListService.authenticate(password, user.getPassword())) {
            session.setAttribute("loggedInUser", user);
            redirectAttributes.addFlashAttribute("success", "Du er nu logget ind.");
            return "redirect:/overview";
        }
        model.addAttribute("error", "Email eller password er ikke korrekt. Prøv igen.");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/overview")
    public String showOverviewPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("wishLists",wishListService.getWishList(loggedInUser.getUserId()));
        model.addAttribute("firstName", loggedInUser.getFirstName());
        model.addAttribute("lastName", loggedInUser.getLastName());
        model.addAttribute("email", loggedInUser.getEmail());
        return "overview";
    }

    @GetMapping("/wishlist")
    public String showWishListPage(HttpSession session, Model model){
        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("userId", loggedInUser.getUserId());
        return "wishlist";
    }

    @PostMapping("/wishlist/create")
    public String createWishList(HttpSession session, @ModelAttribute WishList newWishList, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        newWishList.setUserId(loggedInUser.getUserId());
        wishListService.createWishList(newWishList);
        redirectAttributes.addFlashAttribute("message", "Ønskeliste oprettet");
        return "redirect:/overview";
    }

    @GetMapping("/createwish")
    public String showCreateWishPage(){

        return "addWish";
    }

    @PostMapping("/add")
    public String addWish(@ModelAttribute Wish newWish, RedirectAttributes redirectAttributes ) {

    if(newWish.getName() == null || newWish.getName().isEmpty()){
            redirectAttributes.addFlashAttribute("error", "Feltet navn skal udfyldes!");
            return "redirect:/createwish";
        }
        wishListService.saveNewWish(newWish);
        redirectAttributes.addFlashAttribute("success", "Ønsket er blevet tilføjet!");
        return "redirect:/createwish";
    }

    @GetMapping("/wishlist/{wishListId}")
    public String showWishList(@PathVariable int wishListId, Model model) {
        WishList wishList = wishListService.getWishListById(wishListId);
        if (wishList != null) {
            model.addAttribute("wishList", wishList);
            model.addAttribute("wishes", wishListService.getWishesByWishListId(wishListId));
            return "wishlistDetails";
        }
        model.addAttribute("error", "Ønskeliste ikke fundet");
        return "redirect:/overview";
    }


}
