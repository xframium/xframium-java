truncate table PERFECTO_CONTENT;
truncate table PERFECTO_CLOUDS;
truncate table PERFECTO_DEVICES;
truncate table PERFECTO_DEVICE_CAPABILITIES;
truncate table PERFECTO_PAGE_DATA_TYPE;
truncate table PERFECTO_PAGE_DATA;
truncate table PERFECTO_PAGE_DATA_ATTRS;
truncate table PERFECTO_SITES;
truncate table PERFECTO_PAGES;
truncate table PERFECTO_ELEMENTS;
truncate table PERFECTO_ELEMENTS;
truncate table PERFECTO_APPLICATIONS;
truncate table PERFECTO_TEST_SUITES;
truncate table PERFECTO_TEST_SUITE_PAGES;
truncate table PERFECTO_TESTS;
truncate table PERFECTO_CONTENT;
truncate table PERFECTO_TEST_STEPS;
truncate table PERFECTO_TEST_SUBSTEPS;
truncate table PERFECTO_TEST_STEP_PARAMS;
truncate table PERFECTO_TEST_STEP_TOKENS;
truncate table PERFECTO_FUNCTIONS;
truncate table PERFECTO_TEST_IMPORTED_SUITES;



insert into PERFECTO_CONTENT( KEY_NAME, VALUE, OFFSET )
values ( 'test', 'one', 0 );
insert into PERFECTO_CONTENT( KEY_NAME, VALUE, OFFSET )
values ( 'test', 'two', 1 );
insert into PERFECTO_CONTENT( KEY_NAME, VALUE, OFFSET )
values ( 'test', 'three', 2 );

insert into PERFECTO_CLOUDS ( NAME, USER_NAME, PASSWORD, HOST_NAME,
                              PROXY_HOST, PROXY_PORT, DESCRIPTION, GRID_INSTANCE )
values ( 'partners', 'jharrington@morelandlabs.com', 'Spike123!', 'partners.perfectomobile.com', null, null, null, null );

insert into PERFECTO_DEVICES ( NAME, ID, MANUFACTURER, MODEL, OS, OS_VERSION, BROWSER_NAME, BROWSER_VERSION, ACTIVE, AVAILABLE)
values ( 'Samsung S6', null, 'Samsung', 'Galaxy S6', 'Android', null, null, null, 'Y', 2 );
insert into PERFECTO_DEVICE_CAPABILITIES ( DEVICE_NAME, NAME, CLASS, VALUE )
values ( 'Samsung S6', 'DUMMY-CAPABILITY', 'STRING', 'DUMMY-VALUE' );

insert into PERFECTO_PAGE_DATA_TYPE ( NAME, LOCK_RECORDS )
values ( 'searchData', 'N' );
insert into PERFECTO_PAGE_DATA ( TYPE_NAME, NAME, ACTIVE )
values ( 'searchData', 'one', 'Y' );
insert into PERFECTO_PAGE_DATA_ATTRS ( TYPE_NAME, RECORD_NAME, NAME, VALUE )
values ( 'searchData', 'one', 'text', 'oracle' );
insert into PERFECTO_PAGE_DATA_ATTRS ( TYPE_NAME, RECORD_NAME, NAME, VALUE )
values ( 'searchData', 'one', 'url', 'www.google.com' );
insert into PERFECTO_PAGE_DATA_ATTRS ( TYPE_NAME, RECORD_NAME, NAME, VALUE )
values ( 'searchData', 'one', 'alttext', 'db2' );
insert into PERFECTO_PAGE_DATA_ATTRS ( TYPE_NAME, RECORD_NAME, NAME, VALUE )
values ( 'searchData', 'one', 'text2', 'wwbsphere' );
insert into PERFECTO_PAGE_DATA_ATTRS ( TYPE_NAME, RECORD_NAME, NAME, VALUE )
values ( 'searchData', 'one', 'alttext2', 'weblogic' );

insert into PERFECTO_SITES ( NAME )
values ( 'Google' );
insert into PERFECTO_PAGES ( SITE_NAME, NAME )
values ( 'Google', 'Home' );
insert into PERFECTO_ELEMENTS ( SITE_NAME, PAGE_NAME, NAME, DESCRIPTOR, VALUE, CONTEXT_NAME )
values ( 'Google', 'Home', 'SEARCH_FOR', 'XPATH', '//input[@id=''lst-ib'']', null );
insert into PERFECTO_ELEMENTS ( SITE_NAME, PAGE_NAME, NAME, DESCRIPTOR, VALUE, CONTEXT_NAME )
values ( 'Google', 'Home', 'SEARCH', 'XPATH', '//button[@name=''btnG;'']', null );

