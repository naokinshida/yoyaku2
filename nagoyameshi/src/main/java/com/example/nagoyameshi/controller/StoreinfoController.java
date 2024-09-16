package com.example.nagoyameshi.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Favorite;
import com.example.nagoyameshi.entity.Memberinfo;
import com.example.nagoyameshi.entity.Storeinfo;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.repository.CategoryRepository;
import com.example.nagoyameshi.repository.StoreinfoRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.FavoriteService;

@Controller
@RequestMapping("/storeinfo")
public class StoreinfoController {

    private final StoreinfoRepository storeinfoRepository;
    private final CategoryRepository categoryRepository;
    private final FavoriteService favoriteService; // 新しく追加

    public StoreinfoController(StoreinfoRepository storeinfoRepository, 
                               CategoryRepository categoryRepository, 
                               FavoriteService favoriteService) {
        this.storeinfoRepository = storeinfoRepository;
        this.categoryRepository = categoryRepository;
        this.favoriteService = favoriteService; // 新しく追加
    }

    // 店舗一覧の表示
    @GetMapping
    public String index(@RequestParam(name = "keyword", required = false) String keyword,
                        @RequestParam(name = "area", required = false) String area,
                        @RequestParam(name = "category", required = false) String categoryId,
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
                        Model model) {
        
        Page<Storeinfo> storeinfoPage;

        if (keyword != null && !keyword.isEmpty()) {
            storeinfoPage = storeinfoRepository.findByStorenameLikeOrAddressLike("%" + keyword + "%", "%" + keyword + "%", pageable);
        } else if (area != null && !area.isEmpty()) {
            storeinfoPage = storeinfoRepository.findByAddressLike("%" + area + "%", pageable);
        } else if (categoryId != null) {
            storeinfoPage = storeinfoRepository.findByCategoriesId(categoryId, pageable);
        } else {
            storeinfoPage = storeinfoRepository.findAll(pageable);
        }

        List<Category> categories = categoryRepository.findAll();

        model.addAttribute("storeinfoPage", storeinfoPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("area", area);
        model.addAttribute("categories", categories);

        return "storeinfo/index";
    }

    // 店舗詳細の表示
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, Model model) {
        Storeinfo storeinfo = storeinfoRepository.getReferenceById(id);
        model.addAttribute("storeinfo", storeinfo);
        model.addAttribute("reservationInputForm", new ReservationInputForm());

        // ユーザーがログインしている場合に、お気に入り状態を確認する
        // ※必要に応じてユーザー情報を使って、お気に入り登録済みかをチェック
        return "storeinfo/show";
    }

    // お気に入り登録機能
    @PostMapping("/{id}/favorite")
    public String addFavorite(@PathVariable("id") Integer storeId,
                              @AuthenticationPrincipal UserDetailsImpl userDetails, // 認証済みユーザー情報を取得
                              Model model) {
        // ユーザーがログインしていない場合は、ログインページにリダイレクト
        if (userDetails == null) {
            return "redirect:/login";
        }

        // ログイン中のユーザーの情報を取得
        Memberinfo memberinfo = userDetails.getMemberinfo();

        // お気に入りの追加処理
        Favorite favorite = new Favorite();
        favorite.setStoreId(storeId);
        favorite.setUserId(memberinfo.getId()); // MemberinfoのIDを設定
        favorite.setList("Default List");

        favoriteService.addFavorite(favorite);

        return "redirect:/storeinfo/" + storeId;
    }
    @GetMapping("/favorites")
    public String viewFavorites(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        if (userDetails == null || userDetails.getMemberinfo() == null) {
            return "redirect:/login";
        }

        Memberinfo memberinfo = userDetails.getMemberinfo();
        List<Favorite> favorites = favoriteService.getFavoritesByUserId(memberinfo.getId());

        // ログを追加
        System.out.println("Found " + favorites.size() + " favorites for user: " + memberinfo.getId());

        model.addAttribute("favorites", favorites);
        return "storeinfo/favorites";
    }

}