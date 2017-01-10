-- phpMyAdmin SQL Dump
-- version 4.2.12deb2+deb8u2
-- http://www.phpmyadmin.net
--
-- Client :  localhost
-- Généré le :  Mar 20 Décembre 2016 à 09:42
-- Version du serveur :  5.5.53-0+deb8u1
-- Version de PHP :  5.6.29-0+deb8u1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données :  `j4kim`
--

-- --------------------------------------------------------

--
-- Structure de la table `vitrine`
--

CREATE TABLE IF NOT EXISTS `vitrine` (
`id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `radius` int(11) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `lastPostDate` date NOT NULL,
  `color` varchar(12) NOT NULL DEFAULT 'blue'
) ENGINE=MyISAM AUTO_INCREMENT=38 DEFAULT CHARSET=latin1;

--
-- Contenu de la table `vitrine`
--

INSERT INTO `vitrine` (`id`, `name`, `radius`, `latitude`, `longitude`, `lastPostDate`, `color`) VALUES
(13, 'Suze', 300, 47.149228, 7.003468, '2016-11-24', '#cc911c'),
(4, 'HE-Arc', 123, 46.997637, 6.938717, '2016-11-09', '#55ff00'),
(5, 'Gare', 212, 46.996914, 6.93576, '2016-11-23', '#ff5500'),
(8, 'Parc technologique St-Imier', 356, 47.154859, 7.002969, '2016-11-23', '#00ff55'),
(9, 'La Chaux-de-Fonds', 1400, 47.103189, 6.8272, '2016-11-28', '#0055ff'),
(12, 'Le Coyote Bar', 100, 47.101729, 6.825017, '2016-11-11', '#000000'),
(14, 'Gare de St-Imier', 200, 47.151649, 7.001159, '2016-11-16', '#1cc0cc');

--
-- Index pour les tables exportées
--

--
-- Index pour la table `vitrine`
--
ALTER TABLE `vitrine`
 ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `vitrine`
--
ALTER TABLE `vitrine`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=38;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
