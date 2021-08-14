package com.update.preference.publishPreference.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="marketing_preferences")
public class MarketingPreferenceEntity {

    /* Created by suditi on 2021-08-11 */
    @Column(name = "id",nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "customers_id")
    private Long customerId;
    @Column(name= "flag_post", columnDefinition = "boolean default false")
    private Boolean flagPost;
    @Column(name= "flag_sms", columnDefinition = "boolean default false")
    private Boolean flagSms;
    @Column(name= "flag_email", columnDefinition = "boolean default false")
    private Boolean flagEmail;
    @Transient
    private String email;

}
