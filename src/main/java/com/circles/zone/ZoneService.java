package com.circles.zone;

import com.circles.domain.Account;
import com.circles.domain.Zone;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ZoneService {
    private final ZoneReopsitory zoneReopsitory;

    @PostConstruct
    public void initZoneData() throws IOException {
        if (zoneReopsitory.count() == 0) {
            Resource resource = new ClassPathResource("cityOfKorea.csv");
            List<Zone> excelLists = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8).stream()
                    .map(each -> {
                        String[] split = each.split(",");
                        return Zone.builder().city(split[0]).localNameOfCity(split[1]).province(split[2]).build();
                    }).collect(Collectors.toList());

            zoneReopsitory.saveAll(excelLists);
        }
    }

    public void addZone(Zone zone) {
        zoneReopsitory.save(zone);
    }

    public Zone findByCityAndProvince(String cityName, String provinceName) {
        return zoneReopsitory.findByCityAndProvince(cityName, provinceName);
    }


    public List<Zone> findAll() {
        return zoneReopsitory.findAll();
    }
}