package com.mvoland.cov19api.data.repository;

import com.mvoland.cov19api.data.entity.Region;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegionRepository extends CrudRepository<Region, Long> {

    Region findByRegionNumber(Integer regionNumber);
}