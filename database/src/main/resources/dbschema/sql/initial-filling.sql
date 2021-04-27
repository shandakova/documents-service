INSERT INTO types(name) VALUES ('Письмо');
INSERT INTO types(name) VALUES ('Факс');
INSERT INTO types(name) VALUES ('Приказ');

INSERT INTO users(login, password_hash, role, mail) VALUES (
                                                            'grumpy_cherry',
                                                            crypt('verystrongpassword', gen_salt('bf')),
                                                            'admin',
                                                            'grumpy_cherry@rambler.ru'
                                                            );
INSERT INTO users(login, password_hash, role, mail) VALUES (
                                                            'sly_zucchini',
                                                            crypt('qwerty', gen_salt('bf')),
                                                            'user',
                                                            'sly_zucchini@gmail.com'
                                                            );