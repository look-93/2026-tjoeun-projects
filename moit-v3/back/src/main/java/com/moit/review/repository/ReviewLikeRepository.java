package com.moit.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moit.review.entity.ReviewLike;


@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

}
