package com.example.sysc4806project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * A REST controller for controlling the recommendation functionality
 */
@RestController
public class RecommendationController {

    /**
     * The program wide user repository
     */
    private final UserRepository userRepository;

    /**
     * The recommendation service associated with the controller
     */
    private final RecommendationService recommendationService;

    /**
     * The number of similar users
     */
    private final int NUM_SIMILAR_USERS = 1;

    /**
     * The default controller
     * @param userRepository
     * @param recommendationService
     */
    @Autowired
    public RecommendationController(UserRepository userRepository, RecommendationService recommendationService) {
        this.userRepository = userRepository;
        this.recommendationService = recommendationService;
    }

    /**
     * Gets the recommendations for a user based on similar purchases
     * @return
     */
    @GetMapping("/recommendations")
    public String getRecommendations() {
        // Grab the current user account
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByName(name);

        if (currentUser != null) {
            // Get a list of users sorted by Jaccard similarity
            List<User> similarUsers = recommendationService.findUsersWithSimilarPurchases(currentUser, NUM_SIMILAR_USERS);

            if (similarUsers.isEmpty()) {
                return "No similar users found";
            }

            User mostSimilarUser = similarUsers.get(0);

            // Get books purchased by the most similar user
            List<Book> recommendedBooks = recommendationService.getBooksPurchasedByUser(mostSimilarUser);

            if (recommendedBooks.isEmpty()) {
                return "No recommended books found";
            }

            // TODO: improve format
            StringBuilder result = new StringBuilder("Recommended Books:\n");
            for (Book book : recommendedBooks) {
                result.append(book.toString()).append("\n");
            }

            return result.toString();
        } else {
            return "Invalid user ID";
        }
    }
}
