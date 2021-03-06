package com.mvoland.cov19api.covidstat.hospitalisation.service;

import com.mvoland.cov19api.common.util.ParsingUtils;
import com.mvoland.cov19api.covidstat.hospitalisation.data.entity.DepartmentalHospitalisation;
import com.mvoland.cov19api.covidstat.hospitalisation.data.entity.DepartmentalNewHospitalisation;
import com.mvoland.cov19api.covidstat.hospitalisation.data.entity.RegionalHospitalisation;
import com.mvoland.cov19api.covidstat.hospitalisation.data.entity.RegionalIntensiveCareAdmission;
import com.mvoland.cov19api.covidstat.hospitalisation.data.repository.DepartmentalHospitalisationRepository;
import com.mvoland.cov19api.covidstat.hospitalisation.data.repository.DepartmentalNewHospitalisationRepository;
import com.mvoland.cov19api.covidstat.hospitalisation.data.repository.RegionalHospitalisationRepository;
import com.mvoland.cov19api.covidstat.hospitalisation.data.repository.RegionalIntensiveCareAdmissionRepository;
import com.mvoland.cov19api.covidstat.hospitalisation.exception.HospitalisationNotFoundException;
import com.mvoland.cov19api.covidstat.locality.data.entity.Region;
import com.mvoland.cov19api.covidstat.locality.service.LocalityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HospitalisationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HospitalisationService.class);

    private final RegionalIntensiveCareAdmissionRepository regionalAdmissionsRepository;
    private final RegionalHospitalisationRepository regionalHospitalisationRepository;
    private final DepartmentalNewHospitalisationRepository departmentalNewHospitalisationRepository;
    private final DepartmentalHospitalisationRepository departmentalHospitalisationRepository;
    private final LocalityService localityService;

    @Autowired
    public HospitalisationService(
            RegionalIntensiveCareAdmissionRepository regionalAdmissionsRepository,
            RegionalHospitalisationRepository regionalHospitalisationRepository,
            DepartmentalNewHospitalisationRepository departmentalNewHospitalisationRepository,
            DepartmentalHospitalisationRepository departmentalHospitalisationRepository,
            LocalityService localityService
    ) {
        this.regionalAdmissionsRepository = regionalAdmissionsRepository;
        this.regionalHospitalisationRepository = regionalHospitalisationRepository;
        this.departmentalNewHospitalisationRepository = departmentalNewHospitalisationRepository;
        this.departmentalHospitalisationRepository = departmentalHospitalisationRepository;
        this.localityService = localityService;
    }


    @Transactional
    public RegionalIntensiveCareAdmission updateRegionalIntensiveCareAdmission(RegionalIntensiveCareAdmission admission) {
        return regionalAdmissionsRepository.findByRegionAndNoticeDate(admission.getRegion(), admission.getNoticeDate())
                .map(existingAdmission -> {
                    if (existingAdmission.equals(admission)) {
                        return existingAdmission;
                    } else {
                        existingAdmission.setIntensiveCareAdmissionCount(admission.getIntensiveCareAdmissionCount());
                        return regionalAdmissionsRepository.save(existingAdmission);
                    }
                })
                .orElseGet(() -> regionalAdmissionsRepository.save(admission)
                );
    }

    public Optional<RegionalIntensiveCareAdmission> safeUpdateRegionalIntensiveCareAdmission(RegionalIntensiveCareAdmission admission) {
        try {
            return Optional.of(updateRegionalIntensiveCareAdmission(admission));
        } catch (Exception e) {
            LOGGER.warn("Could not update {}", admission);
            return Optional.empty();
        }
    }

    @Transactional
    public RegionalHospitalisation updateRegionalHospitalisation(RegionalHospitalisation hospitalisation) {
        return regionalHospitalisationRepository.findByRegionAndNoticeDateAndAgeGroup(hospitalisation.getRegion(), hospitalisation.getNoticeDate(), hospitalisation.getAgeGroup())
                .map(existingHospitalisation -> {
                    if (existingHospitalisation.equals(hospitalisation)) {
                        return existingHospitalisation;
                    } else {
                        existingHospitalisation.setCurrentHospitalizedCount(hospitalisation.getCurrentHospitalizedCount());
                        existingHospitalisation.setCurrentIntensiveCareCount((hospitalisation.getCurrentIntensiveCareCount()));
                        existingHospitalisation.setCurrentRadiationCount(hospitalisation.getCurrentRadiationCount());
                        existingHospitalisation.setCurrentDeathCount(hospitalisation.getCurrentDeathCount());
                        return regionalHospitalisationRepository.save(existingHospitalisation);
                    }
                })
                .orElseGet(() -> regionalHospitalisationRepository.save(hospitalisation)
                );
    }

    public Optional<RegionalHospitalisation> safeUdateRegionalHospitalisation(RegionalHospitalisation hospitalisation) {
        try {
            return Optional.of(updateRegionalHospitalisation(hospitalisation));
        } catch (Exception e) {
            LOGGER.warn("Could not update {}", hospitalisation);
            return Optional.empty();
        }
    }


    @Transactional
    public DepartmentalNewHospitalisation updateDepartmentalNewHospitalisation(DepartmentalNewHospitalisation hospitalisation) {
        return departmentalNewHospitalisationRepository
                .findByDepartmentAndNoticeDate(
                        hospitalisation.getDepartment(),
                        hospitalisation.getNoticeDate()
                )
                .map(existingHospitalisation -> {
                    if (existingHospitalisation.equals(hospitalisation)) {
                        return existingHospitalisation;
                    } else {
                        existingHospitalisation.setNewHospitalizedCount(hospitalisation.getNewHospitalizedCount());
                        existingHospitalisation.setNewIntensiveCareCount((hospitalisation.getNewIntensiveCareCount()));
                        existingHospitalisation.setNewRadiationCount(hospitalisation.getNewRadiationCount());
                        existingHospitalisation.setNewDeathCount(hospitalisation.getNewDeathCount());
                        return departmentalNewHospitalisationRepository.save(existingHospitalisation);
                    }
                })
                .orElseGet(() ->
                        departmentalNewHospitalisationRepository.save(hospitalisation)
                );
    }

    public Optional<DepartmentalNewHospitalisation> safeUpdateDepartmentalNewHospitalisation(DepartmentalNewHospitalisation hospitalisation) {
        try {
            return Optional.of(updateDepartmentalNewHospitalisation(hospitalisation));
        } catch (Exception e) {
            LOGGER.warn("Could not update {}", hospitalisation);
            return Optional.empty();
        }
    }

    @Transactional
    public DepartmentalHospitalisation updateDepartmentalHospitalisation(DepartmentalHospitalisation hospitalisation) {
        return departmentalHospitalisationRepository
                .findByDepartmentAndNoticeDateAndSex(
                        hospitalisation.getDepartment(),
                        hospitalisation.getNoticeDate(),
                        hospitalisation.getSex()
                )
                .map(existingHospitalisation -> {
                    if (existingHospitalisation.equals(hospitalisation)) {
                        return existingHospitalisation;
                    } else {
                        existingHospitalisation.setCurrentHospitalizedCount(hospitalisation.getCurrentHospitalizedCount());
                        existingHospitalisation.setCurrentIntensiveCareCount((hospitalisation.getCurrentIntensiveCareCount()));
                        existingHospitalisation.setCurrentRadiationCount(hospitalisation.getCurrentRadiationCount());
                        existingHospitalisation.setCurrentDeathCount(hospitalisation.getCurrentDeathCount());
                        return departmentalHospitalisationRepository.save(existingHospitalisation);
                    }
                })
                .orElseGet(() ->
                        departmentalHospitalisationRepository.save(hospitalisation)
                );
    }

    public Optional<DepartmentalHospitalisation> safeUpdateDepartmentalHospitalisation(DepartmentalHospitalisation hospitalisation) {
        try {
            return Optional.of(updateDepartmentalHospitalisation(hospitalisation));
        } catch (Exception e) {
            LOGGER.warn("Could not update {}", hospitalisation);
            return Optional.empty();
        }
    }


    public List<RegionalIntensiveCareAdmission> findAllRegionalIntensiveCareAdmission() {
        return regionalAdmissionsRepository.findAll();
    }

    public List<RegionalHospitalisation> getAllRegionalHospitalisations() {
        return regionalHospitalisationRepository.findAll();
    }

    public List<DepartmentalNewHospitalisation> getAllDepartmentalNewHospitalisations() {
        return departmentalNewHospitalisationRepository.findAll();
    }

    public List<DepartmentalHospitalisation> getAllDepartmentalHospitalisations() {
        return departmentalHospitalisationRepository.findAll();
    }

    public List<RegionalIntensiveCareAdmission> getByRegionCodeAndDays(
            String regionCode, Integer days
    ) {
        LocalDate date = LocalDate.now().minusDays(days);
        return localityService
                .findRegionByCode(regionCode)
                .map(region -> regionalAdmissionsRepository
                        .findByRegion(region).stream()
                        .filter(admission -> admission.getNoticeDate().isAfter(date))
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }


    public Map<String, Integer> getStats() {
        Map<String, Integer> map = new HashMap<>();
        map.put("RegionalIntensiveCareAdmissionCount", findAllRegionalIntensiveCareAdmission().size());
        map.put("RegionalHospitalisationCount", getAllRegionalHospitalisations().size());
        map.put("DepartmentalNewHospitalisationCount", getAllDepartmentalNewHospitalisations().size());
        map.put("DepartmentalHospitalisationCount", getAllDepartmentalHospitalisations().size());
        return map;
    }

    public List<RegionalIntensiveCareAdmission> findRegionalIntensiveCareAdmissionByRegionAndDates(
            String regionCode,
            String noticeDateBegin,
            String noticeDateEnd
    ) {
        return findRegionalIntensiveCareAdmissionByRegionAndDates(
        localityService.findRegionByCode(regionCode)
                .orElseThrow(() -> new HospitalisationNotFoundException("Could not find region")),
                ParsingUtils.parseDateOrThrow(noticeDateBegin,
                        () -> new HospitalisationNotFoundException("Could not parse noticeDateBegin")),
                ParsingUtils.parseDateOrThrow(noticeDateEnd,
                        () -> new HospitalisationNotFoundException("Could not parse noticeDateEnd"))
        );
    }
    public List<RegionalIntensiveCareAdmission> findRegionalIntensiveCareAdmissionByRegionAndDates(
            Region region,
            LocalDate noticeDateBegin,
            LocalDate noticeDateEnd
    ) {
        return regionalAdmissionsRepository.findByRegionAndNoticeDateIsBetween(region, noticeDateBegin, noticeDateEnd);
    }


    public Optional<RegionalIntensiveCareAdmission> findRegionalIntensiveCareAdmissionById(Long id) {
        return regionalAdmissionsRepository.findById(id);
    }
}
