-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 25, 2017 at 08:02 PM
-- Server version: 5.7.20-0ubuntu0.16.04.1
-- PHP Version: 7.0.22-0ubuntu0.16.04.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `Vertretungsplan`
--

-- --------------------------------------------------------

--
-- Table structure for table `Kurs`
--

CREATE TABLE `Kurs` (
  `idKurs` int(11) NOT NULL,
  `Name` varchar(45) DEFAULT NULL,
  `Stufe` int(11) DEFAULT NULL,
  `Fach` varchar(45) DEFAULT NULL,
  `Vertretungsplan` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Lehrer`
--

CREATE TABLE `Lehrer` (
  `idLehrer` int(11) NOT NULL,
  `Name` varchar(45) DEFAULT NULL,
  `Vertretungsplan` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Vertretungsart`
--

CREATE TABLE `Vertretungsart` (
  `idVertretungsart` int(11) NOT NULL,
  `Name` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Vertretungsplan`
--

CREATE TABLE `Vertretungsplan` (
  `idVertretungsplan` int(11) NOT NULL,
  `Schulname` varchar(45) DEFAULT NULL,
  `Ort` varchar(45) DEFAULT NULL,
  `Name` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Zeile`
--

CREATE TABLE `Zeile` (
  `idZeile` int(11) NOT NULL,
  `Fach` varchar(45) DEFAULT NULL,
  `Kurs` int(11) DEFAULT NULL,
  `Vertretungsplan` int(11) DEFAULT NULL,
  `Lehrer` int(11) DEFAULT NULL,
  `Art` int(11) DEFAULT NULL,
  `StundeVon` int(11) DEFAULT NULL,
  `StundeBis` int(11) DEFAULT NULL,
  `Kommentar` text,
  `Raum` text NOT NULL,
  `Datum` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Zugang`
--

CREATE TABLE `Zugang` (
  `idZugang` int(11) NOT NULL,
  `Benutzername` varchar(45) DEFAULT NULL,
  `Kennwort` varchar(128) DEFAULT NULL,
  `Zugangsart` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `Zugangsart`
--

CREATE TABLE `Zugangsart` (
  `idZugangsart` int(11) NOT NULL,
  `Name` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Kurs`
--
ALTER TABLE `Kurs`
  ADD PRIMARY KEY (`idKurs`),
  ADD UNIQUE KEY `Kurs_idKurs_uindex` (`idKurs`),
  ADD KEY `fk_Kurs_Vertretungsplan` (`Vertretungsplan`);

--
-- Indexes for table `Lehrer`
--
ALTER TABLE `Lehrer`
  ADD PRIMARY KEY (`idLehrer`),
  ADD UNIQUE KEY `Lehrer_idLehrer_uindex` (`idLehrer`),
  ADD KEY `fk_Lehrer_Vertretungsplan` (`Vertretungsplan`);

--
-- Indexes for table `Vertretungsart`
--
ALTER TABLE `Vertretungsart`
  ADD PRIMARY KEY (`idVertretungsart`),
  ADD UNIQUE KEY `Vertretungsart_idVertretungsart_uindex` (`idVertretungsart`);

--
-- Indexes for table `Vertretungsplan`
--
ALTER TABLE `Vertretungsplan`
  ADD PRIMARY KEY (`idVertretungsplan`),
  ADD UNIQUE KEY `Vertretungsplan_idVertretungsplan_uindex` (`idVertretungsplan`);

--
-- Indexes for table `Zeile`
--
ALTER TABLE `Zeile`
  ADD PRIMARY KEY (`idZeile`),
  ADD UNIQUE KEY `Zeile_idZeile_uindex` (`idZeile`),
  ADD KEY `fk_Vertretungsplan_Zeile_idx` (`Vertretungsplan`),
  ADD KEY `fk_Zeile_Lehrer_idx` (`Lehrer`),
  ADD KEY `fk_Zeile_Kurs_idx` (`Kurs`),
  ADD KEY `fk_Zeile_Vertretungsart_idx` (`Art`);

--
-- Indexes for table `Zugang`
--
ALTER TABLE `Zugang`
  ADD PRIMARY KEY (`idZugang`),
  ADD UNIQUE KEY `Zugang_idZugang_uindex` (`idZugang`),
  ADD KEY `fk_Zugang_Zugangsart_idx` (`Zugangsart`);

--
-- Indexes for table `Zugangsart`
--
ALTER TABLE `Zugangsart`
  ADD PRIMARY KEY (`idZugangsart`),
  ADD UNIQUE KEY `Zugangsart_idZugangsart_uindex` (`idZugangsart`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Kurs`
--
ALTER TABLE `Kurs`
  MODIFY `idKurs` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Lehrer`
--
ALTER TABLE `Lehrer`
  MODIFY `idLehrer` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Vertretungsart`
--
ALTER TABLE `Vertretungsart`
  MODIFY `idVertretungsart` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Vertretungsplan`
--
ALTER TABLE `Vertretungsplan`
  MODIFY `idVertretungsplan` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Zeile`
--
ALTER TABLE `Zeile`
  MODIFY `idZeile` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Zugang`
--
ALTER TABLE `Zugang`
  MODIFY `idZugang` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `Zugangsart`
--
ALTER TABLE `Zugangsart`
  MODIFY `idZugangsart` int(11) NOT NULL AUTO_INCREMENT;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `Kurs`
--
ALTER TABLE `Kurs`
  ADD CONSTRAINT `fk_Kurs_Vertretungsplan` FOREIGN KEY (`Vertretungsplan`) REFERENCES `Vertretungsplan` (`idVertretungsplan`);

--
-- Constraints for table `Lehrer`
--
ALTER TABLE `Lehrer`
  ADD CONSTRAINT `fk_Lehrer_Vertretungsplan` FOREIGN KEY (`Vertretungsplan`) REFERENCES `Vertretungsplan` (`idVertretungsplan`);

--
-- Constraints for table `Zeile`
--
ALTER TABLE `Zeile`
  ADD CONSTRAINT `fk_Zeile_Art` FOREIGN KEY (`Art`) REFERENCES `Vertretungsart` (`idVertretungsart`),
  ADD CONSTRAINT `fk_Zeile_Kurs` FOREIGN KEY (`Kurs`) REFERENCES `Kurs` (`idKurs`),
  ADD CONSTRAINT `fk_Zeile_Lehrer` FOREIGN KEY (`Lehrer`) REFERENCES `Lehrer` (`idLehrer`),
  ADD CONSTRAINT `fk_Zeile_Vertretugnsplan` FOREIGN KEY (`Vertretungsplan`) REFERENCES `Vertretungsplan` (`idVertretungsplan`);

--
-- Constraints for table `Zugang`
--
ALTER TABLE `Zugang`
  ADD CONSTRAINT `fk_Zugang_Zugangsart` FOREIGN KEY (`Zugangsart`) REFERENCES `Zugangsart` (`idZugangsart`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
