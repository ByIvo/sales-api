-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema sales_api
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema sales_api
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `sales_api` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `sales_api` ;

-- -----------------------------------------------------
-- Table `sales_api`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sales_api`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '				\n',
  `name` VARCHAR(80) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `password` VARCHAR(512) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sales_api`.`items`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sales_api`.`items` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(80) NOT NULL,
  `image` VARCHAR(512) NULL,
  `price` DOUBLE NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sales_api`.`user_items`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sales_api`.`user_items` (
  `user_id` INT NOT NULL,
  `item_id` INT NOT NULL,
  `quantity` INT NOT NULL DEFAULT 1,
  INDEX `fk_users_items_users_idx` (`user_id` ASC),
  INDEX `fk_users_items_items1_idx` (`item_id` ASC),
  PRIMARY KEY (`user_id`, `item_id`),
  CONSTRAINT `fk_users_items_users`
    FOREIGN KEY (`user_id`)
    REFERENCES `sales_api`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_items_items1`
    FOREIGN KEY (`item_id`)
    REFERENCES `sales_api`.`items` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sales_api`.`sells`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sales_api`.`sells` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` DATETIME NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_sells_users1_idx` (`user_id` ASC),
  CONSTRAINT `fk_sells_users1`
    FOREIGN KEY (`user_id`)
    REFERENCES `sales_api`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sales_api`.`sell_items`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sales_api`.`sell_items` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `price` DOUBLE NOT NULL DEFAULT 0,
  `quantity` INT NOT NULL DEFAULT 1,
  `item_id` INT NOT NULL,
  `sell_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_sell_items_items1_idx` (`item_id` ASC),
  INDEX `fk_sell_items_sells1_idx` (`sell_id` ASC),
  CONSTRAINT `fk_sell_items_items1`
    FOREIGN KEY (`item_id`)
    REFERENCES `sales_api`.`items` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_sell_items_sells1`
    FOREIGN KEY (`sell_id`)
    REFERENCES `sales_api`.`sells` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
