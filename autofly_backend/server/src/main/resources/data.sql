-- Initial Inserts
-- Passengers
insert into user(email_id, password, user_type) values('raj123@gmail.com', 'iamraj', 'P');
insert into user(email_id, password, user_type) values('tina123@gmail.com', 'iamtina', 'P');
insert into user(email_id, password, user_type) values('pooja123@gmail.com', 'iampooja', 'P');
insert into user(email_id, password, user_type) values('manoj123@gmail.com', 'iammanoj', 'P');
insert into user(email_id, password, user_type) values('sourav123@gmail.com', 'iamsourav', 'P');
                                                                     
-- Drivers                                                           
insert into user(email_id, password, user_type) values('rohit123@gmail.com', 'iamrohit', 'D');
insert into user(email_id, password, user_type) values('hardik123@gmail.com', 'iamhardik', 'D');
insert into user(email_id, password, user_type) values('rahul123@gmail.com', 'iamrahul', 'D');
insert into user(email_id, password, user_type) values('karan123@gmail.com', 'iamkaran', 'D');
insert into user(email_id, password, user_type) values('vicky123@gmail.com', 'iamvicky', 'D');

-- Hotspots
insert into hotspot(name, lat, lng) values('ITPL Mall', 12.98747, 77.736464);
insert into hotspot(name, lat, lng) values('PSN', 12.989570, 77.727983);
insert into hotspot(name, lat, lng) values('Hoodi Circle', 12.992353, 77.716387);
--insert into hotspot(name, lat, lng) values('Hoodi Circle-Phoenix', 12.992443, 77.716035);
--insert into hotspot(name, lat, lng) values('Hoodi Circle-Graphite', 12.992190, 77.716322);
insert into hotspot(name, lat, lng) values('Brigade Metropolis', 12.993053, 77.703638);
insert into hotspot(name, lat, lng) values('Phoenix Mall', 12.997361, 77.696630);
insert into hotspot(name, lat, lng) values('Inorbit Mall', 12.979568, 77.728424);
insert into hotspot(name, lat, lng) values('Vydehi Hospital', 12.977170, 77.726652);
insert into hotspot(name, lat, lng) values('SAP Labs', 12.977921, 77.714472);	
--insert into hotspot(name, lat, lng) values('Graphite India - SAP', 12.977638, 77.709937);
insert into hotspot(name, lat, lng) values('Graphite India', 12.978025, 77.709540);
insert into hotspot(name, lat, lng) values('Brookefield Mall', 12.966346, 77.717892);
-- insert into hotspot(name, lat, lng) values('Kundanahalli Hypercity', 12.958332, 77.715991);
insert into hotspot(name, lat, lng) values('Kundanahalli Gate Signal', 12.956136, 77.715723);

-- Driver Details
insert into auto_driver(USER_ID, NAME, MOB_NO, AUTO_VEHICLE_NO, RATING, WALLET_BALANCE) values(6, 'Rohit', '98110212007','KA001234', 4.5, 500.00);
insert into auto_driver(USER_ID, NAME, MOB_NO, AUTO_VEHICLE_NO, RATING, WALLET_BALANCE) values(7, 'Hardik', '98450210000','KA001264', 4.0, 500.00);
insert into auto_driver(USER_ID, NAME, MOB_NO, AUTO_VEHICLE_NO, RATING, WALLET_BALANCE) values(8, 'Rahul', '98450212911','KA001903', 4.4, 500.00);
insert into auto_driver(USER_ID, NAME, MOB_NO, AUTO_VEHICLE_NO, RATING, WALLET_BALANCE) values(9, 'Karan', '78450212956','KA001356', 4.1, 500.00);
insert into auto_driver(USER_ID, NAME, MOB_NO, AUTO_VEHICLE_NO, RATING, WALLET_BALANCE) values(10,'Vicky', '78450452937', 'KA001252', 4.0, 500.00);
