CREATE KEYSPACE twittermashup WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};
USE twittermashup;

CREATE TABLE stream (
	year 		int,
	month 		int,
	day 		int,
	hour 		int,
	minute 		int,
	t_id 		bigint,
	t_text 		text,
	t_ts 		timestamp,
	t_user 		text,
	t_lang 		text,
	PRIMARY KEY ((year, month, day, hour, minute), t_id)
);

CREATE TABLE userlist_minute (
	year 		int,
	month 		int,
	day 		int,
	hour 		int,
	minute 		int,
	t_ts 		timestamp,
	t_user 		text,
	PRIMARY KEY ((year, month, day, hour, minute), t_ts)
);

CREATE TABLE userlist_hour (
	year 		int,
	month 		int,
	day 		int,
	hour 		int,
	t_ts 		timestamp,
	t_user 		text,
	PRIMARY KEY ((year, month, day, hour), t_ts)
);

CREATE TABLE userlist_day (
	year 		int,
	month 		int,
	day 		int,
	t_ts 		timestamp,
	t_user 		text,
	PRIMARY KEY ((year, month, day), t_ts)
);

CREATE TABLE userlist_month (
	year 		int,
	month 		int,
	t_ts 		timestamp,
	t_user 		text,
	PRIMARY KEY ((year, month), t_ts)
);

CREATE TABLE correlatedkeywords_day (
	year 		int,
	month 		int,
	day 		int,
	count 		int,
	keyword 	text,
	PRIMARY KEY ((year, month, day), count, keyword)
);

CREATE TABLE countrylist_day (
	year 		int,
	month 		int,
	day 		int,
	country 	text,
	count 		int,
	PRIMARY KEY ((year, month, day), country, count)
);

