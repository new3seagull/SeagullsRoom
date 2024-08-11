package com.new3seagull.SeagullsRoom.domain.study.repository;

import com.new3seagull.SeagullsRoom.domain.study.entity.Study;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {

    List<Study> findAllByUser(User user);

    Study findByUserAndStudyDate(User user, LocalDate studyDate);

    @Query("SELECT s FROM Study s WHERE s.user = :user AND FUNCTION('YEAR', s.studyDate) = :year AND FUNCTION('MONTH', s.studyDate) = :month")
    List<Study> findByUserAndYearAndMonth(@Param("user") User user, @Param("year") int year, @Param("month") int month);

    //사용자의 특정 날짜의 공부시간 조회 메서드
    @Query("SELECT s.studyTime FROM Study s WHERE s.user = :user AND FUNCTION('YEAR', s.studyDate) = :year AND FUNCTION('MONTH', s.studyDate) = :month AND FUNCTION('DAY', s.studyDate) = :day")
    List<LocalTime> findTotalStudyTimeByUserIdAndDate(@Param("user") User user, @Param("year") int year, @Param("month") int month, @Param("day") int day);

    @Query("SELECT s.user, " +
            "SUM(HOUR(s.studyTime) * 60 + MINUTE(s.studyTime)) AS totalStudyTimeInMinutes " +
            "FROM Study s " +
            "WHERE FUNCTION('YEAR', s.studyDate) = :year " +
            "AND FUNCTION('MONTH', s.studyDate) = :month " +
            "AND FUNCTION('DAY', s.studyDate) = :day " +
            "GROUP BY s.user " +
            "ORDER BY totalStudyTimeInMinutes DESC")
    Page<Object[]> findStudyTimeRankingByDate(@Param("year") int year,
                                              @Param("month") int month,
                                              @Param("day") int day,
                                              Pageable pageable);
}
