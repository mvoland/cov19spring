package com.mvoland.cov19api.data.repository;

import com.mvoland.cov19api.data.entity.DepartementalHospitalisation;
import com.mvoland.cov19api.data.entity.RegionalHospitalisation;
import org.springframework.data.repository.CrudRepository;

public interface DepartementalHospitalisationRepository extends CrudRepository<DepartementalHospitalisation, Long> {

}
