create table user_profile (
	user_id varchar(100) not null,
	primary_email varchar(100) not null,
	first_name varchar(255),
	last_name varchar(255),
	last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	is_active tinyint(1) not null default 0,
	image blob,
	PRIMARY KEY (user_id),
	UNIQUE (primary_email)
);

create table client_registry (
	client_id varchar(255) not null,
	client_secret varchar(255) not null,
	client_name varchar(255) not null,
	client_email varchar(255),
	PRIMARY KEY(client_id),
	UNIQUE (client_name)
);


create table user_auth (
	user_id varchar(100) not null,
	user_pwd varchar(50) not null,
	primary_email varchar(100) not null,
	PRIMARY KEY(user_id),
	UNIQUE (primary_email)
);

create table client_oauth_attr (
	client_id varchar(255) not null,
	auth_code varchar(255),
	access_token varchar(255),
	auth_code_issued TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	access_token_issued TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	access_token_expiry int,
	PRIMARY KEY(client_id)
);



