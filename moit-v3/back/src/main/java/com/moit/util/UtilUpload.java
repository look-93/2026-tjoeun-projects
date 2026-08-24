package com.moit.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

@Component //내가 만든 부품이야
public class UtilUpload {
	@Value("${resource.path}") private String resourcePath;// C:/upload -> application.properties

	
	// 방식 A: 경로를 지정 안 하면 기본 경로(defaultPath) 사용
    public String fileUpload(MultipartFile file) throws IOException {	
        return fileUpload(file, resourcePath); 
    }
    
    // 방식 B: 경로를 지정하면 해당 경로 사용 (핵심 로직은 여기서 처리)
    public String fileUpload(MultipartFile file, String subPath) throws IOException {

        // C:/upload
        File directory = new File(resourcePath);

        // C:/upload/meetup
        if (subPath != null && !subPath.isBlank()) {
            directory = new File(directory, subPath);
        }

        if (!directory.exists() && !directory.mkdirs()) {
            throw new IOException(
                "업로드 폴더 생성 실패: " + directory.getAbsolutePath()
            );
        }

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IOException("파일명이 없습니다.");
        }

        String saveFilename =
                UUID.randomUUID() + "_" + originalFilename;

        File target = new File(directory, saveFilename);

        FileCopyUtils.copy(file.getBytes(), target);

        return saveFilename;
    }
    
    public void fileDelete(String fileName, String subPath) {

        File file = new File(
            resourcePath,
            subPath + File.separator + fileName
        );

        System.out.println("🔥 삭제 대상 파일 = " + file.getAbsolutePath());

        if (file.exists()) {

            boolean deleted = file.delete();

            System.out.println("🔥 파일 삭제 결과 = " + deleted);

            if (!deleted) {
                throw new RuntimeException(
                    "파일 삭제에 실패했습니다: " + file.getAbsolutePath()
                );
            }

        } else {
            System.out.println("⚠️ 삭제할 파일이 존재하지 않습니다.");
        }
    }
//	public String fileUpload(MultipartFile file) throws IOException {	
//		//1. 파일 이름 중복안되게
//		UUID uid = UUID.randomUUID();
//		String save = uid.toString() + "_" + file.getOriginalFilename();
//		//2. 파일업로드
//		File target = new File(resourcePath , save);
//		FileCopyUtils.copy(file.getBytes(), target);
//		return save;		
//	}
	
	//1. 업로드 : 팀미션 : 확장자, 용량 사이즈 고려
	//2. 팀미션 : 글삭제시 파일도 같이 삭제
}
