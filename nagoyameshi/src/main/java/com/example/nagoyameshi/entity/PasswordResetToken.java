package com.example.nagoyameshi.entity;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "password_reset_token")
@Data
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(targetEntity = Memberinfo.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private Memberinfo user;

    private Date expiryDate;

    public boolean isExpired() {
        return expiryDate.before(new java.util.Date());
    }

	public void setExpiryDate(java.util.Date calculateExpiryDate) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}