-- phpMyAdmin SQL Dump
-- version 4.2.12deb2+deb8u2
-- http://www.phpmyadmin.net
--
-- Client :  localhost
-- Généré le :  Mar 20 Décembre 2016 à 09:43
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
-- Structure de la table `subscribe`
--

CREATE TABLE IF NOT EXISTS `subscribe` (
`id` int(11) NOT NULL,
  `fk_user_id` int(11) NOT NULL,
  `fk_vitrine_id` int(11) NOT NULL
) ENGINE=MyISAM AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;

--
-- Contenu de la table `subscribe`
--

INSERT INTO `subscribe` (`id`, `fk_user_id`, `fk_vitrine_id`) VALUES
(19, 1, 12),
(17, 1, 9),
(16, 1, 8),
(15, 1, 5),
(13, 1, 13),
(30, 1, 4),
(29, 1, 14);

--
-- Index pour les tables exportées
--

--
-- Index pour la table `subscribe`
--
ALTER TABLE `subscribe`
 ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `subscribe`
--
ALTER TABLE `subscribe`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=33;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
