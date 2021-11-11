package com.my.hmc.repository;

import com.my.hmc.domain.ReviewQuestion;
import com.my.hmc.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewQuestionRepository extends JpaRepository<ReviewQuestion, Long> {

    Page<ReviewQuestion> findAll(Pageable pageable);

    List<ReviewQuestion> findByQuestionUser(User user);

    void deleteById(Long id);

    @Query("select count (rq.id) > 0 " +
                "from ReviewQuestion rq " +
                "where rq.id = :id and rq.questionUser.id = :userId")
    boolean existsByUserAndId(Long userId, Long id);
}
