package id.nolimit.api.metricsemployee.service;

import java.util.Map;

public interface EmployeeService {

    long getTotalEmployees();

    Double getAverageSalary();

    Map<String, Double> getMinimumAndMaximumSalary();

    Map<Double, Long> getAgeDistribution();

    Map<String, Long> getGenderDistribution();

    Map<String, Long> getMaritalStatusDistribution();

    Map<String, Long> getJoiningDateDistribution();

    Map<String, Long> getCommonInterests();

    Map<String, Long> getDesignationDistribution();

}
