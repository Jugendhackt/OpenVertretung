-- MySQL Script generated by MySQL Workbench
-- Sa 25 Nov 2017 11:59:44 CET
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema Vertretungsplan
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema Vertretungsplan
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `Vertretungsplan` DEFAULT CHARACTER SET utf8 ;
USE `Vertretungsplan` ;

-- -----------------------------------------------------
-- Table `Vertretungsplan`.`Vertretungsplan`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Vertretungsplan`.`Vertretungsplan` (
  `idVertretungsplan` INT NOT NULL,
  `Schulname` VARCHAR(45) NULL,
  `Ort` VARCHAR(45) NULL,
  `Name` VARCHAR(45) NULL,
  PRIMARY KEY (`idVertretungsplan`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Vertretungsplan`.`Lehrer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Vertretungsplan`.`Lehrer` (
  `idLehrer` INT NOT NULL,
  `Name` VARCHAR(45) NULL,
  PRIMARY KEY (`idLehrer`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Vertretungsplan`.`Kurs`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Vertretungsplan`.`Kurs` (
  `idKurs` INT NOT NULL,
  `Name` VARCHAR(45) NULL,
  `Stufe` INT NULL,
  `Fach` VARCHAR(45) NULL,
  PRIMARY KEY (`idKurs`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Vertretungsplan`.`Vertretungsart`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Vertretungsplan`.`Vertretungsart` (
  `idVertretungsart` INT NOT NULL,
  `Name` VARCHAR(45) NULL,
  PRIMARY KEY (`idVertretungsart`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Vertretungsplan`.`Zeile`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Vertretungsplan`.`Zeile` (
  `idZeile` INT NOT NULL,
  `Fach` VARCHAR(45) NULL,
  `Kurs` INT NULL,
  `Vertretungsplan` INT NULL,
  `Lehrer` INT NULL,
  `Art` INT NULL,
  `StundeVon` INT NULL,
  `StundeBis` INT NULL,
  PRIMARY KEY (`idZeile`),
  INDEX `fk_Vertretungsplan_Zeile_idx` (`Vertretungsplan` ASC),
  INDEX `fk_Zeile_Lehrer_idx` (`Lehrer` ASC),
  INDEX `fk_Zeile_Kurs_idx` (`Kurs` ASC),
  INDEX `fk_Zeile_Vertretungsart_idx` (`Art` ASC),
  CONSTRAINT `fk_Vertretungsplan_Zeile`
    FOREIGN KEY (`Vertretungsplan`)
    REFERENCES `Vertretungsplan`.`Vertretungsplan` (`idVertretungsplan`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Zeile_Lehrer`
    FOREIGN KEY (`Lehrer`)
    REFERENCES `Vertretungsplan`.`Lehrer` (`idLehrer`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Zeile_Kurs`
    FOREIGN KEY (`Kurs`)
    REFERENCES `Vertretungsplan`.`Kurs` (`idKurs`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Zeile_Vertretungsart`
    FOREIGN KEY (`Art`)
    REFERENCES `Vertretungsplan`.`Vertretungsart` (`idVertretungsart`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Vertretungsplan`.`Zugangsart`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Vertretungsplan`.`Zugangsart` (
  `idZugangsart` INT NOT NULL,
  `Name` VARCHAR(45) NULL,
  PRIMARY KEY (`idZugangsart`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Vertretungsplan`.`Zugang`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Vertretungsplan`.`Zugang` (
  `idZugang` INT NOT NULL,
  `Benutzername` VARCHAR(45) NULL,
  `Kennwort` VARCHAR(128) NULL,
  `Zugangsart` INT NULL,
  PRIMARY KEY (`idZugang`),
  INDEX `fk_Zugang_Zugangsart_idx` (`Zugangsart` ASC),
  CONSTRAINT `fk_Zugang_Zugangsart`
    FOREIGN KEY (`Zugangsart`)
    REFERENCES `Vertretungsplan`.`Zugangsart` (`idZugangsart`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
