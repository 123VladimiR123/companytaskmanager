package com.spring.companytaskmanager.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.sql.Date;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "user_info")
public class UserInfoEntity {
    @Id
    @Email(message = "Некорректный email адрес")
    @Column(name = "username")
    private String username;
    @Pattern(regexp = "^\\+7[0-9]{10}$", message = "Номер телефона должен быть в виде: +7XXXXXXXXXX")
    @Column(name = "phone_number")
    private String phoneNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department", referencedColumnName = "department_name")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private DepartmentEntity departmentName;
    @NotEmpty(message = "Должность не указана")
    @Column(name = "role")
    private String role;
    @Column(name = "birth_date")
    private Date birthDate;
    @NotEmpty(message = "Не указано имя")
    @Column(name = "first_name")
    private String firstName;
    @NotEmpty(message = "Не указана фамилия")
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "father_name")
    private String fatherName;
    @Column(name = "image_src")
    private String iconPath;
}
