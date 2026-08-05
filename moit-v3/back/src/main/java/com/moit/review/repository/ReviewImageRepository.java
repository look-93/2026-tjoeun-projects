package com.moit.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moit.review.entity.ReviewImage;



@Repository
public interface ReviewImageRepository extends JpaRepository <ReviewImage,Long> {

}
