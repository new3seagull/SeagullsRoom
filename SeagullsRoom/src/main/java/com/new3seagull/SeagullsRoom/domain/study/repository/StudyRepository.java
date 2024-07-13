package com.new3seagull.SeagullsRoom.domain.study.repository;

import com.new3seagull.SeagullsRoom.domain.study.entity.Study;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {
    List<Study> findAllByUser(User user);

    Page<Study> findAllByStudyDateOrderByStudyTimeDesc(Pageable pageable, LocalDate studyDate);

}
