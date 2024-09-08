package com.new3seagull.SeagullsRoom.domain.screenTime.repository;

import com.new3seagull.SeagullsRoom.domain.screenTime.entity.ScreenTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreenTimeRepository extends JpaRepository<ScreenTime, Long> {

}
