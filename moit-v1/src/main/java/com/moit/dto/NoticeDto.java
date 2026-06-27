package com.moit.dto;


import lombok.Data;

@Data
public class NoticeDto {
    private int id;
    private int member_id;
    private String title;
    private String content;
    private int view_count;
    private boolean is_fixed;
    private boolean delete_yn;
    private String created_by;
    private String updated_by;
    private String updated_at;
}
