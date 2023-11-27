package com.example.sysc4806project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class RecommendationController {

    private final UserRepository userRepository;
    private final RecommendationService recommendationService;


    private final int NUM_SIMILAR_USERS = 1;

    @Autowired
    public RecommendationController(UserRepository userRepository, RecommendationService recommendationService) {
        this.userRepository = userRepository;
        this.recommendationService = recommendationService;
    }

    @GetMapping("/recommendations")
    public String getRecommendations(@RequestParam("userId") long userId) {
        User currentUser = userRepository.findById(userId);

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
