package com.update.preference.publishPreference.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerEntity {

    /* Created by suditi on 2021-08-14 */

    private Long id;
    private String name;
    private String email;
    private String address;
    private String mobile;
    private String password;

}
