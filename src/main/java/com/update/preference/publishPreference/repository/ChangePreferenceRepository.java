package com.update.preference.publishPreference.repository;

import com.update.preference.publishPreference.entity.MarketingPreferenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ChangePreferenceRepository extends JpaRepository<MarketingPreferenceEntity,Long> {

    /* Created by suditi on 2021-08-11 */
    @Modifying
    @Query(value ="UPDATE marketing_preferences SET flag_post = :flagPost, flag_sms = :flagSms, flag_email = :flagEmail WHERE customers_id = :customersId", nativeQuery= true)
    @Transactional
    int updatemarketingPref(@Param("flagPost") boolean flagPost, @Param("flagSms") boolean flagSms, @Param("flagEmail") boolean flagEmail,@Param("customersId") Long customersId);


}
