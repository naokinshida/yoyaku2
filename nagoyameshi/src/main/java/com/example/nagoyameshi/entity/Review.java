package com.example.nagoyameshi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int storeId;

    private String comment;
    
    private int star;

    // No-argument constructor (required by JPA)
    public Review() {}

    // Constructor with all arguments
    public Review(Integer id, int storeId, String comment, int star) {
        this.id = id;
        this.storeId = storeId;
        this.comment = comment;
        this.star = star;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

	public static boolean isEmpty() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}
}
