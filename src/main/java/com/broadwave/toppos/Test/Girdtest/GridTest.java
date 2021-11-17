package com.broadwave.toppos.Test.Girdtest;

import lombok.*;

import javax.persistence.*;

/**
 * @author Minkyu
 * Date : 2021-11-03
 * Time :
 * Remark : 그리드 테스트 테이블(리스트출력)
 */
@Entity
@Data
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="grid_test_table")
public class GridTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="test_name")
    private String testName;

    @Column(name="test_old")
    private Integer testOld;

    @Column(name="test_gender")
    private String testGender;

    @Column(name="test_money")
    private Integer testMoney;

}
