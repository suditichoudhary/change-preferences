package com.update.preference.publishPreference.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.update.preference.publishPreference.entity.*;
import com.update.preference.publishPreference.repository.ChangePreferenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChangeMarketingPreferenceService {
    private static final Logger logger = LoggerFactory.getLogger(ChangeMarketingPreferenceService.class);

    /* Created by suditi on 2021-08-11 */

    ChangePreferenceRepository changePreferenceRepository;
    RestTemplate restTemplate;
    ObjectMapper objectMapper;


    @Autowired
    public ChangeMarketingPreferenceService(ChangePreferenceRepository changePreferenceRepository, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.changePreferenceRepository = changePreferenceRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     *
     * @param marketingPreferenceEntity
     *
     * checks if email exists
     * validates incomign object
     * calls customer microservice to bring user data
     * calls marketing pref get microservice to check existing preferences
     * update/add marketing pref
     *
     *
     * @return response
     */
    public Response addOrUpdateMarketingPreference(MarketingPreferenceEntity marketingPreferenceEntity){
        Response response = new Response();
        response.setCode(HttpStatus.OK.value());
        if(marketingPreferenceEntity==null){
            response.setMessage("Email and marketing Pref is mandatory");
            response.setCode(HttpStatus.OK.value());
            return response;
        }
        int status =0;


        logger.info("check if that email id is genuine");


        Response custResponse = getCustomerDetailsByEmail(marketingPreferenceEntity.getEmail());
        if(custResponse.getMessage()!=null && custResponse.getMessage().equals("success") && custResponse.getData()!=null) {
            CustomerEntity customerEntity = objectMapper.convertValue(custResponse.getData(), CustomerEntity.class);

            if(customerEntity!=null && customerEntity.getId()!=null) {
                marketingPreferenceEntity.setCustomerId(customerEntity.getId());

                logger.info("validate input preferences");

                Response validateResponse = validatePreferences(marketingPreferenceEntity,customerEntity);
                if(validateResponse.getMessage()!=null && validateResponse.getMessage().equals("success")) {

                    logger.info("check if marketing preference exists for this customer");

                    Response tempResponse = getMarketingPreferenceByEmail(marketingPreferenceEntity.getEmail());
                    if(tempResponse.getMessage()!=null && tempResponse.getMessage().equals("success") && tempResponse.getData()!=null) {

                        logger.info("marketing preferences exists already so update it");

                        MarketingPreferenceModel marketingPreferenceModel = objectMapper.convertValue(tempResponse.getData(),MarketingPreferenceModel.class);

                        if(marketingPreferenceModel!=null && marketingPreferenceModel.getEmail()!=null) {

                            if(tempResponse.getMessage().equals("success")) {
                                marketingPreferenceEntity = setUpMarketingPrefModelFromList(marketingPreferenceModel.getPreferences(),marketingPreferenceEntity);

                                status = changePreferenceRepository.updatemarketingPref(marketingPreferenceEntity.getFlagPost(),
                                        marketingPreferenceEntity.getFlagSms(),
                                        marketingPreferenceEntity.getFlagEmail(),
                                        marketingPreferenceEntity.getCustomerId());
                            }else{
                                response.setMessage(tempResponse.getMessage());
                            }
                        }else{
                            response.setMessage("Something went wrong while fetching marketing pref :(");
                            return response;
                        }
                    }else{

                        if(tempResponse!=null && tempResponse.getMessage().equals("No marketing preferences for this Email!!")){

                            logger.info("Add new marketing preference");

                            marketingPreferenceEntity = setUpMarketingPrefModelFromList(null,marketingPreferenceEntity);

                            changePreferenceRepository.save(marketingPreferenceEntity);
                            status = 1;
                        }else {
                            response.setMessage(tempResponse != null ? tempResponse.getMessage() : "Something is not right");
                            return response;
                        }
                    }


                    if(status>0) {
                        response.setMessage("success");
                        MarketingPreferenceModel marketingPreferenceModel1 = setUpMarketingPrefModel(marketingPreferenceEntity);
                        response.setData(marketingPreferenceModel1);
                    }else{
                        response.setMessage("Couldn't add/update pref :(");
                    }

                }else{
                    response.setMessage(validateResponse.getMessage());
                    return response;
                }


            }else{
                response.setMessage("Something went wrong while fetching customer details :(");
                return response;
            }

        }else{
            response.setMessage(custResponse.getMessage());
            return response;
        }

        return response;
    }

    /**
     *
     * @param marketingPreferenceEntity
     *
     * validates the incoming model
     *
     * @param customerEntity
     * @return
     */
    public Response validatePreferences(MarketingPreferenceEntity marketingPreferenceEntity, CustomerEntity customerEntity){
        Response response =  new Response();
        response.setMessage("success");
        if(marketingPreferenceEntity.getFlagEmail()==null && marketingPreferenceEntity.getFlagPost()==null && marketingPreferenceEntity.getFlagPost()==null ){
            response.setMessage("Please specify atleast one Marketing preferences : post/sms/email");
            return response;
        }

        if (marketingPreferenceEntity.getFlagPost()!=null && marketingPreferenceEntity.getFlagPost()==true
                && (customerEntity.getAddress() == null || customerEntity.getAddress().isEmpty())) {

            response.setMessage("Please set customer's address to select post");
            return response;
        }

        if (marketingPreferenceEntity.getFlagEmail()!=null && marketingPreferenceEntity.getFlagEmail()==true
                && (customerEntity.getEmail() == null || customerEntity.getEmail().isEmpty())) {

            response.setMessage("Please set customer's email to select email");
            return response;
        }

        if (marketingPreferenceEntity.getFlagSms()!=null && marketingPreferenceEntity.getFlagSms()==true
                && (customerEntity.getMobile() == null || customerEntity.getMobile().isEmpty())) {

            response.setMessage("Please set customer's mobile num to select sms");
            return response;
        }

        return response;

    }

    /**
     *
     * @param prefList
     *
     * fills marketing entity model as per new and old pref
     *
     * @param inputEntity
     * @return
     */
    public MarketingPreferenceEntity setUpMarketingPrefModelFromList(List<String> prefList,MarketingPreferenceEntity inputEntity){

        if (inputEntity.getFlagPost()==null) {
            if (prefList!=null && prefList.contains("post")) {
                inputEntity.setFlagPost(true);
            } else {
                inputEntity.setFlagPost(false);
            }
        }
        if (inputEntity.getFlagSms()==null) {
            if (prefList!=null && prefList.contains("sms")) {
                inputEntity.setFlagSms(true);
            } else {
                inputEntity.setFlagSms(false);
            }
        }

        if (inputEntity.getFlagEmail()==null) {
            if (prefList!=null && prefList.contains("email")) {
                inputEntity.setFlagEmail(true);
            } else {
                inputEntity.setFlagEmail(false);
            }
        }

        return inputEntity;
    }

    /**
     *
     * @param marketingPreferenceEntity
     *
     * prepares response of pref in a list (post/email/sms)
     *
     * @return
     */
    public MarketingPreferenceModel setUpMarketingPrefModel(MarketingPreferenceEntity marketingPreferenceEntity){
        List<String> prefList = new ArrayList<>();
        MarketingPreferenceModel marketingPreferenceModel = new MarketingPreferenceModel();
        marketingPreferenceModel.setEmail(marketingPreferenceEntity.getEmail());
        marketingPreferenceModel.setCustomersId(marketingPreferenceEntity.getCustomerId());

        if(marketingPreferenceEntity.getFlagEmail()){
            prefList.add("email");
        }
        if(marketingPreferenceEntity.getFlagPost()){
            prefList.add("post");
        }
        if(marketingPreferenceEntity.getFlagSms()){
            prefList.add("sms");
        }

        marketingPreferenceModel.setPreferences(prefList);
        return marketingPreferenceModel;
    }

    /**
     *
     * @param email
     *
     * calls marketing get microservice
     *
     * @return
     */
    public Response getMarketingPreferenceByEmail(String email){

        String url = MarketingProperties.url+"?email="+email;
        logger.debug("URL -> " + url);
        Response response = restTemplate.getForObject(url, Response.class);
        logger.debug("RESPONSE " + response.toString());
        return response;
    }

    /**
     *
     * @param email
     *
     * calls customer get info microservice
     * 
     * @return
     */

    public Response getCustomerDetailsByEmail(String email){

        String url = MarketingProperties.custUrl+"?email="+email;
        logger.debug("customer serice URL -> " + url);
        Response response = restTemplate.getForObject(url, Response.class);
        logger.debug("RESPONSE " + response.toString());
        return response;
    }

}
