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
insert into hotspot(name, lat, lng) values('Hope Farm Circle', 12.983766, 77.752478);
insert into hotspot(name, lat, lng) values('ITPL Mall', 12.987470, 77.736142);
insert into hotspot(name, lat, lng) values('Opp. ITPL Mall', 12.987752, 77.736174);
insert into hotspot(name, lat, lng) values('Big Bazzar ITPL', 12.987794, 77.731752);
insert into hotspot(name, lat, lng) values('Big Bazzar ITPL PSN', 12.988157, 77.731420);
insert into hotspot(name, lat, lng) values('Big Bazzar ITPL(Towards Hope Farm)', 12.988281, 77.731701);

insert into hotspot(name, lat, lng) values('PSN', 12.988932,77.727979);
insert into hotspot(name, lat, lng) values('Opp. PSN', 12.988777,77.727948);
insert into hotspot(name, lat, lng) values('Hoodi Circle -> Graphite', 12.991906, 77.715718);
insert into hotspot(name, lat, lng) values('Hoodi Circle -> PSN', 12.992353,77.716387);
insert into hotspot(name, lat, lng) values('Graphite (Tds Hoodi, Brigade, BF)', 12.980107, 77.708892);

insert into hotspot(name, lat, lng) values('Graphite Circle <- SAP', 12.977925, 77.709780);
insert into hotspot(name, lat, lng) values('Graphite India(Towards SAP)', 12.978113, 77.709760);
insert into hotspot(name, lat, lng) values('Brookefield Mall', 12.966284, 77.718121);
insert into hotspot(name, lat, lng) values('Opp. Brookefield Mall', 12.966313, 77.718243);
insert into hotspot(name, lat, lng) values('Kundanahalli Circle', 12.955940, 77.714781);

insert into hotspot(name, lat, lng) values('Inorbit Mall', 12.979799, 77.727486);
insert into hotspot(name, lat, lng) values('Opp. Inorbit Mall', 12.979825, 77.727170);

insert into hotspot(name, lat, lng) values('Vydehi Hospital', 12.976648, 77.726731);
insert into hotspot(name, lat, lng) values('Opp Vydehi Hospital', 12.976419, 77.726719);

insert into hotspot(name, lat, lng) values('SAP Labs', 12.977832, 77.714456);
insert into hotspot(name, lat, lng) values('Opp SAP Labs', 12.977615, 77.714400);

insert into hotspot(name, lat, lng) values('Brigade Metropolis', 12.993053, 77.703638);
insert into hotspot(name, lat, lng) values('Opp. Brigade Metropolis', 12.993511, 77.703804);


insert into hotspot(name, lat, lng) values('Phoenix Mall', 12.995539, 77.695586);
insert into hotspot(name, lat, lng) values('Opp. Phoenix Mall', 12.995322, 77.695527);

insert into hotspot(name, lat, lng) values('HP', 12.997936, 77.689346);


--insert into hotspot(name, lat, lng) values('Kundanahalli Hypercity', 12.958361, 77.715753);
--insert into hotspot(name, lat, lng) values('Kundanahalli Gate Signal1', 12.956235, 77.714919);
--insert into hotspot(name, lat, lng) values('Kundanahalli Gate Signal2', 12.956217, 77.714774);
--insert into hotspot(name, lat, lng) values('Opp. Kundanahalli Hypercity', 12.958416, 77.715613);
--insert into hotspot(name, lat, lng) values('Vydehi Hospital Bus-stop', 12.976765, 77.726664);
--insert into hotspot(name, lat, lng) values('Opp. Inorbit Mall', 12.979576, 77.727129);
--insert into hotspot(name, lat, lng) values('Hoodi Circle -> PSN', 12.992309, 77.716594);

-- insert into hotspot(name, lat, lng) values('Hoodi Circle', 12.992353, 77.716387);
--insert into hotspot(name, lat, lng) values('Hoodi Circle-Phoenix', 12.992443, 77.716035);
--insert into hotspot(name, lat, lng) values('PSN->Hoodi Circle', 12.992190, 77.716322);
-- insert into hotspot(name, lat, lng) values('Brigade Metropolis', 12.993053, 77.703638);
-- insert into hotspot(name, lat, lng) values('Phoenix Mall', 12.997361, 77.696630);
-- insert into hotspot(name, lat, lng) values('Inorbit Mall', 12.979568, 77.728424);
--insert into hotspot(name, lat, lng) values('Vydehi Hospital', 12.977170, 77.726652);
--insert into hotspot(name, lat, lng) values('SAP Labs', 12.977921, 77.714472);	
--insert into hotspot(name, lat, lng) values('Graphite India - SAP', 12.977638, 77.709937);
--insert into hotspot(name, lat, lng) values('Graphite India', 12.978025, 77.709540);
--insert into hotspot(name, lat, lng) values('Brookefield Mall', 12.966346, 77.717892);
-- insert into hotspot(name, lat, lng) values('Kundanahalli Hypercity', 12.958332, 77.715991);
--insert into hotspot(name, lat, lng) values('Kundanahalli Gate Signal', 12.956136, 77.715723);

-- Driver Details
insert into auto_driver(USER_ID, NAME, MOB_NO, AUTO_VEHICLE_NO, RATING, WALLET_BALANCE) values(6, 'Rohit', '98110212007','KA001234', 4.5, 500.00);
insert into auto_driver(USER_ID, NAME, MOB_NO, AUTO_VEHICLE_NO, RATING, WALLET_BALANCE) values(7, 'Hardik', '98450210000','KA001264', 4.0, 500.00);
insert into auto_driver(USER_ID, NAME, MOB_NO, AUTO_VEHICLE_NO, RATING, WALLET_BALANCE) values(8, 'Rahul', '98450212911','KA001903', 4.4, 500.00);
insert into auto_driver(USER_ID, NAME, MOB_NO, AUTO_VEHICLE_NO, RATING, WALLET_BALANCE) values(9, 'Karan', '78450212956','KA001356', 4.1, 500.00);
insert into auto_driver(USER_ID, NAME, MOB_NO, AUTO_VEHICLE_NO, RATING, WALLET_BALANCE) values(10,'Vicky', '78450452937', 'KA001252', 4.0, 500.00);


--Passenger Details
insert into passenger(USER_ID, NAME, MOB_NO, RATING, WALLET_BALANCE) values (1,'Raj','98450212937','5.0', 500.00);
insert into passenger(USER_ID, NAME, MOB_NO, RATING, WALLET_BALANCE) values (2,'Tina','984502323937','5.0', 500.00);
insert into passenger(USER_ID, NAME, MOB_NO, RATING, WALLET_BALANCE) values (3,'Pooja','98450223937','5.0', 500.00);
insert into passenger(USER_ID, NAME, MOB_NO, RATING, WALLET_BALANCE) values (4,'Manoj','98230212937','5.0', 500.00);
insert into passenger(USER_ID, NAME, MOB_NO, RATING, WALLET_BALANCE) values (5,'Sourav','9848912913','5.0', 500.00);