INSERT INTO types(name) VALUES ('Письмо');
INSERT INTO types(name) VALUES ('Факс');
INSERT INTO types(name) VALUES ('Приказ');

INSERT INTO users(login, password_hash, role, mail) VALUES ('grumpy_cherry',
                                                            '$2a$10$ngpk7EA22mwXrsgZTTucG.a0fMk6D7tkiIuStf.L4/WMZ7Vx8aMF6',
                                                            'ADMIN',
                                                            'grumpy_cherry@rambler.ru');
INSERT INTO users(login, password_hash, role, mail) VALUES ('sly_zucchini',
                                                            '$2a$10$MEIGKiMw1wLmIpjFoYIg4eEpzh9ZAd5MCbS6wadNvM9owQxockNi6',
                                                            'USER',
                                                            'sly_zucchini@gmail.com');