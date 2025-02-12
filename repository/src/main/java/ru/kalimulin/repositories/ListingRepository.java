package ru.kalimulin.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.kalimulin.models.Listing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.kalimulin.models.User;
import ru.kalimulin.util.ListingStatus;

import java.util.Optional;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long>, JpaSpecificationExecutor<Listing> {
    Page<Listing> findAll(Specification<Listing> specification, Pageable pageable);

    Page<Listing> findBySeller(User seller, Pageable pageable);

    long countBySellerAndStatus(User seller, ListingStatus status);

    boolean existsBySellerAndStatusAndBuyer(User seller, ListingStatus status, User buyer);


}