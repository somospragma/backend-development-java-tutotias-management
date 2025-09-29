package com.pragma.usuarios.infrastructure.adapter.output.persistence.repository;

import com.pragma.usuarios.infrastructure.adapter.output.persistence.entity.UsersEntity;
import com.pragma.usuarios.domain.model.enums.RolUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataUserRepository extends JpaRepository<UsersEntity, String> {
    Optional<UsersEntity> findByEmail(String email);
    Optional<UsersEntity> findByGoogleUserId(String googleUserId);
    
    @Query("SELECT u FROM UsersEntity u WHERE " +
           "(:chapterId IS NULL OR u.chapter.id = :chapterId) AND " +
           "(:rol IS NULL OR LOWER(CAST(u.rol AS string)) LIKE LOWER(CONCAT('%', :rol, '%'))) AND " +
           "(:seniority IS NULL OR u.seniority = :seniority) AND " +
           "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')))")
    List<UsersEntity> findByFilters(@Param("chapterId") String chapterId, 
                                   @Param("rol") String rol, 
                                   @Param("seniority") Integer seniority, 
                                   @Param("email") String email);
}