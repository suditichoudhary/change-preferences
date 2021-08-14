package com.update.preference;

import com.update.preference.publishPreference.entity.CustomerEntity;
import com.update.preference.publishPreference.entity.MarketingPreferenceEntity;
import com.update.preference.publishPreference.entity.Response;
import com.update.preference.publishPreference.service.ChangeMarketingPreferenceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PreferenceApplicationTests {

	@Autowired
	ChangeMarketingPreferenceService changeMarketingPreferenceService;

	@MockBean
	MarketingPreferenceEntity marketingPreferenceEntity;

	/* 1. Add customer Marketing Pref list */
	@org.junit.Test
	public void addMarketingPreferenceList() {
		String emailAddress = "test2@gmail.com";
		CustomerEntity customerEntity = new CustomerEntity(5l,"test","test2@gmail.com","dubai","585058688","test");
		marketingPreferenceEntity = new MarketingPreferenceEntity(5l,customerEntity.getId(),null,true,true,emailAddress);
		Response response=changeMarketingPreferenceService.addOrUpdateMarketingPreference(marketingPreferenceEntity);
		assertEquals("success",response.getMessage());
	}

	/* 2. Update customer Marketing Pref list */
	@Test
	public void updateMarketingPreferenceList() {
		String emailAddress = "test@gmail.com";
		CustomerEntity customerEntity = new CustomerEntity(5l,"test","test@gmail.com","dubai","585058688","test");
		marketingPreferenceEntity = new MarketingPreferenceEntity(2l,customerEntity.getId(),null,null,true,emailAddress);
		Response response=changeMarketingPreferenceService.addOrUpdateMarketingPreference(marketingPreferenceEntity);
		assertEquals("success",response.getMessage());
	}

	/* 3. Update customer Marketing Pref for post with address missing */
	@Test
	public void updateMarketingAddressMissing() {
		String emailAddress = "test@gmail.com";
		CustomerEntity customerEntity = new CustomerEntity(5l,"test","test@gmail.com",null,"585058688","test");
		marketingPreferenceEntity = new MarketingPreferenceEntity(2l,customerEntity.getId(),true,false,false,emailAddress);
		Response response=changeMarketingPreferenceService.addOrUpdateMarketingPreference(marketingPreferenceEntity);
		assertEquals("Please set customer's address to select post",response.getMessage());
	}

	/* 4. No Preference specified */
	@Test
	public void updateWithoutPref() {
		String emailAddress = "test@gmail.com";
		CustomerEntity customerEntity = new CustomerEntity(5l,"test","test@gmail.com",null,"585058688","test");
		marketingPreferenceEntity = new MarketingPreferenceEntity(2l,customerEntity.getId(),null,null,null,emailAddress);
		Response response=changeMarketingPreferenceService.addOrUpdateMarketingPreference(marketingPreferenceEntity);
		assertEquals("Please specify atleast one Marketing preferences : post/sms/email",response.getMessage());
	}

	/* 4. Remove existing Preference */
	@Test
	public void removeSmsPref() {
		String emailAddress = "test@gmail.com";
		CustomerEntity customerEntity = new CustomerEntity(5l,"test","test@gmail.com",null,"585058688","test");
		marketingPreferenceEntity = new MarketingPreferenceEntity(2l,customerEntity.getId(),null,null,false,emailAddress);
		Response response=changeMarketingPreferenceService.addOrUpdateMarketingPreference(marketingPreferenceEntity);
		assertEquals("success",response.getMessage());
	}
}
