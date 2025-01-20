package com.cvp.skuska.controllers;

import com.cvp.skuska.models.UserAttempt;
import com.cvp.skuska.repositories.UserAttemptRepository;
import com.cvp.skuska.services.ProblemGeneratorService;
import com.cvp.skuska.services.RedisService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class MainController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RedisService redisService;
    private final UserAttemptRepository userAttemptRepository;
    private final ProblemGeneratorService problemGeneratorService;


    public MainController(KafkaTemplate<String, String> kafkaTemplate,
                          RedisService redisService,
                          UserAttemptRepository userAttemptRepository,
                          ProblemGeneratorService problemGeneratorService) {
        this.kafkaTemplate = kafkaTemplate;
        this.redisService = redisService;
        this.userAttemptRepository = userAttemptRepository;
        this.problemGeneratorService = problemGeneratorService;
    }



    @GetMapping
    public String homePage(Model model) {
        model.addAttribute("uid", "");
        model.addAttribute("problem", problemGeneratorService.generateSimpleProblem());
        model.addAttribute("rank", "N/A");
        model.addAttribute("score", "N/A");
        return "index";
    }

    @PostMapping("/submit")
    public String submitAttempt(@RequestParam String userId,
                                @RequestParam String userAnswer,
                                @RequestParam String problem,
                                Model model) {

        // Mock problem for now
        boolean isCorrect = problemGeneratorService.getSolution(problem) == Integer.parseInt(userAnswer);

        // Log attempt in PostgreSQL
        UserAttempt attempt = new UserAttempt();
        attempt.setUserId(userId);
        attempt.setProblem(problem);
        attempt.setUserAnswer(userAnswer);
        attempt.setCorrect(isCorrect);
        userAttemptRepository.save(attempt);

        // Update score in Redis
        if (isCorrect) {
            redisService.incrementUserScore(userId, 1); // Increment score by 1 for correct answer
        }

        // Fetch updated rank and score
        Long rank = redisService.getUserRank(userId);
        Double score = redisService.getUserScore(userId);

        model.addAttribute("uid", userId);
        model.addAttribute("problem", problemGeneratorService.generateSimpleProblem());
        model.addAttribute("rank", (rank != null) ? "#" + rank : "N/A");
        model.addAttribute("score", (score != null) ? score.intValue() : "N/A");
        return "index";
    }
}
