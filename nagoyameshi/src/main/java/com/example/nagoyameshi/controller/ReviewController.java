package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.Review;
import com.example.nagoyameshi.entity.Storeinfo;
import com.example.nagoyameshi.repository.StoreinfoRepository;
import com.example.nagoyameshi.service.ReviewService;

@Controller
public class ReviewController {

    @Autowired
    private StoreinfoRepository storeInfoRepository;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/review/{id}")
    public String getStoreinfo(@PathVariable("id") Integer id, Model model) {
        Storeinfo storeinfo = storeInfoRepository.getReferenceById(id);
        List<Review> reviews = reviewService.findByStoreId(id);
        double averageScore = reviews.stream().mapToInt(Review::getStar).average().orElse(0.0);

        model.addAttribute("storeinfo", storeinfo);
        model.addAttribute("reviews", reviews);
        model.addAttribute("averageScore", averageScore);

        return "reviews/review";
    }

    @PostMapping("/review")
    public String postReview(
            @RequestParam("storeId") int storeId,
            @RequestParam("comment") String comment,
            @RequestParam("star") int star) {

        Review newReview = new Review();
        newReview.setStoreId(storeId);
        newReview.setComment(comment);
        newReview.setStar(star);

        reviewService.save(newReview);
        return "redirect:/storeinfo/" + storeId; // レビュー送信後に店舗詳細画面にリダイレクトする
    }
}