package id.nolimit.api.metricsemployee.controller;

import id.nolimit.api.metricsemployee.constant.ConstantValue;
import id.nolimit.api.metricsemployee.model.BaseResponse;
import id.nolimit.api.metricsemployee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping(value = "/count-of-employees", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<Long>> getCountOfEmployees() throws IOException {
        long response = employeeService.getTotalEmployees();
        BaseResponse<Long> baseResponse = BaseResponse.<Long>builder()
                .code(HttpStatus.OK.value())
                .message(ConstantValue.SUCCESS)
                .data(response)
                .build();
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/average-salary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<Double>> getAverageSalary() throws IOException {
        Double response = employeeService.getAverageSalary();
        BaseResponse<Double> baseResponse = BaseResponse.<Double>builder()
                .code(HttpStatus.OK.value())
                .message(ConstantValue.SUCCESS)
                .data(response)
                .build();
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/minimum-maximum-salary", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<Map<String, Double>>> getMinimumAndMaximumSalary() throws IOException {
        Map<String, Double> response = employeeService.getMinimumAndMaximumSalary();
        BaseResponse<Map<String, Double>> baseResponse = BaseResponse.<Map<String, Double>>builder()
                .code(HttpStatus.OK.value())
                .message(ConstantValue.SUCCESS)
                .data(response)
                .build();
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/age-distribution", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<Map<Double, Long>>> getAgeDistribution() throws IOException {
        Map<Double, Long> response = employeeService.getAgeDistribution();
        BaseResponse<Map<Double, Long>> baseResponse = BaseResponse.<Map<Double, Long>>builder()
                .code(HttpStatus.OK.value())
                .message(ConstantValue.SUCCESS)
                .data(response)
                .build();
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/gender-distribution", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<Map<String, Long>>> getGenderDistribution() throws IOException {
        Map<String, Long> response = employeeService.getGenderDistribution();
        BaseResponse<Map<String, Long>> baseResponse = BaseResponse.<Map<String, Long>>builder()
                .code(HttpStatus.OK.value())
                .message(ConstantValue.SUCCESS)
                .data(response)
                .build();
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/marital-status-distribution", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<Map<String, Long>>> getMaritalStatusDistribution() throws IOException {
        Map<String, Long> response = employeeService.getMaritalStatusDistribution();
        BaseResponse<Map<String, Long>> baseResponse = BaseResponse.<Map<String, Long>>builder()
                .code(HttpStatus.OK.value())
                .message(ConstantValue.SUCCESS)
                .data(response)
                .build();
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/joining-date-histogram", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<Map<String, Long>>> getJoiningDateHistogram() throws IOException {
        Map<String, Long> response = employeeService.getJoiningDateDistribution();
        BaseResponse<Map<String, Long>> baseResponse = BaseResponse.<Map<String, Long>>builder()
                .code(HttpStatus.OK.value())
                .message(ConstantValue.SUCCESS)
                .data(response)
                .build();
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/top-interests", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<Map<String, Long>>> getCommonInterests() throws IOException {
        Map<String, Long> response = employeeService.getCommonInterests();
        BaseResponse<Map<String, Long>> baseResponse = BaseResponse.<Map<String, Long>>builder()
                .code(HttpStatus.OK.value())
                .message(ConstantValue.SUCCESS)
                .data(response)
                .build();
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/designation-distribution", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse<Map<String, Long>>>  getDesignationDistribution() throws IOException {
        Map<String, Long> response = employeeService.getDesignationDistribution();
        BaseResponse<Map<String, Long>> baseResponse = BaseResponse.<Map<String, Long>>builder()
                .code(HttpStatus.OK.value())
                .message(ConstantValue.SUCCESS)
                .data(response)
                .build();
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

}
