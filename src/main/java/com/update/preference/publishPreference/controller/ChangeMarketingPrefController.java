package com.update.preference.publishPreference.controller;

import com.update.preference.publishPreference.entity.MarketingPreferenceEntity;
import com.update.preference.publishPreference.entity.Response;
import com.update.preference.publishPreference.service.ChangeMarketingPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ChangeMarketingPrefController {

    /* Created by suditi on 2021-08-11 */

    public final ChangeMarketingPreferenceService changeMarketingPreferenceService;

    @Autowired
    public ChangeMarketingPrefController(ChangeMarketingPreferenceService changeMarketingPreferenceService) {
        this.changeMarketingPreferenceService = changeMarketingPreferenceService;
    }

    @PutMapping("/v1/marketing/preference")
    public ResponseEntity<?> addOrupdateMarketingPreference(@RequestBody MarketingPreferenceEntity marketingPreferenceEntity) {
        Response response = changeMarketingPreferenceService.addOrUpdateMarketingPreference(marketingPreferenceEntity);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
