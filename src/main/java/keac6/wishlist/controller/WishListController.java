package keac6.wishlist.controller;

import jakarta.servlet.http.HttpServletRequest;
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
        if (wishListService.getUserByEmail(newUser.getEmail()) != null) {
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
        User user = wishListService.getUserByEmail(email);
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
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("wishLists", wishListService.getAllWishLists(loggedInUser.getUserId()));
        return "overview";
    }

    @PostMapping("/wishlist/create")
    public String createWishList(HttpSession session, @ModelAttribute WishList newWishList, RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        newWishList.setUserId(loggedInUser.getUserId());
        wishListService.createWishList(newWishList);
        redirectAttributes.addFlashAttribute("success", "Ønskelisten blev oprettet");
        return "redirect:/overview";
    }

    @PostMapping("/wishlist/delete/{wishListId}")
    public String deleteWishList(HttpSession session, RedirectAttributes redirectAttributes, @PathVariable int wishListId) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        wishListService.deleteWishList(wishListId);
        redirectAttributes.addFlashAttribute("success", "Ønskelisten blev slettet");
        return "redirect:/overview";
    }

    @GetMapping("/wishlist/{wishListId}/add")
    public String showCreateWishPage(@PathVariable int wishListId, Model model) {
        model.addAttribute("wishListId", wishListId);
        return "addWish";
    }

    @PostMapping("/wish/add")
    public String addWish(@ModelAttribute Wish newWish, @RequestParam int wishListId, RedirectAttributes redirectAttributes) {
        newWish.setWishListId(wishListId);
        wishListService.saveNewWish(newWish);
        redirectAttributes.addFlashAttribute("success", "Ønsket er blevet tilføjet!");
        return "redirect:/wishlist/" + wishListId;
    }

    @PostMapping("/wish/delete/{wishId}")
    public String deleteWish(HttpSession session, RedirectAttributes redirectAttributes, @PathVariable int wishId) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        wishListService.deleteWish(wishId);
        redirectAttributes.addFlashAttribute("success", "Ønsket blev slettet");
        return "redirect:/overview";
    }

    @GetMapping("/wishlist/{wishListId}")
    public String showWishListPage(@PathVariable int wishListId, Model model, HttpSession session, HttpServletRequest request) {
        WishList wishList = wishListService.getWishListById(wishListId);
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        User wishListOwner = wishListService.getUserById(wishList.getUserId());

        if ((loggedInUser != null) && (loggedInUser.getUserId() == wishList.getUserId())) {
            model.addAttribute("canEditWishList", true);
        } else {
            model.addAttribute("canEditWishList", false);
        }


        String currentUrl = request.getRequestURL().toString();
        model.addAttribute("currentUrl", currentUrl);

        model.addAttribute("wishList", wishList);
        model.addAttribute("wishes", wishListService.getWishesByWishListId(wishListId));
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("wishListOwner", wishListOwner);
        return "wishList";
    }

    @PostMapping("/wish/reserve/{wishId}")
    public String reserveWish(@RequestParam int reservedByUserId, @PathVariable int wishId, RedirectAttributes redirectAttributes) {
        Wish existingWish = wishListService.findWishById(wishId);
        if (existingWish == null) {
            redirectAttributes.addFlashAttribute("error", "Ønsket blev ikke fundet.");
            return "redirect:/overview";
        }

        if (existingWish.isReserved()) {
            wishListService.reserveWish(false, reservedByUserId, wishId);
            redirectAttributes.addFlashAttribute("success", "Ønsket er ikke længere reserveret");
            return "redirect:/overview";
        }

        wishListService.reserveWish(true, reservedByUserId, wishId);
        redirectAttributes.addFlashAttribute("success", "Ønsket er blevet reserveret");
        return "redirect:/overview";
    }
}