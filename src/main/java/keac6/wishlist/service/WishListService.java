package keac6.wishlist.service;

import keac6.wishlist.model.User;
import keac6.wishlist.model.Wish;
import keac6.wishlist.model.WishList;
import keac6.wishlist.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public void saveNewWish(Wish newWish){
        wishListRepository.saveNewWish(newWish);
    }

    public User findByEmail(String email) {
        return wishListRepository.findByEmail(email);
    }

    public boolean authenticate(String inputPassword, String hashedPassword) {
        return passwordEncoder.matches(inputPassword, hashedPassword);
    }

    public void createWishList(WishList newWishList) {
        wishListRepository.createWishList(newWishList);
    }
}

