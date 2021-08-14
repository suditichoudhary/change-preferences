package com.update.preference.publishPreference.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MarketingPreferenceModel {

    /* Created by suditi on 2021-08-12 */
    private String email;
    private Long customersId;
    private List<String> preferences;
}
