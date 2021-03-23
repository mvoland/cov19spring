package com.mvoland.cov19api.covidstat.locality.data;


import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(indexes = @Index(name = "index", columnList = "regionNumber", unique = true))
@AllArgsConstructor
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private Integer regionNumber;

    @Column
    private String regionName;

    public Region() {
    }

    public Region(Integer regionNumber, String regionName) {
        this.setRegionNumber(regionNumber);
        this.setRegionName(regionName);
    }

    public Long getId() {
        return id;
    }

    public Integer getRegionNumber() {
        return regionNumber;
    }

    public void setRegionNumber(Integer regionNumber) {
        this.regionNumber = regionNumber;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    @Override
    public String toString() {
        return "Region{" +
                "id=" + id +
                ", regionNumber=" + regionNumber +
                ", regionName='" + regionName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Region region = (Region) o;
        return Objects.equals(regionNumber, region.regionNumber) && Objects.equals(regionName, region.regionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regionNumber, regionName);
    }
}