insert into PERFECTO_APPLICATIONS( NAME, APP_PACKAGE, BUNDLE_ID, URL, IOS_INSTALL, ANDROID_INSTALL )
values ( 'Google', null, null, 'http://www.google.com', null, null );

insert into PERFECTO_TEST_SUITES ( NAME )
values( 'alternate-device' );

insert into PERFECTO_TEST_SUITE_PAGES ( SUITE_NAME, PAGE_NAME, CLASS_NAME )
values( 'alternate-device', 'Home', NULL );

insert into PERFECTO_TESTS ( SUITE_NAME, NAME, DATA_DRIVER, DATA_PROVIDER, LINK_ID, TIMED, THRESHOLD, ACTIVE, OS, TAG_NAMES )
values( 'alternate-device', 'MyTest', 'searchData', NULL, NULL, 'Y', NULL, 'Y', NULL, NULL );

insert into PERFECTO_TEST_STEPS ( KEY, SUITE_NAME, TEST_NAME, NAME, PAGE_NAME, TYPE, ACTIVE, LINK_ID, OS,
                                 POI, THRESHOLD, TIMED, INVERSE, WAIT, FAILURE_MODE, VALIDATION, VALIDATION_TYPE,
                                 DEVICE, TAG_NAMES, OFFSET )
values ( 1, 'alternate-device', 'MyTest', 'LOAD', 'Home', 'OPEN_PAGE', 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 1 );
insert into PERFECTO_TEST_STEP_PARAMS ( STEP_KEY, TYPE, VALUE, OFFSET )
values ( 1, 'DATA', 'searchData.url', 1 );
insert into PERFECTO_TEST_STEPS ( KEY, SUITE_NAME, TEST_NAME, NAME, PAGE_NAME, TYPE, ACTIVE, LINK_ID, OS,
                                 POI, THRESHOLD, TIMED, INVERSE, WAIT, FAILURE_MODE, VALIDATION, VALIDATION_TYPE,
                                 DEVICE, TAG_NAMES, OFFSET )
values ( 2, 'alternate-device', 'MyTest', 'SEARCH_FOR', 'Home', 'SET', 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2 );
insert into PERFECTO_TEST_STEP_PARAMS ( STEP_KEY, TYPE, VALUE, OFFSET )
values ( 2, 'DATA', 'searchData.text', 1 );
insert into PERFECTO_TEST_STEPS ( KEY, SUITE_NAME, TEST_NAME, NAME, PAGE_NAME, TYPE, ACTIVE, LINK_ID, OS,
                                 POI, THRESHOLD, TIMED, INVERSE, WAIT, FAILURE_MODE, VALIDATION, VALIDATION_TYPE,
                                 DEVICE, TAG_NAMES, OFFSET )
values ( 3, 'alternate-device', 'MyTest', 'SEARCH', 'Home', 'CLICK', 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3 );
insert into PERFECTO_TEST_STEPS ( KEY, SUITE_NAME, TEST_NAME, NAME, PAGE_NAME, TYPE, ACTIVE, LINK_ID, OS,
                                 POI, THRESHOLD, TIMED, INVERSE, WAIT, FAILURE_MODE, VALIDATION, VALIDATION_TYPE,
                                 DEVICE, TAG_NAMES, OFFSET )
values ( 4, 'alternate-device', 'MyTest', 'OPEN-ALT-PHONE', 'Home', 'ADD_DEVICE', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4 );
insert into PERFECTO_TEST_STEP_PARAMS ( STEP_KEY, TYPE, VALUE, OFFSET )
values ( 4, 'STATIC', 'alt', 1 );
insert into PERFECTO_TEST_STEP_PARAMS ( STEP_KEY, TYPE, VALUE, OFFSET )
values ( 4, 'STATIC', '02157DF2A1B46C22', 2 );
insert into PERFECTO_TEST_STEPS ( KEY, SUITE_NAME, TEST_NAME, NAME, PAGE_NAME, TYPE, ACTIVE, LINK_ID, OS,
                                 POI, THRESHOLD, TIMED, INVERSE, WAIT, FAILURE_MODE, VALIDATION, VALIDATION_TYPE,
                                 DEVICE, TAG_NAMES, OFFSET )
