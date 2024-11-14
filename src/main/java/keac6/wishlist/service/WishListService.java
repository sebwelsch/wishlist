package keac6.wishlist.service;

import keac6.wishlist.model.User;
import keac6.wishlist.model.Wish;
import keac6.wishlist.model.WishList;
import keac6.wishlist.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class WishListService {
    private final WishListRepository wishListRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public WishListService(WishListRepository wishListRepository, PasswordEncoder passwordEncoder) {
        this.wishListRepository = wishListRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveNewUser(User newUser) {
        String hashedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(hashedPassword);
        wishListRepository.saveNewUser(newUser);
    }

    public void saveNewWish(Wish newWish) {
        wishListRepository.saveNewWish(newWish);
    }

    public User getUserByEmail(String email) {
        return wishListRepository.getUserByEmail(email);
    }

    public User getUserById(int id) {
        return wishListRepository.getUserById(id);
    }

    public boolean authenticate(String inputPassword, String hashedPassword) {
        return passwordEncoder.matches(inputPassword, hashedPassword);
    }

    public void createWishList(WishList newWishList) {
        wishListRepository.createWishList(newWishList);
    }

    public ArrayList<WishList> getAllWishLists(int userid) {
        return wishListRepository.getAllWishLists(userid);
    }

    public WishList getWishListById(int wishListId) {
        return wishListRepository.getWishListById(wishListId);
    }

    public ArrayList<Wish> getWishesByWishListId(int wishListId) {
        return wishListRepository.getWishesByWishListId(wishListId);
    }

    public void deleteWishList(int wishListId) {
        wishListRepository.deleteWishList(wishListId);
    }

    public void deleteWish(int wishId) {
        wishListRepository.deleteWish(wishId);
    }

    public Wish findWishById(int wish_id) {
        return wishListRepository.findWishById(wish_id);
    }

    public void reserveWish(boolean reserved, int reservedByUserId, int wishId) {
        wishListRepository.reserveWish(reserved, reservedByUserId, wishId);
    }
}

