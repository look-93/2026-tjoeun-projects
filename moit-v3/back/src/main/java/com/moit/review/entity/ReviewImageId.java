package com.moit.review.entity;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class ReviewImageId implements Serializable {

    private Long reviewId;

    private Long imageId;

}