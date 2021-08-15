This micrservice is mainly responsible to update/add customer marketing preferences.

This service internally calls another microservice for customer details and retrieving existing preferences.

Link to the respective microservice code : https://github.com/suditichoudhary/marketing-preferences

It has following Controller :
1. Add/update marketing preferences -> this service adds the pref if not present for the user, if present then updates it.


Certain checks are incorporated here :
1. If email pref to be added - Customer table must have EMAIL present
2. If post pref to be added - Customer table must have ADDRESS present
3. If sms pref to be added - Customer table must have MOBILE present

Specs :

Java version : 1.8 Spring Boot Application Mysql version : 8.0.12 Port : 8082

Main method : com.update.preference.PreferenceApplication

Test class : com.update.preference.PreferenceApplicationTests

--> Sample Request For retrieving pref :

1. curl -X PUT \
  http://localhost:8082/v1/marketing/preference \
  -H 'cache-control: no-cache' \
  -d ' {
         "email": "test@gmail.com",
         "flagEmail" :false,
         "flagSms" : false,
         "flagPost" :false
    }'

NOTE : if any preference is not need to be update then that key should be removed from the above sample body.

Example : 

1. Add email preference 

curl -X PUT \
  http://localhost:8082/v1/marketing/preference \
  -H 'cache-control: no-cache' \
  -d ' {
         "email": "test@gmail.com",
         "flagEmail" :true
    }'

 2. Remove email preference and add sms

 curl -X PUT \
  http://localhost:8082/v1/marketing/preference \
  -H 'cache-control: no-cache' \
  -d ' {
         "email": "test@gmail.com",
         "flagEmail" :false,
         "flagSms" : true
    }'


Mysql schema :

create database marketing;

use marketing;

CREATE TABLE `customers` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `customers_name` varchar(32) NOT NULL DEFAULT '',
  `customers_email` varchar(96) NOT NULL DEFAULT '',
  `customers_address` varchar(400) DEFAULT NULL,
  `customers_password` text NOT NULL,
  `customers_mobile` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `customers_email` (`customers_email`)
) ENGINE=InnoDB

insert into user_details values (null,'suditi','suditichoudhary@gmail.com',null,'Ghfhgg',7965547);


CREATE TABLE `marketing_preferences` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `customers_id` int(10) NOT NULL,
  `flag_post` tinyint(1) NOT NULL DEFAULT '0',
  `flag_sms` tinyint(1) NOT NULL DEFAULT '0',
  `flag_email` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `customers_id` (`customers_id`)
) ENGINE=InnoDB 

insert into marketing_preferences values (null,1,0,0,1);


--> Docker File added in the project

TEST CASES :

1. Test to Add customer Marketing Pref list
2. Test to Update customer Marketing Pref list
3. Test to Update customer Marketing Pref for post with address missing 
4. Test to throw error if No Preference specified
5. Test to remove existing Preference 
