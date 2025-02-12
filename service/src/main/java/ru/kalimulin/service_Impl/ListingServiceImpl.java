package ru.kalimulin.service_Impl;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kalimulin.custum_exceptions.categoryException.CategoryNotFoundException;
import ru.kalimulin.custum_exceptions.listingException.ListingLimitExceededException;
import ru.kalimulin.custum_exceptions.listingException.ListingNotFoundException;
import ru.kalimulin.custum_exceptions.userException.UnauthorizedException;
import ru.kalimulin.custum_exceptions.userException.UserNotFoundException;
import ru.kalimulin.entity_dto.listingDTO.ListingCreateDTO;
import ru.kalimulin.entity_dto.listingDTO.ListingResponseDTO;
import ru.kalimulin.entity_dto.listingDTO.ListingUpdateDTO;
import ru.kalimulin.mappers.listingMapper.ListingMapper;
import ru.kalimulin.models.Category;
import ru.kalimulin.models.Listing;
import ru.kalimulin.models.User;
import ru.kalimulin.repositories.CategoryRepository;
import ru.kalimulin.repositories.ListingRepository;
import ru.kalimulin.repositories.UserRepository;
import ru.kalimulin.service.ListingService;
import ru.kalimulin.specification.ListingSpecification;
import ru.kalimulin.util.ListingStatus;
import ru.kalimulin.util.RoleName;
import ru.kalimulin.util.SessionUtils;

import java.math.BigDecimal;

