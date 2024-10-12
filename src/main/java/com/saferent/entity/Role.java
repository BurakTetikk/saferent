package com.saferent.entity;

import com.saferent.entity.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;



    @Enumerated(EnumType.STRING) // Enum type almak için kullandık --- rolü string olarak atamak için EnumType.STRING
    private RoleType roleType;


    @Override
    public String toString() {
        return "Role{" +
                "roleType=" + roleType +
                '}';
    }
}
