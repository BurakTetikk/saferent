package com.saferent.repository;

import com.saferent.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "roles") // defaultta LAZY olan rol bilgilerini EAGER yaptık--- bu anatasyon olmasaydı role fieldı lazy olduğu için metodla beraber gelmeeycekti ayrıca roles.getRoles() yapmamız gerekecekti
    Optional<User> findByEmail(String email);

}