values ( 5, 'alternate-device', 'MyTest', 'LOAD', 'Home', 'OPEN_PAGE', 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'alt', NULL, 5 );
insert into PERFECTO_TEST_STEP_PARAMS ( STEP_KEY, TYPE, VALUE, OFFSET )
values ( 5, 'DATA', 'searchData.url', 1 );
insert into PERFECTO_TEST_STEPS ( KEY, SUITE_NAME, TEST_NAME, NAME, PAGE_NAME, TYPE, ACTIVE, LINK_ID, OS,
                                 POI, THRESHOLD, TIMED, INVERSE, WAIT, FAILURE_MODE, VALIDATION, VALIDATION_TYPE,
                                 DEVICE, TAG_NAMES, OFFSET )
values ( 6, 'alternate-device', 'MyTest', 'SEARCH_FOR', 'Home', 'SET', 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'alt', NULL, 6 );
insert into PERFECTO_TEST_STEP_PARAMS ( STEP_KEY, TYPE, VALUE, OFFSET )
values ( 6, 'DATA', 'searchData.alttext', 1 );
insert into PERFECTO_TEST_STEPS ( KEY, SUITE_NAME, TEST_NAME, NAME, PAGE_NAME, TYPE, ACTIVE, LINK_ID, OS,
                                 POI, THRESHOLD, TIMED, INVERSE, WAIT, FAILURE_MODE, VALIDATION, VALIDATION_TYPE,
                                 DEVICE, TAG_NAMES, OFFSET )
values ( 7, 'alternate-device', 'MyTest', 'SEARCH', 'Home', 'CLICK', 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'alt', NULL, 7 );
insert into PERFECTO_TEST_STEPS ( KEY, SUITE_NAME, TEST_NAME, NAME, PAGE_NAME, TYPE, ACTIVE, LINK_ID, OS,
                                 POI, THRESHOLD, TIMED, INVERSE, WAIT, FAILURE_MODE, VALIDATION, VALIDATION_TYPE,
                                 DEVICE, TAG_NAMES, OFFSET )
values ( 8, 'alternate-device', 'MyTest', 'SEARCH_FOR', 'Home', 'SET', 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 8 );
insert into PERFECTO_TEST_STEP_PARAMS ( STEP_KEY, TYPE, VALUE, OFFSET )
values ( 8, 'DATA', 'searchData.text2', 1 );
insert into PERFECTO_TEST_STEPS ( KEY, SUITE_NAME, TEST_NAME, NAME, PAGE_NAME, TYPE, ACTIVE, LINK_ID, OS,
                                 POI, THRESHOLD, TIMED, INVERSE, WAIT, FAILURE_MODE, VALIDATION, VALIDATION_TYPE,
                                 DEVICE, TAG_NAMES, OFFSET )
values ( 9, 'alternate-device', 'MyTest', 'SEARCH', 'Home', 'CLICK', 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 9 );
insert into PERFECTO_TEST_STEPS ( KEY, SUITE_NAME, TEST_NAME, NAME, PAGE_NAME, TYPE, ACTIVE, LINK_ID, OS,
                                 POI, THRESHOLD, TIMED, INVERSE, WAIT, FAILURE_MODE, VALIDATION, VALIDATION_TYPE,
                                 DEVICE, TAG_NAMES, OFFSET )
values ( 10, 'alternate-device', 'MyTest', 'SEARCH_FOR', 'Home', 'SET', 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'alt', NULL, 10 );
insert into PERFECTO_TEST_STEP_PARAMS ( STEP_KEY, TYPE, VALUE, OFFSET )
values ( 10, 'DATA', 'searchData.alttext2', 1 );
insert into PERFECTO_TEST_STEPS ( KEY, SUITE_NAME, TEST_NAME, NAME, PAGE_NAME, TYPE, ACTIVE, LINK_ID, OS,
                                 POI, THRESHOLD, TIMED, INVERSE, WAIT, FAILURE_MODE, VALIDATION, VALIDATION_TYPE,
                                 DEVICE, TAG_NAMES, OFFSET )
values ( 11, 'alternate-device', 'MyTest', 'SEARCH', 'Home', 'CLICK', 'Y', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'alt', NULL, 11 );

commit;

           

       
            


