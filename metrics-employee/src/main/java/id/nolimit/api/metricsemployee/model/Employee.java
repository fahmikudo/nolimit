package id.nolimit.api.metricsemployee.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class Employee {

    @JsonProperty("FirstName")
    private String firstName;

    @JsonProperty("LastName")
    private String lastName;

    @JsonProperty("Designation")
    private String designation;

    @JsonProperty("Salary")
    private Integer salary;

    @JsonProperty("DateOfJoining")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfJoining;

    @JsonProperty("Address")
    private String address;

    @JsonProperty("Gender")
    private String gender;

    @JsonProperty("Age")
    private String age;

    @JsonProperty("MaritalStatus")
    private String maritalStatus;

    @JsonProperty("Interests")
    private String interests;

}
