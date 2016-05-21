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
values ( 'searchData1', 'N' );
insert into PERFECTO_PAGE_DATA ( TYPE_NAME, NAME, ACTIVE )
values ( 'searchData1', 'one', 'Y' );
insert into PERFECTO_PAGE_DATA ( TYPE_NAME, NAME, ACTIVE )
values ( 'searchData1', 'two', 'Y' );
insert into PERFECTO_PAGE_DATA ( TYPE_NAME, NAME, ACTIVE )
values ( 'searchData1', 'three', 'Y' );
insert into PERFECTO_PAGE_DATA_ATTRS ( TYPE_NAME, RECORD_NAME, NAME, VALUE )
values ( 'searchData1', 'one', 'text', 'perfecto' );
insert into PERFECTO_PAGE_DATA_ATTRS ( TYPE_NAME, RECORD_NAME, NAME, VALUE )
values ( 'searchData1', 'one', 'url', 'www.google.com' );
insert into PERFECTO_PAGE_DATA_ATTRS ( TYPE_NAME, RECORD_NAME, NAME, VALUE )
values ( 'searchData1', 'two', 'text', 'jaxb' );
insert into PERFECTO_PAGE_DATA_ATTRS ( TYPE_NAME, RECORD_NAME, NAME, VALUE )
values ( 'searchData1', 'two', 'url', 'www.google.com' );
insert into PERFECTO_PAGE_DATA_ATTRS ( TYPE_NAME, RECORD_NAME, NAME, VALUE )
values ( 'searchData1', 'three', 'text', 'oracle' );
insert into PERFECTO_PAGE_DATA_ATTRS ( TYPE_NAME, RECORD_NAME, NAME, VALUE )
values ( 'searchData1', 'three', 'url', 'www.google.com' );

insert into PERFECTO_SITES ( NAME )
values ( 'Google' );
insert into PERFECTO_PAGES ( SITE_NAME, NAME )
values ( 'Google', 'Home' );
insert into PERFECTO_ELEMENTS ( SITE_NAME, PAGE_NAME, NAME, DESCRIPTOR, VALUE, CONTEXT_NAME )
values ( 'Google', 'Home', 'SEARCH_FOR', 'XPATH', '//input[@id=''lst-ib'']', null );
insert into PERFECTO_ELEMENTS ( SITE_NAME, PAGE_NAME, NAME, DESCRIPTOR, VALUE, CONTEXT_NAME )
values ( 'Google', 'Home', 'SEARCH', 'XPATH', '//button[@name=''btnG;'']', null );

commit;

           

       
            


