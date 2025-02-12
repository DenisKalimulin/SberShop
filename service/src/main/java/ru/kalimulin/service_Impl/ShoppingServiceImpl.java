package ru.kalimulin.service_Impl;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kalimulin.custum_exceptions.listingException.ListingNotFoundException;
import ru.kalimulin.custum_exceptions.listingException.ListingUnavailableException;
import ru.kalimulin.models.Listing;
import ru.kalimulin.models.User;
import ru.kalimulin.repositories.ListingRepository;
import ru.kalimulin.repositories.UserRepository;
import ru.kalimulin.service.ShoppingService;
import ru.kalimulin.service.WalletService;
import ru.kalimulin.util.ListingStatus;
import ru.kalimulin.util.SessionUtils;

@Service
public class ShoppingServiceImpl implements ShoppingService {
    private final ListingRepository listingRepository;
    private final UserRepository userRepository;
    private final WalletService walletService;

    public ShoppingServiceImpl(ListingRepository listingRepository, UserRepository userRepository, WalletService walletService) {
        this.listingRepository = listingRepository;
        this.userRepository = userRepository;
        this.walletService = walletService;
    }

    @Transactional
    @Override
    public void buyListing(Long listingId, HttpSession session) {
        String buyerEmail = SessionUtils.getUserEmail(session);

        User buyer = userRepository.findByEmail(buyerEmail).get();

        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new ListingNotFoundException("Объявление не найдено"));

        if(listing.getStatus() == ListingStatus.SOLD || listing.getStatus() == ListingStatus.ARCHIVED) {
            throw new ListingUnavailableException("Товар продан или находится в архиве");
        }

        User seller = listing.getSeller();

        walletService.transfer(buyer, seller, listing.getPrice());

        listing.setStatus(ListingStatus.SOLD);
        listing.setBuyer(buyer);
        listingRepository.save(listing);

    }
}