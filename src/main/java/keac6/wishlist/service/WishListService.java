package keac6.wishlist.service;

import keac6.wishlist.model.User;
import keac6.wishlist.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishListService {
    private final WishListRepository wishListRepository;

    @Autowired
    public WishListService(WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }

    public void saveNewUser(User newUser) {
        wishListRepository.saveNewUser(newUser);
    }
}
