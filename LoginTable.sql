-- MySQL Script generated by MySQL Workbench
-- Sun Apr 16 16:44:16 2023
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema LoginDB
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema LoginDB
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `LoginDB` DEFAULT CHARACTER SET utf8 ;
USE `LoginDB` ;

-- -----------------------------------------------------
-- Table `LoginDB`.`Users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `LoginDB`.`Users` (
  `UserName` varchar(32) NOT NULL,
  `Password` varchar(32) NOT NULL,
  PRIMARY KEY (`UserName`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