@Service
public class ListingServiceImpl implements ListingService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ListingMapper listingMapper;
    private final ListingRepository listingRepository;

    private static final Logger logger = LoggerFactory.getLogger(ListingServiceImpl.class);

    public ListingServiceImpl(UserRepository userRepository, CategoryRepository categoryRepository, ListingMapper listingMapper, ListingRepository listingRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.listingMapper = listingMapper;
        this.listingRepository = listingRepository;
    }

    @Override
    public ListingResponseDTO findById(Long id) {
        logger.info("Поиск объявления с id {}", id);
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Объявление с id {} не найдено", id);
                    return new ListingNotFoundException("Объявление не найдено");
                });

        return listingMapper.toListingResponseDTO(listing);
    }

    @Override
    public Page<ListingResponseDTO> findAllListings(Pageable pageable) {
        logger.info("Поиск всех объявлений с пагинацией {}", pageable);
        Page<Listing> listingPage = listingRepository.findAll(pageable);
        return listingPage.map(listingMapper::toListingResponseDTO);
    }

    @Override
    public Page<ListingResponseDTO> searchListings(String title, String category, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        logger.info("Поиск с параметрами: title={}, category={}, minPrice={}, maxPrice={}", title, category, minPrice, maxPrice);
        Specification<Listing> specification = ListingSpecification.filterBy(title, category, minPrice, maxPrice);

        Page<Listing> listingPage = listingRepository.findAll(specification, pageable);
        return listingPage.map(listingMapper::toListingResponseDTO);
    }

    @Override
    public Page<ListingResponseDTO> findAllBySeller(String sellerEmail, Pageable pageable) {
        logger.info("Поиск объявлений пользователя с email: {}", sellerEmail);
        User seller = userRepository.findByEmail(sellerEmail)
                .orElseThrow(() -> {
                    logger.error("Пользователь с email {} не найден", sellerEmail);
                    return new UserNotFoundException("Пользователя с таким email " + sellerEmail + " не существует");
                });

        Page<Listing> listingPage = listingRepository.findBySeller(seller, pageable);
        return listingPage.map(listingMapper::toListingResponseDTO);
    }

    @Transactional
    @Override
    public ListingResponseDTO createListing(ListingCreateDTO listingCreateDTO, HttpSession session) {
        String userEmail = SessionUtils.getUserEmail(session);

        logger.info("Создание нового объявления пользователем с email {}", userEmail);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с таким email " + userEmail + " не найден"));

        boolean isPremium = user.getRoles().stream().anyMatch(role -> role.getRoleName() == RoleName.PREMIUM);
        boolean isSeller = user.getRoles().stream().anyMatch(role -> role.getRoleName() == RoleName.SELLER);

        if (isSeller && !isPremium) {
            long activeListingsCount = listingRepository.countBySellerAndStatus(user, ListingStatus.ACTIVE);

            if (activeListingsCount >= 10) {
                logger.warn("Пользователь с email {} превысил лимит объявлений", userEmail);
                throw new ListingLimitExceededException("Вы достигли лимита в 10 активных объявлений. Обновите до PREMIUM для снятия ограничений");
            }
        }

        Category category = categoryRepository.findById(listingCreateDTO.getCategory())
                .orElseThrow(() -> {
                    logger.error("Категория с id не найдена {}", listingCreateDTO.getCategory());
                    return new CategoryNotFoundException("Категория с таким id " + listingCreateDTO.getCategory() + " не найдена");
                });

        Listing listing = listingMapper.toListing(listingCreateDTO, user, category);
        Listing savedListing = listingRepository.save(listing);

        updateActiveListingsCount(user, 1);

        logger.info("Объявление с id {} успешно создано", savedListing.getId());
        return listingMapper.toListingResponseDTO(savedListing);
    }

    @Transactional
    @Override
    public ListingResponseDTO updateListing(Long id, ListingUpdateDTO listingUpdateDTO, HttpSession session) {
        String userEmail = SessionUtils.getUserEmail(session);
        logger.info("Обновление данный в объявлении с id {} пользователем с email {}", id, userEmail);

        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Объявление с id {} не найдено", id);
                    return new ListingNotFoundException("Объявление c таким id " + id + " не найдено");
                });

        if (!listing.getSeller().getEmail().equalsIgnoreCase(userEmail)) {
            logger.warn("Не авторизованный пользователь пытается изменить объявление с id {}", id);
            throw new UnauthorizedException("Вы не можете изменить это объявление");
        }

        if (listingUpdateDTO.getTitle() != null) {
            listing.setTitle(listingUpdateDTO.getTitle());
        }
        if (listingUpdateDTO.getDescription() != null) {
            listing.setDescription(listingUpdateDTO.getDescription());
        }
        if (listingUpdateDTO.getPrice() != null) {
            listing.setPrice(listingUpdateDTO.getPrice());
        }
        if (listingUpdateDTO.getBrand() != null) {
            listing.setBrand(listingUpdateDTO.getBrand());
        }
        if (listingUpdateDTO.getImageUrl() != null) {
            listing.setImageUrl(listingUpdateDTO.getImageUrl());
        }

        if (listingUpdateDTO.getCategory() != null) {
            Category category = categoryRepository.findById(listingUpdateDTO.getCategory())
                    .orElseThrow(() -> {
                        logger.error("Категория с id {} не найдена", listingUpdateDTO.getCategory());
                        return new CategoryNotFoundException("Категория с таким id " + listingUpdateDTO.getCategory() + " не найдена");
                    });

            listing.setCategory(category);
        }

        Listing updatedListing = listingRepository.save(listing);

        logger.info("Объявление с id {} успешно обновлено", id);

        return listingMapper.toListingResponseDTO(updatedListing);
    }

    @Transactional
    @Override
    public ListingResponseDTO changeListingStatus(Long id, ListingStatus newStatus, HttpSession session) {
        String userEmail = SessionUtils.getUserEmail(session);
        logger.info("Попытка смены статуса у объявления с id: {} на статус {}", id, newStatus);

        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Объявление с id {} не найдено", id);
                    return new ListingNotFoundException("Объявление не найдено");
                });

        if (!listing.getSeller().getEmail().equalsIgnoreCase(userEmail)) {
            logger.warn("Не авторизованный пользователь пытается изменить статус объявления с id {}", id);
            throw new UnauthorizedException("Вы не можете изменить статус в этом объявлении");
        }

        listing.setStatus(newStatus);

        if (newStatus == ListingStatus.SOLD || newStatus == ListingStatus.ARCHIVED) {
            updateActiveListingsCount(listing.getSeller(), -1);
        }

        if (newStatus == ListingStatus.ACTIVE) {
            updateActiveListingsCount(listing.getSeller(), 1);
        }

        listing.setStatus(newStatus);

        listingRepository.save(listing);

        logger.info("Статус объявления с id: {} изменен на {}", id, newStatus);

        return listingMapper.toListingResponseDTO(listing);
    }

    @Transactional
    @Override
    public void deleteListing(Long id, HttpSession session) {
        String userEmail = SessionUtils.getUserEmail(session);
        logger.info("Удаление объявления с id: {} пользователем с email: {}", id, userEmail);

        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Объявление с  id {} не найдено", id);
                    return new ListingNotFoundException("Объявление не найдено");
                });

        if (!listing.getSeller().getEmail().equalsIgnoreCase(userEmail)) {
            logger.warn("Не авторизованный пользователь пытается удалить объявление с id {}", id);
            throw new UnauthorizedException("Вы не можете удалить это объявление");
        }

        updateActiveListingsCount(listing.getSeller(), -1);

        listingRepository.delete(listing);
        logger.info("Объявление c id {} успешно удалено", id);
    }

    /**
     * Метод для подсчета активных объявлений у пользователя
     * @param seller пользователь у которого считаем объявления со статусом ACTIVE
     * @param count передаем количество которое нужно отнять (-1) или прибавить (+1)
     */
    private void updateActiveListingsCount(User seller, int count) {
        logger.info("Обновление счетчика активных объявлений у пользователя с email: {}", seller.getEmail());
        long activeListingCount = listingRepository.countBySellerAndStatus(seller, ListingStatus.ACTIVE);

        long newCount = activeListingCount + count;

        if (newCount < 0) {
            logger.error("Счетчик не может быть отрицательным");
            throw new IllegalStateException("Количество объявлений не может быть отрицательным");
        }
    }
}