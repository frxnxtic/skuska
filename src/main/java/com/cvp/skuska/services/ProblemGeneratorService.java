package com.cvp.skuska.services;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ProblemGeneratorService {
    private final Random random = new Random();

    public String generateSimpleProblem() {
        System.out.println("Generating problem");
        int num1 = random.nextInt(100) + 1;
        int num2 = random.nextInt(100) + 1;
        System.out.println(num1 + " + " + num2);
        return num1 + " + " + num2;
    }

    public int getSolution(String problem) {
        String[] parts = problem.split(" \\+ ");
        int num1 = Integer.parseInt(parts[0].trim());
        int num2 = Integer.parseInt(parts[1].trim());
        return num1 + num2;
    }
}
