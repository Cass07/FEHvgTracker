DROP TABLE IF EXISTS "vg_info";
DROP TABLE IF EXISTS "vg_data";
DROP TABLE IF EXISTS "posts";
DROP TABLE IF EXISTS "hero_list";
DROP TABLE IF EXISTS "feh_user";

CREATE TABLE "feh_user" (
    `id` BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `picture` VARCHAR(255) NOT NULL,
    `role` VARCHAR(255) NOT NULL,
    `create_date` DATETIME,
    `modified_date` DATETIME
);

CREATE TABLE "hero_list" (
    `id` VARCHAR(35) NOT NULL PRIMARY KEY,
    `korname` VARCHAR(10) NOT NULL,
    `kornamesub` VARCHAR(20) NOT NULL,
    `jpname` VARCHAR(10) NOT NULL
);

CREATE TABLE "posts" (
    `id` BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(500) NOT NULL,
    `content` TEXT NOT NULL,
    `author` VARCHAR(255) NOT NULL,
    `create_date` DATETIME,
    `modified_date` DATETIME
);

CREATE TABLE "vg_data" (
    `id` BIGINT(20) AUTO_INCREMENT PRIMARY KEY,
    `vg_number` INT(10) NOT NULL,
    `team1score` VARCHAR(20) NOT NULL,
    `team2score` VARCHAR(20) NOT NULL,
    `team1index` INT(10),
    `team2index` INT(10),
    `round_number` INT(10),
    `tournament_index` INT(10),
    `time_index` INT(10)
);

CREATE TABLE "vg_info" (
    `id` BIGINT(20) AUTO_INCREMENT PRIMARY KEY NOT NULL,
    `vg_number` INT(10) NOT NULL,
    `vg_title` VARCHAR(255) NOT NULL,
    `vg_start_date` DATE NOT NULL,
    `team1id` VARCHAR(50),
    `team2id` VARCHAR(50),
    `team3id` VARCHAR(50),
    `team4id` VARCHAR(50),
    `team5id` VARCHAR(50),
    `team6id` VARCHAR(50),
    `team7id` VARCHAR(50),
    `team8id` VARCHAR(50)
);