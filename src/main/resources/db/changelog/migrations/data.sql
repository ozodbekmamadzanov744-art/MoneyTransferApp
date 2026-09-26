-- Тестовые аккаунты:
-- admin / Demo123!
-- alina / Demo123!
-- azamat / Demo123!
INSERT INTO currency_rates(code,units_per_usd) VALUES ('USD',1),('KGS',87.5),('EUR',0.92);
INSERT INTO app_users(username,password,role) VALUES
                                                  ('admin','$2b$12$TOY94fqnbQnsDYMOepKaaem8nH449d9hRKjm7sj6I969aHJHxymV2','ADMIN'),('alina','$2b$12$TOY94fqnbQnsDYMOepKaaem8nH449d9hRKjm7sj6I969aHJHxymV2','USER'),('azamat','$2b$12$TOY94fqnbQnsDYMOepKaaem8nH449d9hRKjm7sj6I969aHJHxymV2','USER');
INSERT INTO accounts(id,user_id,currency,balance) VALUES
                                                      (100001,(SELECT id FROM app_users WHERE username='alina'),'KGS',1000),
                                                      (100002,(SELECT id FROM app_users WHERE username='alina'),'USD',1000),
                                                      (100003,(SELECT id FROM app_users WHERE username='alina'),'EUR',1000),
                                                      (100004,(SELECT id FROM app_users WHERE username='azamat'),'KGS',1000),
                                                      (100005,(SELECT id FROM app_users WHERE username='azamat'),'USD',1000),
                                                      (100006,(SELECT id FROM app_users WHERE username='azamat'),'EUR',1000);
INSERT INTO service_providers(name,currency) VALUES ('Mobile KG','KGS'),('Internet KG','KGS'),('Digital Services','USD');
INSERT INTO subscribers(provider_id,reference,balance) VALUES
                                                           ((SELECT id FROM service_providers WHERE name='Mobile KG'),'0555123456',0),
                                                           ((SELECT id FROM service_providers WHERE name='Mobile KG'),'0700123456',0),
                                                           ((SELECT id FROM service_providers WHERE name='Internet KG'),'NET1001',0),
                                                           ((SELECT id FROM service_providers WHERE name='Internet KG'),'NET1002',0),
                                                           ((SELECT id FROM service_providers WHERE name='Digital Services'),'DIG1001',0);
INSERT INTO money_transactions(type,status,target_id,amount,currency,credited_amount,credited_currency,rate,created_at,processed_at)
SELECT 'OPENING','COMPLETED',id,1000,currency,1000,currency,1,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP FROM accounts;

INSERT INTO money_transactions(type,status,source_id,target_id,amount,currency,credited_amount,credited_currency,rate,created_at)
VALUES ('TRANSFER','PENDING',100002,100006,150,'USD',138,'EUR',0.92,CURRENT_TIMESTAMP);

ALTER TABLE accounts ALTER COLUMN id RESTART WITH 100010;
