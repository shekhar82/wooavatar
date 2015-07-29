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

