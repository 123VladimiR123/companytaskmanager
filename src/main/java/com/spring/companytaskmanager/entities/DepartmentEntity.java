package com.spring.companytaskmanager.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "departments")
public class DepartmentEntity {
    @Id
    @Column(name = "department_name")
    @NotEmpty
    private String name;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_name", referencedColumnName = "department")
    private UserInfoEntity mainUser;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_higher")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private DepartmentEntity departmentHigher;
    @Column(name = "enabled")
    private boolean enabled;
}
