package com.circles.zone;

import com.circles.domain.Zone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoneReopsitory extends JpaRepository<Zone,Long> {

    Zone findByName(String zoneName);
}
