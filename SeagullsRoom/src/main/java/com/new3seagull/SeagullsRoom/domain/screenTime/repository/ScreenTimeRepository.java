package com.new3seagull.SeagullsRoom.domain.screenTime.repository;

import com.new3seagull.SeagullsRoom.domain.screenTime.entity.ScreenTime;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreenTimeRepository extends JpaRepository<ScreenTime, Long> {

    @Query("SELECT st.category, SUM(st.count) FROM ScreenTime st " +
        "WHERE st.user = :user AND DATE(st.updatedAt) = CURRENT_DATE " +
        "GROUP BY st.category")
    List<Object[]> findScreenTimesByUserGroupByCategory(@Param("user") User user);
}
