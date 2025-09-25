package com.pragma.usuarios.infrastructure.adapter.output.external.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PragmaUserDto {
    @JsonProperty("pragmatic_id")
    private Integer pragmaticId;
    
    @JsonProperty("full_name")
    private String fullName;
    
    private String email;
    
    private Location location;
    
    @JsonProperty("profile_photo")
    private String profilePhoto;
    
    @JsonProperty("view_onboarding")
    private Boolean viewOnboarding;
    
    private List<Chapter> chapters;
    
    @Data
    public static class Location {
        private String ciudad;
        private String departamento;
        private String pais;
    }
    
    @Data
    public static class Chapter {
        @JsonProperty("order_id")
        private Integer orderId;
        
        @JsonProperty("order_name")
        private String orderName;
        
        @JsonProperty("chapter_id")
        private Integer chapterId;
        
        @JsonProperty("chapter_name")
        private String chapterName;
        
        @JsonProperty("seniority_id")
        private Integer seniorityId;
        
        @JsonProperty("seniority_name")
        private String seniorityName;
        
        @JsonProperty("level_id")
        private Integer levelId;
        
        @JsonProperty("level_name")
        private String levelName;
        
        @JsonProperty("speciality_id")
        private Integer specialityId;
        
        @JsonProperty("speciality_name")
        private String specialityName;
        
        @JsonProperty("technology_id")
        private Integer technologyId;
        
        @JsonProperty("technology_name")
        private String technologyName;
        
        @JsonProperty("next_estimated_valoration_date")
        private LocalDateTime nextEstimatedValorationDate;
        
        @JsonProperty("level_id_chapter_administration")
        private Integer levelIdChapterAdministration;
        
        @JsonProperty("is_autovaloration_active")
        private Boolean isAutovalorationActive;
    }
}