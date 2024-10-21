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

    @Query(value = "SELECT s.user_id, " +
            "SUM(EXTRACT(HOUR FROM s.study_time) * 3600 + EXTRACT(MINUTE FROM s.study_time) * 60 + EXTRACT(SECOND FROM s.study_time)) AS totalStudyTimeInSeconds " +
            "FROM Study s " +
            "WHERE EXTRACT(YEAR FROM s.study_date) = :year " +
            "AND EXTRACT(MONTH FROM s.study_date) = :month " +
            "AND EXTRACT(DAY FROM s.study_date) = :day " +
            "GROUP BY s.user_id " +
            "ORDER BY totalStudyTimeInSeconds DESC " +
            "LIMIT :pageSize OFFSET :offset",
            nativeQuery = true)
    List<Object[]> findStudyTimeRankingByDateInSeconds(@Param("year") int year,
                                                       @Param("month") int month,
                                                       @Param("day") int day,
                                                       @Param("pageSize") int pageSize,
                                                       @Param("offset") int offset);


    @Query(value = "SELECT s.user_id, " +
            "SUM(EXTRACT(HOUR FROM s.study_time) * 3600 + EXTRACT(MINUTE FROM s.study_time) * 60 + EXTRACT(SECOND FROM s.study_time)) AS totalStudyTimeInSeconds " +
            "FROM Study s " +
            "WHERE EXTRACT(YEAR FROM s.study_date) = :year " +
            "AND EXTRACT(MONTH FROM s.study_date) = :month " +
            "AND EXTRACT(DAY FROM s.study_date) = :day " +
            "GROUP BY s.user_id " +
            "ORDER BY totalStudyTimeInSeconds DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Object[]> findTop10StudyTimeByDate(@Param("year") int year, @Param("month") int month, @Param("day") int day);
}
