package com.example.sysc4806project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * A class for creating a service for the recommendations
 */
@Service
public class RecommendationService {

    /**
     * The database for the purchase history
     */
    private final PurchaseHistoryRepository purchaseHistoryRepository;

    /**
     * The database for the users
     */
    private final UserRepository userRepository;

    /**
     * The default constructor
     * @param purchaseHistoryRepository
     * @param userRepository
     */
    @Autowired
    public RecommendationService(PurchaseHistoryRepository purchaseHistoryRepository, UserRepository userRepository) {
        this.purchaseHistoryRepository = purchaseHistoryRepository;
        this.userRepository = userRepository;
    }

    /**
     * Returns how much of a similarity two users have based on their purchase history
     *
     * @param user1
     * @param user2
     * @return
     */
    public double calculateJaccardSimilarity(User user1, User user2) {
        List<Book> booksUser1 = getBooksPurchasedByUser(user1);
        List<Book> booksUser2 = getBooksPurchasedByUser(user2);

        int intersectionSize = calculateIntersectionSize(booksUser1, booksUser2);
        int unionSize = calculateUnionSize(booksUser1, booksUser2);

        return (double) intersectionSize / unionSize;
    }

    /**
     * Retrieves a list of books purchased by a given user.
     *
     * @param user The user for whom to retrieve the purchase history.
     * @return A list of books purchased by the user.
     */
    public List<Book> getBooksPurchasedByUser(User user) {
        List<PurchaseHistory> purchaseHistoryList = purchaseHistoryRepository.findByUser(user);
        return extractBooksFromPurchaseHistory(purchaseHistoryList);
    }

    /**
     * Extracts books from a list of purchase history records.
     *
     * @param purchaseHistoryList The list of purchase history records.
     * @return A list of books extracted from the purchase history.
     */
    private List<Book> extractBooksFromPurchaseHistory(List<PurchaseHistory> purchaseHistoryList) {
        List<Book> books = new ArrayList<>();
        for (PurchaseHistory purchaseHistory : purchaseHistoryList) {
            books.add(purchaseHistory.getBook());
        }
        return books;
    }

    /**
     * Calculates the size of the intersection between two lists of books.
     *
     * @param list1 The first list of books.
     * @param list2 The second list of books.
     * @return The size of the intersection between the two lists.
     */
    private int calculateIntersectionSize(List<Book> list1, List<Book> list2) {
        return (int) list1.stream()
                .filter(list2::contains)
                .count();
    }

    /**
     * Calculates the size of the union between two lists of books.
     *
     * @param list1 The first list of books.
     * @param list2 The second list of books.
     * @return The size of the union between the two lists.
     */
    private int calculateUnionSize(List<Book> list1, List<Book> list2) {
        List<Book> union = new ArrayList<>(list1);
        union.addAll(list2);
        return (int) union.stream()
                .distinct()
                .count();
    }

    /**
     * Finds users with similar purchases to the given user.
     *
     * @param targetUser The user for whom to find similar users.
     * @return A list of users sorted by Jaccard similarity in descending order.
     */
    public List<User> findUsersWithSimilarPurchases(User targetUser, int numberOfUsers) {
        List<User> allUsers = userRepository.findAll();
        List<User> usersWithSimilarPurchases = new ArrayList<>(allUsers);

        // Remove the targetUser from the list
        usersWithSimilarPurchases.remove(targetUser);

        // Calculate Jaccard similarity for each user and the targetUser
        usersWithSimilarPurchases.sort(Comparator.comparingDouble(user -> -calculateJaccardSimilarity(targetUser, user)));

        // Keep only the top N users
        if (usersWithSimilarPurchases.size() > numberOfUsers) {
            usersWithSimilarPurchases.subList(numberOfUsers, usersWithSimilarPurchases.size()).clear();
        }

        return usersWithSimilarPurchases;
    }

}
